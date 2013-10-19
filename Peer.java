import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Peer {
	private static ArrayList<Integer> peerPorts = new ArrayList<Integer>();
	private static int nextPort = 0;
	private int port;
	private ServerSocket sev;
	private Socket soc;
	private ArrayList<Socket> myPeers;
	public Peer () {
		try {
			port = nextPort;
			peerPorts.add(port);
			nextPort++;
			
			sev = new ServerSocket(port);
			soc = new Socket();
			myPeers = new ArrayList<Socket>();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void connect() {
		try {
			for (int i = 0; i < peerPorts.size() && !soc.isConnected(); i++) {
				System.out.println("I: " + i);
				if (peerPorts.get(i) != port) {
					soc = new Socket("localhost", peerPorts.get(i));
					System.out.println("Socket: " + soc.toString());
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