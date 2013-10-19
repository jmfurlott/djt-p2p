import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.net.ServerSocket;

public class MySocket {
	public static void main (String args[]) {
		try {
			Socket soc = new Socket("localhost", 100);
			DataOutputStream out = new DataOutputStream(soc.getOutputStream());
			out.writeUTF("Sup derek");
			DataInputStream in =  new DataInputStream(soc.getInputStream());
			String re = in.readUTF();
			System.out.println("Message from server: " + re);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}