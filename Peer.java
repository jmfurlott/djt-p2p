import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.*;
//import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.nio.ByteBuffer;



public class Peer {
	private static int [] peerPorts = { 11, 12, 13, 14, 15, 16 };
	// private static int nextPort = 0;
	private int myPort;
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
			myPort = Integer.parseInt(peerId);
			this.peerId = peerId+"00";
			//sev = new ServerSocket(myPort);
			//sev.setSoTimeout(100);
			//soc = new Socket();
			myServers = new ServerSocket[5];
			
			myPeers = new ArrayList<Socket>();
			//myPeersCli = new ArrayList<Socket>();
			numConnectedSev = 1;
			connected = new boolean [peerPorts.length];
			int k = 0;
			for (int i = 0; i < peerPorts.length; i++) {
				if (peerPorts[i] == myPort) {
					connected[i] = true;
				}
				else {
					System.out.println("HERE");
					myServers[k++] = createServer("localhost", peerPorts[i]);
					System.out.println("NOT HERE");
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
				//System.out.print("PortMe: " + myPort + " To Port: " + peerPorts[i] );
				if (!connected[i]) {
					myPeers.add(new Socket("localhost", createToPort(peerPorts[i])));
					//soc.connect(new InetSocketAddress("localhost", peerPorts[i]));\=
					//System.out.println(" Worked!");
					//System.out.println("Socket: " + myPeers.get(myPeers.size()-1).toString());
					connected[i] = true;
				}
				else {
					//System.out.println(" Failed!");
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
					//System.out.println("Server " + myServers[i] + " is Waiting");
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
	public void sendHandShakeToPeer(int index) {
		try {
			System.out.println("Trying to send");
			Socket p = myPeers.get(index);
			byte[] message = new byte[32];
			message[0] = 'H';
			message[1] = 'E';
			message[2] = 'L';
			message[3] = 'L';
			message[4] = 'O';
			DataOutputStream out = new DataOutputStream(p.getOutputStream());
			out.write(message, 0, 32);
			out.write(peerId.getBytes(), 0, 4);
			System.out.println("Sent " + new String(message) + " with pId " +peerId);
		} catch (Exception e) {
			System.out.println("\n"+Arrays.toString(e.getStackTrace()));
			// e.printStackTrace();
			System.exit(1);
		}
	}
	public void receiveHandShakeFromPeer(int index) {
		try {
			Socket p = myPeers.get(index);
			byte[] message = new byte[5];
			DataInputStream in = new DataInputStream(p.getInputStream());
			System.out.println("Before Read");
			in.read(message, 0, 5);
			System.out.println("After Read");
			if ("HELLO".equals(new String(message))) {
				in.read(new byte[27], 0, 27);
				byte [] id = new byte[4];
				in.read(id, 0, 4);
				String pId = new String(id);
				mapPeers.put(pId, index);
				System.out.println("HandShake message received from " + pId + " by " + peerId);
			}
			else {
				int i = lengthOfMessage(message);
				in.read(new byte[i], 0, i);
			}
		} catch (Exception e) {
			System.out.println("\n"+Arrays.toString(e.getStackTrace()));
			// e.printStackTrace();
			System.exit(1);
		}
	}
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
		return Integer.parseInt(port+""+myPort);
	}
	public int createAddressPort(int port) {
		return Integer.parseInt(myPort+""+port);
	}
	public int lengthOfMessage(byte[] bytes) {
		ByteBuffer bb = ByteBuffer.wrap(bytes, 0, 4);
		return bb.getInt();
	}
	public int myPeersSize() {
		return myPeers.size();
	}
	public int myServersSize() {
		return myServers.length;
	}
	public ServerSocket createServer(String host, int port) throws Exception{
		ServerSocket ss = new ServerSocket();
		ss.bind(new InetSocketAddress("localhost", createAddressPort(port)));
		ss.setSoTimeout(60000);
		return ss;
	}
}