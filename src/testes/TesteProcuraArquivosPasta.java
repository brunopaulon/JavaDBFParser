package testes;

import java.io.File;

public class TesteProcuraArquivosPasta {
	
	public static void main(String[] args) {
		File dir = new File("C:\\teste");
		
		File[] txtchildren = dir.listFiles();
		for (int i = 0; i < txtchildren.length; i++) {
			if(txtchildren[i].isDirectory()){
				
			}
		}
		
		System.out.println("OK");
	}
	
}
