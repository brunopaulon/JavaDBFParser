package diseaseParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import entities.DiseaseOccurrence;

import parser.DBFParserException;
import parser.FileListing;

public class Main2 {

	public static void main(String[] args){
		Main2 d = new Main2();
		d.parseFiles("C:\\teste2");
	}
	
	private void parseFiles(String directory){
		File dir = new File(directory);
		ArrayList<File> files = new ArrayList<File>();
		try {
			files = new FileListing().listFiles(dir);

		} catch (DBFParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			if(files.size() != 0) {
				for (File file : files) {
					parseFile(file);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	private void parseFile(File file) {
		System.out.println(file.toString());
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("medic_base");

		EntityManager em = factory.createEntityManager();
		if(file.exists()){
			InputStream is = null;
			try{
				is = new FileInputStream(file);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			byte[] firstHeader = new byte[32];
			try {
				is.read(firstHeader, 0, 32);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			int firstDataRecord = noComplement(firstHeader[8]) + noComplement(firstHeader[9]) * 256;
			int lengthDataRecord = noComplement(firstHeader[10]) + noComplement(firstHeader[11]) * 256;
						
			System.out.println("first: " + firstDataRecord + " length: " + lengthDataRecord);
			byte[] secondHeader = new byte[firstDataRecord - 32];
			try{
				is.read(secondHeader, 0, firstDataRecord - 32);
			} catch (IOException e){
				e.printStackTrace();
			}

			byte[] row = new byte[lengthDataRecord];
			
			// Entity variables
			
			boolean reading = true;
			
			try{
				while(reading) {
					String otherDisease = "";
					
					int bytesRead = is.read(row, 0, lengthDataRecord);
					if(bytesRead < lengthDataRecord)
						break;
					if(row[0] == 0x20){
						DiseaseOccurrence disOc = new DiseaseOccurrence();
						disOc.setOccurrenceDate(dateFromBytes(row, 26, 31));
						disOc.setCpf(stringFromBytes(row, 32, 42));
						disOc.setBirthProvince(stringFromBytes(row, 43, 44));
						disOc.setZipCode(stringFromBytes(row, 45, 52));
						disOc.setBirthday(dateFromBytes(row, 60, 67));
						disOc.setGender(stringFromBytes(row, 68, 68));
						disOc.setDisease(stringFromBytes(row, 75, 78));
						otherDisease = stringFromBytes(row, 81, 84);
						em.getTransaction().begin();
						em.persist(disOc);
						em.getTransaction().commit();
						
						if(otherDisease != null && !otherDisease.replace(" ", "").equals("")){
							DiseaseOccurrence disOc2 = new DiseaseOccurrence();
							disOc2.setOccurrenceDate(disOc.getOccurrenceDate());
							disOc2.setCpf(disOc.getCpf());
							disOc2.setBirthProvince(disOc.getBirthProvince());
							disOc2.setZipCode(disOc.getZipCode());
							disOc2.setBirthday(disOc.getBirthday());
							disOc2.setGender(disOc.getGender());
							disOc2.setDisease(otherDisease);
							
							em.getTransaction().begin();
							em.persist(disOc2);
							em.getTransaction().commit();
						}
						
						System.out.println("occurrenceDate: " + disOc.getOccurrenceDate().toString() + " Cpf:" + disOc.getCpf() + " birhPlace: " + disOc.getBirthProvince() + " zipCode: " + disOc.getZipCode()
								+ " birthday: " + disOc.getBirthday().toString() + " gender: " + disOc.getGender() + " disease: " + disOc.getDisease() + " secondDisease: " + otherDisease);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
			

			
		}
	}
	
	private int noComplement(int anyNumber){
		if(anyNumber < 0)
			return 256 + anyNumber;
		else 
			return anyNumber;
	}
	
	private Date dateFromBytes(byte[] row, int initial, int end){
		String dateStr = stringFromBytes(row, initial, end);
		
		Calendar c = Calendar.getInstance();
		c.clear();
		if(dateStr.length() >= 8)
			c.set(Integer.valueOf(dateStr.substring(0, 4)), 
				  Integer.valueOf(dateStr.substring(4, 6)) - 1,
				  Integer.valueOf(dateStr.substring(6, 8)));
		else if(dateStr.length() >= 6)
			c.set(Integer.valueOf(dateStr.substring(0, 4)), 
				  Integer.valueOf(dateStr.substring(4, 6)) - 1, 1);
		else
			return null;

		return c.getTime();
	}
	
	private String stringFromBytes(byte[] row, int initial, int end) {
		String result = "";
		if(row.length < end)
			return "";
		for (int i = initial; i <= end; i++) {
			result += new Character((char) row[i]);
		}
		return result;
	}
}
