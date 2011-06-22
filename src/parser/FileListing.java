package parser;

import java.io.File;
import java.util.ArrayList;

public class FileListing {
	// TODO pegar arquivos apenas do tipo .dbf e tambem mostrar algumas estatisticas
	public ArrayList<File> listFiles(File directory) throws DBFParserException {
		if(directory.isDirectory()){
			ArrayList<File> files = new ArrayList<File>();
			File[] children = directory.listFiles();
			for (int i = 0; i < children.length; i++) {
				if(children[i].isFile()){
					files.add(children[i]);
				}
			}
			return files;
			
		} else {
			throw new DBFParserException();
		}
	}
}
