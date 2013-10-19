import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.net.ServerSocket;

public class Main2 {
	public static void main (String args[]) {
		Peer first = new Peer();
		System.out.println("Main2: Peer created");		
		first.connect();
		System.out.println("Connected");
		first.accept();
		System.out.println("Accept");
	}
}