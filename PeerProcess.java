import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.*;

public class PeerProcess {
	public static void main (String args[]) {
		System.out.println("Running");
		//System.out.println(Arrays.toString(args));
		Peer peer = new Peer(args[0]);
		while (true) {
		//System.out.println("TY");
			peer.connect();
			peer.accept();
		}
	}
	
}