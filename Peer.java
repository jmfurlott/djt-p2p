import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Peer {
	private static int [] peerPorts = { 1001, 1002, 1003, 1004, 1005, 1006 };
	// private static int nextPort = 0;
	private int port;
	private ServerSocket sev;
	private Socket soc;
	private ArrayList<Socket> myPeers;
	public Peer (String peerId) {
		//System.out.println("Test");
		try {
			// port = nextPort;
			// peerPorts.add(port);
			// nextPort++;
			port = Integer.parseInt(peerId);
			sev = new ServerSocket(port);
			soc = new Socket();
			myPeers = new ArrayList<Socket>();
		}
		catch (Exception e) {
			//System.out.println("WTF");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void connect() {
		//System.out.println("Test");
		try {
			for (int i = 0; i < peerPorts.length; i++) { // && !soc.isConnected(); i++) {
				System.out.print("PortMe: " + port + " To Port: " + peerPorts[i] );
				if (peerPorts[i] != port) {
					soc = new Socket("localhost", peerPorts[i]);
					System.out.println(" Worked!");
					System.out.println("Socket: " + soc.toString());
				}
				else {
					System.out.println(" Failed!");
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void accept() {
		try {
			System.out.println("Waiting");
			Socket p = sev.accept();
			myPeers.add(p);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}