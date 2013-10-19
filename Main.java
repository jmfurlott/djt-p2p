import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.net.ServerSocket;

public class Main {
	public static void main (String args[]) {
		Peer first = new Peer();
		System.out.println("Main: Peer created");
		first.accept();
		System.out.println("Accept");
		first.connect();
		System.out.println("Connected");
	}
}