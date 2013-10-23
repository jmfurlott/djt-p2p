import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.*;
//import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;



public class Peer {
	private static int [] peerPorts = { 1001, 1002, 1003, 1004, 1005, 1006 };
	// private static int nextPort = 0;
	private int port;
	private ServerSocket sev;
	private Socket soc;
	private ArrayList<Socket> myPeers;
	private boolean [] connected;
	private int numConnectedSev;
	public Peer (String peerId) {
		//System.out.println("Test");
		try {
			// port = nextPort;
			// peerPorts.add(port);
			// nextPort++;
			port = Integer.parseInt(peerId);
			sev = new ServerSocket(port);
			//sev.setSoTimeout(100);
			soc = new Socket();
			myPeers = new ArrayList<Socket>();
			numConnectedSev = 1;
			connected = new boolean [peerPorts.length];
			for (int i = 0; i < peerPorts.length; i++) {
				if (peerPorts[i] == port) {
					connected[i] = true;
				}
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
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
				if (!connected[i]) {
					myPeers.add(new Socket("localhost", peerPorts[i]));
					//soc.connect(new InetSocketAddress("localhost", peerPorts[i]));\=
					System.out.println(" Worked!");
					System.out.println("Socket: " + myPeers.get(myPeers.size()-1).toString());
					connected[i] = true;
				}
				else {
					System.out.println(" Failed!");
				}
			}
		}
		catch (Exception e) {
			System.out.println("\n"+Arrays.toString(e.getStackTrace()));
			// e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void accept() {
		if (numConnectedSev < peerPorts.length) { 
			try {
				System.out.println("Waiting");
				Socket p = sev.accept();
				if (!myPeers.contains(p)) {
					myPeers.add(p);
					numConnectedSev++;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	public boolean serverFullyConnected() {
		if (numConnectedSev < peerPorts.length) {
			return false;
		}
		return true;
	}
	
	public boolean socketsFullyConnected() {
		for (int i = 0; i < connected.length; i++) {
			if (!connected[i]) {
				return false;
			}
		}
		return true;
	}
	
}