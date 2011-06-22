package testes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TesteLeituraArquivoDBF {
	public static void notmain(String[] args){
		File file = new File("UDSP0103.DBF");
		byte[] bytes = null;
		try {
			bytes = getBytesFromFile(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(bytes != null){
			System.out.println(bytes.toString());
		}
	}
    public static byte[] getBytesFromFile(File file) throws IOException {
    	if(file.exists()){
	        InputStream is = new FileInputStream(file);
	    
	        // Get the size of the file
	        long length = file.length();
	    
	        if (length > Integer.MAX_VALUE) {
	            throw new IOException("File is too large " + file.getName());
	        }
	    
	        // Create the byte array to hold the data
	        byte[] bytes = new byte[(int)length];
	    
	        // Read in the bytes
	        int offset = 0;
	        int numRead = 0;
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
	        return bytes;
    	} else {
    		return null;
    	}
        
    }
}
