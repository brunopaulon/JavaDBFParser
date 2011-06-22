package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.tools.query.QueryBuilder;

import entities.FieldDBF;
import entities.TableDBF;

public class ParseDBF {
	/*
	 *  The accepted extensions that the program currently accepts
	 *  They must be all small caps
	 */
	private String[] ACCEPTED_EXTENSIONS = new String[] { "dbf" };
	byte[] bytes;
	
	public static void initiate_parser(String[] args){
		ParseDBF d = new ParseDBF();
		d.parseDBF("C:\\teste");
	}
	
	public void parseDBF(String directory){
		File dir = new File(directory);
		ArrayList<File> files = new ArrayList<File>();
		try {
			files = new FileListing().listFiles(dir);
			
		} catch (DBFParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("medic_base");
			
			EntityManager em = factory.createEntityManager();
			
			if(files.size() != 0) {
				for (File file : files) {
					if (testForUniqueness(file, em)) {
						TableDBF t = parseSingleDBF(file);
						em.getTransaction().begin();
						em.persist(t);
						em.getTransaction().commit();
						ArrayList<FieldDBF> list = parseFields(t.getId());
						for (FieldDBF f : list) {
							em.getTransaction().begin();
							em.persist(f);
							em.getTransaction().commit();
						}
					}
				}
				
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TableDBF parseSingleDBF(File file) {
		if(file.exists()){
			InputStream is = null;
			try{
				is = new FileInputStream(file);
			} catch(Exception e) {
				// What TODO?
			}
			
	        byte[] bytesAux = new byte[10];
	        
	        try {
	        	is.read(bytesAux, 0, 10);
			} catch (Exception e) {
				// TODO: handle exception
			}
	        
			long length = noComplement(bytesAux[8]) + noComplement(bytesAux[9]) * 256 + 1;
	        
			bytes = new byte[(int)length];
	        // Read in the bytes
	        int offset = 10;
	        int numRead = 0;
	        try {
				for (int i = 0; i < bytesAux.length; i++) {
					bytes[i] = bytesAux[i];
				}
			
		        while (offset < bytes.length
		               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
		            offset += numRead;
		        }
		    
		        // Ensure all the bytes have been read in
		        if (offset < bytes.length) {
		            throw new IOException("Could not completely read file "+file.getName());
		        }
		    
		        // Close the input stream and return bytes
		        is.close();
		        
	        } catch (Exception e) {
				// TODO: handle exception
			}
	        Utils utils = new Utils();
	        TableDBF t = new TableDBF();
	        t.setCreatedAt(new Date());
	        t.setDbfFileType(utils.getDBFFileType(bytes[0]));
	        t.setLastUpdate(utils.getLastUpdate(bytes[1], bytes[2], bytes[3]));
	        t.setModifiedAt(new Date());
	        t.setNumberRecords(getNumberOfRecords(bytes[4], bytes[5], bytes[6], bytes[7]));
	        t.setTableFlag(utils.getTableFlag(bytes[28]));
	        //TODO remover o .dbf do nome do arquivo
	        t.setName(testAndRemoveExtension(file));
	        
	        return t;
    	} 
		return null;

	}
	
	private ArrayList<FieldDBF> parseFields(int tableDBFId){
		// The fields start on the byte 32 (there are the 0 - 31 before them)
		ArrayList<FieldDBF> list = new ArrayList<FieldDBF>();
		
		Utils utils = new Utils();
		for (int i = 32; i < bytes.length; i++) {
			// Each field has 32 bytes (0 - 31)

			FieldDBF f = new FieldDBF();
			String fieldName = "";
			String reserved = "";
			boolean include = true;
			for (int j = 0; j < 32; j++) {
				// the field name has maximum 10 characters
				if(j < 11){
					if(bytes[i+j] == 0x0d){
						include = false;
						break;
					}
					if(bytes[i+j] != 0)
						fieldName += new Character((char) bytes[i+j]);
				} else if(j == 11)
					f.setFieldType(utils.getFieldTypeFromByte(bytes[i+j]));
				else if(j <= 15)
					// TODO what is this displacement for?
					;
				else if(j == 16)
					f.setFieldLength(noComplement(bytes[i+j]));
				else if(j == 17)
					// TODO what is this number of decimal places for??
					f.setDecimalPlaces(noComplement(bytes[i+j]));
				else if(j == 18)
					f.setFieldFlags(utils.getFieldFlagFromByte(bytes[i+j]));
				else if(j == 19){
					f.setAutoIncrNext(noComplement(bytes[i+j]) + 256*noComplement(bytes[i+j+1]) +
							256*256*noComplement(bytes[i+j + 2])+ 256*256*256*noComplement(bytes[i+j+3]));
					j = 22;
				} else if(j == 23)
					f.setAutoIncrStep(bytes[i+j]);
				else if(j <= 31)
					reserved += bytes[i+j];
			}
			if(include){
				f.setReserved(reserved);
				f.setFieldName(fieldName);
				f.setTableDBFId(tableDBFId);
				list.add(f);
			}
			i += 31;
		}
		return list;
	}
	
	private int getNumberOfRecords(byte part_1, byte part_2, byte part_3, byte part_4 ) {
		return noComplement(part_1) + 256*noComplement(part_2) + 256*256*noComplement(part_3) + 256*256*256*noComplement(part_4);
	}
	
	private int noComplement(int anyNumber){
		if(anyNumber < 0)
			return 256 + anyNumber;
		else 
			return anyNumber;
	}
	

	
	private boolean testForUniqueness(File file, EntityManager em){
		// TODO colocar método em algum dao ou coisa assim
		
		List<TableDBF> result = null;
		try{
			result = em.createQuery("from TableDBF as table where table.name = ?1", TableDBF.class)
			.setParameter(1, testAndRemoveExtension(file))
			.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		if(result == null || result.size() == 0)
			return true;
		else 
			return false;
		
	}	
	
	private String testAndRemoveExtension(File file){
		String regex = "\\.(";
		for (int i = 0; i < ACCEPTED_EXTENSIONS.length; ++i) {
			if(ACCEPTED_EXTENSIONS.length > 1){
				if ( i != 0)
					regex += "|(" + ACCEPTED_EXTENSIONS[i] + ")";
				else
					regex += "(" + ACCEPTED_EXTENSIONS[i] + ")";
			} else {
				regex += ACCEPTED_EXTENSIONS[i];
			}
			
		}
		regex += ")";
		
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(file.getName());
		if(m.find()) {
			String[] names = p.split(file.getName());
			if(names.length > 0)
				return names[0];
		}
		
		return null;
	}
}
