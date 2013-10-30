import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.nio.ByteBuffer;

//import java.util.logging.Logger;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

/*


*/

public class Peer {
	//private final static Logger LOGGER = Logger.getLogger(Peer.class .getName());
	//Logger set up
	File file;
	FileWriter fileWritter;
	BufferedWriter bufferWritter;
	
	
	//TODO, need to have these data driven from config
	//TODO, simply need to add args to the peer constructor
	//TODONE
	private int [] peerPorts; // = { 11, 12, 13, 14, 15, 16};

	private int myPort;
	private String peerId;

	private ArrayList<Socket> myPeers;
	private ArrayList<Socket> myInputs;
	private ServerSocket [] myServers;
	private boolean [] connected;
	private boolean [] connectedServers;
	
	private int numConnectedSev;
	private HashMap<String, Integer> mapPeers = new HashMap<String, Integer>();
	public Peer (String[] args) {
		//System.out.println("Test");
		try {
			//File file = new File("logFile.txt");
			//FileWriter fileWritter = new FileWriter(file.getName(), true);
			//BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		
		
			String peerId = args[0];
			
			peerPorts = new int[args.length-1];
			for (int i = 1; i < args.length; i++) {
				peerPorts[i-1] = Integer.parseInt(args[i]);
			}
			
			myPort = Integer.parseInt(peerId);
			this.peerId = peerId+"00";
			
			myServers = new ServerSocket[5];
			
			myPeers = new ArrayList<Socket>();
			myInputs = new ArrayList<Socket>();

			numConnectedSev = 1;
			connected = new boolean [peerPorts.length];
			connectedServers = new boolean[myServers.length];
			int k = 0;
			for (int i = 0; i < peerPorts.length; i++) {
				if (peerPorts[i] == myPort) {
					connected[i] = true;
				}
				else {
					//System.out.println("ServerSocket: " + createAddressPort(peerPorts[i]));
					myServers[k] = createServer("localhost", peerPorts[i]);
					k++;
				}
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			//System.exit(1);
		}
	}
	
	public void connect() {
		//System.out.println("Test");
		try {
			for (int i = 0; i < peerPorts.length; i++) { // && !soc.isConnected(); i++) {
				//System.out.println("PortMe: " + port + " To Port: " + peerPorts[i] );
				if (!connected[i]) {
					//System.out.println("PeerPort: " + createToPort(peerPorts[i]));
					myPeers.add(new Socket("localhost", createToPort(peerPorts[i])));
					//soc.connect(new InetSocketAddress("localhost", peerPorts[i]));\=
					//System.out.println(" Worked!");
					//System.out.println("Socket: " + myPeers.get(myPeers.size()-1).toString());
					connected[i] = true;
				}

				else {
					System.out.println("Failed!");
				}
			}
		}
		catch (Exception e) {
			System.out.println("Exception on port: " + myPort);
			//if (myPeers.get(myPeers.size()-1) != null) {
			//	System.out.println("End of myPeers: " + myPeers.get(myPeers.size()-1));
			//}
			//else {
			//	System.out.println("End of myPeers: null");
			//}
			//System.out.println("\n"+Arrays.toString(e.getStackTrace()));
			//e.printStackTrace();
			//System.exit(1);
		}
	}
	
	public void accept() {
		if (numConnectedSev < peerPorts.length) { 
			try {
				for (int i = 0; i < myServers.length; i++) {
					//System.out.println("Server " + myServers[i] + " is Waiting");
					
					if (!connectedServers[i]) {
						myInputs.add(myServers[i].accept());
						//if (!myPeers.contains(p)) {
							//myPeersCli.add(p);
						numConnectedSev++;
						connectedServers[i] = true;
					}
				}
				//}
			}
			catch (Exception e) {
				e.printStackTrace();
				//System.exit(1);
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
			System.out.println(p+"");
			byte[] message = new byte[32];
			message[0] = 'H';
			message[1] = 'E';
			message[2] = 'L';
			message[3] = 'L';
			message[4] = 'O';
			DataOutputStream out = new DataOutputStream(p.getOutputStream());
			out.write(message, 0, 32);
			//The last 4 bytes hold the peerId in string form
			out.write(peerId.getBytes(), 0, 4);

			System.out.println("Sent " + new String(message) + " with pId " +peerId + " to " + myPeers.get(index));
		} catch (Exception e) {
			System.out.println("****\n"+Arrays.toString(e.getStackTrace()));
			// e.printStackTrace();
			System.exit(1);
		}
	}
	
	/* Receives the HandShake message from peer
	* 
	*
	*/
	public void receiveHandShakeFromPeer(int index) {
		try {
			Socket p =myInputs.get(index);
			byte[] message = new byte[5];
			DataInputStream in = new DataInputStream(p.getInputStream());
			System.out.println("Before Read");
			in.read(message, 0, 5);
			System.out.println("After Read");
			if ("HELLO".equals(new String(message))) {
				System.out.println("HELLO received, attempting to read rest of message");
				//bufferWritter.write("HELLO received");
				in.read(new byte[27], 0, 27);
				byte [] id = new byte[4];
				in.read(id, 0, 4);
				String pId = new String(id);
				//String pId = "11"; //hardcoded an 11, will work on this next TODO
				mapPeers.put(pId, index);
				System.out.println("HandShake message received from " + pId + " by " + peerId);
			}
			else {
				System.out.println("This is not a handshake..., attempting to read rest of message");
				int i = lengthOfMessage(message);
				in.read(new byte[i], 0, i);
			}

			//System.out.println("Handshake received and processed by peer " + index);
		} catch (Exception e) {
			System.out.println("\n"+Arrays.toString(e.getStackTrace()));
			// e.printStackTrace();
			System.exit(1);
		}
	}
	//public void sendToPeer(String message, int index) {
	//	Socket p = myPeers.get(index);
	//	DataOutputStream out = new DataOutputStream(p.getOutputStream());
	//	out.writeUTF(message);
	//}
	//public String receiveFromPeer(int index) {
	//	ServerSocket p = myServers[index];
	//	DataInputStream in = new DataInputStream(p.getInputStream());
	//	return in.readLine();
	//}
	
	public int lengthOfMessage(byte[] bytes) {
		ByteBuffer bb = ByteBuffer.wrap(bytes, 0, 4);
		return bb.getInt();
	}
	public int myPeersSize() {
		return myPeers.size();
	}
	public int myInputsSize() {
		return myInputs.size();
	}
	
	public int createToPort(int port) {
		return Integer.parseInt(port+ "" + myPort);
	}
	public int createAddressPort(int port) {
		return Integer.parseInt(myPort+ "" + port);
	}
	public ServerSocket createServer(String host, int port) throws Exception{
		ServerSocket ss = new ServerSocket();
		ss.bind(new InetSocketAddress("localhost", createAddressPort(port)));
		ss.setSoTimeout(1000);
		return ss;
	}
}
