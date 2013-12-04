import java.io.*;
import java.util.*;

public class FileSplitter {

	public FileSplitter() {
	
	}

	public static boolean splitFile(File f, int sizeOfFiles, String peerID) throws IOException {

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
		FileOutputStream out;

		String name = f.getName();
		int pos = name.lastIndexOf(".");
		if (pos > 0) {
			name = name.substring(0, pos);
		}
		int partCounter = 0;
		//int sizeOfFiles = 1024*1024; //1 mb
		
		byte[] buffer = new byte[sizeOfFiles];
		int tmp = 0;

		while((tmp = bis.read(buffer)) > 0) {
			//creat new dir first
			String dirname = f.getParent() + "/" + peerID + "/";
			File newDir = new File(dirname);
			newDir.mkdirs();
			String filename = f.getParent() +"/"+peerID + "/" + name + partCounter++;
			File newFile = new File(filename);

			System.out.println("FILE created: " + filename);
			newFile.createNewFile();
			out = new FileOutputStream(newFile);
			out.write(buffer, 0, tmp);
			System.out.println("FILE written: " + filename);

			out.close();

		}	

		return true;


	}
/*
	public ArrayList<byte[]> splitReturnFiles(File f, int sizeOfFiles) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
		ArrayList<byte[]> splitFiles = new ArrayList<byte[]>();
		byte[] buffer = new byte[sizeOfFiles];
		int partCounter = 1;
		int tmp = 0;
		while((tmp = bis.read(buffer)) > 0) {
			
		}

*/


//	public static void main(String[] args) throws IOException {
//		boolean test = splitFile(new File("test.txt"), 1024*1024);
//	}
	
}
