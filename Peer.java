import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.*;
//import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;



public class Peer {
	private static int [] peerPorts = { 11, 12, 13, 14, 15, 16 };
	// private static int nextPort = 0;
	private int port;
	private String peerId;
	//private ServerSocket sev;
	//private Socket soc;
	private ArrayList<Socket> myPeers;
	private ServerSocket [] myServers;
	//private ArrayList<Socket> myPeersCli;
	private boolean [] connected;
	private int numConnectedSev;
	private HashMap<String, Integer> mapPeers = new HashMap<String, Integer>();
	public Peer (String peerId) {
		//System.out.println("Test");
		try {
			// port = nextPort;
			// peerPorts.add(port);
			// nextPort++;
			port = Integer.parseInt(peerId);
			this.peerId = peerId;
			//sev = new ServerSocket(port);
			//sev.setSoTimeout(100);
			//soc = new Socket();
			myServers = new ServerSocket[5];
			
			myPeers = new ArrayList<Socket>();
			//myPeersCli = new ArrayList<Socket>();
			numConnectedSev = 1;
			connected = new boolean [peerPorts.length];
			int k = 0;
			for (int i = 0; i < peerPorts.length; i++) {
				if (peerPorts[i] == port) {
					connected[i] = true;
				}
				else {
					myServers[k++] = new ServerSocket(createAddressPort(peerPorts[i]));
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
					myPeers.add(new Socket("localhost", createToPort(peerPorts[i])));
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
				for (int i = 0; i < myServers.length; i++) {
					System.out.println("Server " + myServers[i] + " is Waiting");
					myServers[i].accept();
					//if (!myPeers.contains(p)) {
						//myPeersCli.add(p);
					numConnectedSev++;
				}
				//}
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
	// public void sendHeaderToPeer(int index) {
		// Socket p = myPeers[index];
		// byte[] message = new byte[32];
		// message[0] = 'H';
		// message[1] = 'E';
		// message[2] = 'L';
		// message[3] = 'L';
		// message[4] = 'O';
		// DataOutputStream out = new DataOutputStream(p.getOutputStream());
		// out.write(message, 0, 32);
		// out.write(peerId.getBytes(), 0, 4);
	// }
	// public void receiveHeaderFromPeer(int index) {
		// Socket p = myPeers[index];
		// byte[] message;
		// DataInputStream in = new DataInputStream(p.getInputStream());
		// in.read(message, 0, 32);
		// byte [] id;
		// in.read(id, 0, 4);
		// String pId = new String(id);
		// mapPeers.put(pId, index);
	// }
	// public void sendToPeer(String message, String peerId) {
		// Socket p = myPeers[mapPeers.get(peerId)];
		// DataOutputStream out = new DataOutputStream(p.getOutputStream());
		// out.writeUTF(message);
	// }
	// public String receiveFromPeer(String peerId) {
		// Socket p = myPeers[mapPeers.get(peerId)];
		// DataInputStream in = new DataInputStream(p.getInputStream());
		// return in.readLine();
	// }
	public int createToPort(int port) {
		return Integer.parseInt(port+peerId);
	}
	public int createAddressPort(int port) {
		return Integer.parseInt(peerId+port);
	}
}