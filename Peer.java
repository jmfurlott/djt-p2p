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
import java.io.PrintWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.io.FileOutputStream;
/*
Peer Class
-Instances of this class will be running on all of the different user machines
*/

public class Peer {
	//private final static Logger LOGGER = Logger.getLogger(Peer.class .getName());
	//Logger set up
	private PrintWriter logger;
	
	private int [] peerPorts;
	
	//TODO populate initially based on bitfield message
	//Maintain as HAVE messages are received
	private ArrayList<Integer> numPiecesPerPeer;
	//TODO not choking yet
	private boolean [] peerChoked;
	//TODO not interesting
	private ArrayList<Boolean> peerInterested;
	
	private int currentParts;
	
	private String fileName;

	//private int myPort;
	private String peerId;

	private ArrayList<Socket> myPeers;
	private ArrayList<Socket> myInputs;
	private ArrayList<String> myPeersIds;
	private ArrayList<String> peerInfoStrings;
	private ServerSocket [] myServers;
	private boolean [] connected;
	private boolean [] connectedServers;
	
	private int numConnectedSev;
	private HashMap<String, Integer> mapPeers = new HashMap<String, Integer>();

	public Peer (String myPeerId, String host, String fileName, ArrayList<String> pIS) {
		try {
			peerInfoStrings = pIS;
			peerId = myPeerId;
			peerPorts = new int [peerInfoStrings.size()];
			
			this.fileName = fileName;
			
			currentParts = 0;
			myServers = new ServerSocket[peerInfoStrings.size()-1];
			myPeers = new ArrayList<Socket>();
			myInputs = new ArrayList<Socket>();
			myPeersIds = new ArrayList<String>();

			numConnectedSev = 1;
			connected = new boolean[peerPorts.length];
			connectedServers = new boolean[myServers.length];
			
			numPiecesPerPeer = new ArrayList<Integer>();
			peerChoked = new boolean[peerPorts.length];
			peerInterested = new ArrayList<Boolean>();
			
			int h = 0;
			for (int i = 0; i < peerInfoStrings.size(); i++) {
				String id = peerInfoStrings.get(i).split(" ")[0];
				peerPorts[i] = Integer.parseInt(id);
				if (!id.equals(peerId)) {
					
					myServers[h] = createServer(host,Integer.parseInt(peerInfoStrings.get(i).split(" ")[0]));
					System.out.println("Server " + h + ": " + myServers[h]);
					h++;
				}
				else {
					connected[i] = true;
				}
			}
		} 
		catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			//System.exit(1);
		}
	}
	/*
	public Peer (String[] args) {
		//System.out.println("Test");
		try {
			String peerId = args[0];
			if (peerId != null) {
				String helper = "logs/log_peer_" + peerId + ".txt";

				logger = new PrintWriter(new FileWriter(helper, true)); 
				//out.println("Hello world "); 
				//out.println("Logger version 1.0"); 
				//out.close();

				//System.out.println("Log file established: " + helper);
			}
			
			peerPorts = new int[args.length-1];
			for (int i = 1; i < args.length; i++) {
				peerPorts[i-1] = Integer.parseInt(args[i]);
			}
			
			myPort = Integer.parseInt(peerId);
			this.peerId = peerId+"";
			
			myServers = new ServerSocket[5];
			
			myPeers = new ArrayList<Socket>();
			myInputs = new ArrayList<Socket>();
			myPeersIds = new ArrayList<String>();

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
	*/
	public void connect() {
		/*
			Connects all the Peer's myPeers sockets to the other 
			processes server sockets
		*/
		//System.out.println("Test");
		try {
			for (int i = 0; i < peerPorts.length; i++) { // && !soc.isConnected(); i++) {
				//System.out.println("PortMe: " + port + " To Port: " + peerPorts[i] );
				if (!connected[i]) {
					System.out.println("Host: " +peerInfoStrings.get(i).split(" ")[1] + " PeerPort: " + Integer.parseInt(peerId));
					myPeers.add(new Socket(peerInfoStrings.get(i).split(" ")[1], Integer.parseInt(peerId)));
					myPeersIds.add(peerPorts[i]+"");
					numPiecesPerPeer.add(0);
					peerInterested.add(false);
					//soc.connect(new InetSocketAddress("localhost", peerPorts[i]));\=
					//System.out.println(" Worked!");
					//System.out.println("Socket: " + myPeers.get(myPeers.size()-1).toString());
					connected[i] = true;
					
					//TODO need to convert this to a logger event
					String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());
					System.out.println("[" + date + "]: Peer [" + peerId + "] makes a connection to Peer [" + peerPorts[i] + "]");
				}

				else {
					//System.out.println("Failed!");
				}
			}
		}
		catch (Exception e) {
			System.out.println("Exception on Peer: " + peerId);
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
		/*
			Accepts the connection to each of the Peer's ServerSockets
			from the other processes Sockets
		*/
		if (numConnectedSev < peerPorts.length) { 
			try {
				for (int i = 0; i < myServers.length; i++) {
					//System.out.println("Server " + myServers[i] + " is Waiting");
					
					if (!connectedServers[i]) {
						myInputs.add(myServers[i].accept());
						
						//logger.println("[Time]: Peer [" + peerId + "] makes a connection to Peer [blank]");
						//logger.println("TEST");

						String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());
						
						//TODO convert these into a logger event
						
						System.out.println("[" + date + "]: Peer [" + peerPorts[i] + "] is connected from Peer [" + peerId + "]");
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
		/* 
			Checks to see if the ServerSockets have all been connected too
		*/
		if (numConnectedSev < peerPorts.length) {
			return false;
		}
		return true;
	}
	
	public boolean socketsFullyConnected() {
		/* 
			Checks to see if the Sockets have all been connected to a ServerSocket
		*/
		for (int i = 0; i < connected.length; i++) {
			if (!connected[i]) {
				return false;
			}
		}
		return true;
	}
	public void sendHandShakeToPeer(int index) {
		/* 
			Sends a Hand Shake message to one of it's Peers
		*/
		try {
			//System.out.println("Trying to send handshake");
			Socket p = myPeers.get(index);
			//System.out.println(p+"");
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

			//System.out.println("Sent " + new String(message) + " with pId " +peerId + " to " + myPeers.get(index));
			String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());			
			//TODO convert these into a logger event		
			System.out.println("[" + date + "]: Peer [" + peerPorts[index] + "] sent handshake message to Peer [" + peerId + "]");
			
		} catch (Exception e) {
			System.out.println("\n"+Arrays.toString(e.getStackTrace()));
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
			//System.out.println("Before Read Handshake");
			in.read(message, 0, 5);
			//System.out.println("After Read Handshake");
			if ("HELLO".equals(new String(message))) {
				//System.out.println("HELLO received, attempting to read rest of message");
				//bufferWritter.write("HELLO received");
				in.read(new byte[27], 0, 27);
				byte [] id = new byte[4];
				in.read(id, 0, 4);
				String pId = new String(id);
				//String pId = "11"; //hardcoded an 11, will work on this next TODO
				mapPeers.put(pId, index);
				//System.out.println("HandShake message received from " + pId + " by " + peerId);
				String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());			
				//TODO convert these into a logger event		
				System.out.println("[" + date + "]: Peer [" + peerPorts[index] + "] received handshake message from Peer [" + peerId + "]");
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
	public void sendMessageToPeer(int index, byte type) {
		try {
			Socket p = myPeers.get(index);
			byte[] messageLength = new byte[4];
			byte[] messageType = new byte[1];
			messageType[0] = type;
			Message m = createMessage(messageType[0]);
			byte [] messageBody = m.sendMessage();

			messageLength = intToByte(messageBody.length);
			
			DataOutputStream out = new DataOutputStream(p.getOutputStream());
			//System.out.println("Before Reading Bitfield Message");
			out.write(messageLength, 0, 4);
			out.write(messageType, 0, 1);
			out.write(messageBody, 0, messageBody.length);
			
			
			
		} catch (Exception e) {
			System.out.println("\n"+Arrays.toString(e.getStackTrace()));
			// e.printStackTrace();
			//System.exit(1);
		}
	}
	
	public void sendMessageToPeer(int index, Message m) {
		try {
			Socket p = myPeers.get(index);
			byte [] mess = m.getMessage(true);
			System.out.println("Sending Message");
			DataOutputStream out = new DataOutputStream(p.getOutputStream());
			//System.out.println("Before Reading Bitfield Message");
			out.write(mess, out.size(), mess.length);
			
			String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());			
			System.out.println("[" + date + "]: Peer [" + peerId+ "] sent message of type " + messageType(mess) + " to Peer [" +  myPeersIds.get(index) + "]");
			System.out.println("Message: " + Arrays.toString(m.getMessage(true)));
			System.out.println("End Send Message\n");

			//if (messageType(m.getMessage(true)) == 5) {
				//receive InterestedMessage
				//System.out.println("Waiting for InterestedMessage");
				//receiveMessageFromPeer(index);
				
			//}
			
			
			//TODO convert these into a logger event		
			
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
			System.out.println("\n"+Arrays.toString(e.getStackTrace()));
			
			// e.printStackTrace();
			//System.exit(1);
		}
	}
	
	public void receiveMessageFromPeer(int index) {
		try {
			Socket p =myInputs.get(index);
			byte[] messageLength = new byte[4];
			byte[] messageType = new byte[1];
			//System.out.println("Before receive");
			DataInputStream in = new DataInputStream(p.getInputStream());
			//System.out.println("Before Reading Bitfield Message");
			//Scanner hasNextCheck = new Scanner(in);
			//if (!hasNextCheck.hasNext()) {
				//System.out.println("No input found");
				//return;
			//}
			Thread.sleep(100);
			if (in.available() == 0) {
				//System.out.println("No input found");
				return;
			}
			in.read(messageLength, 0, 4);
			in.read(messageType, 0, 1);
			
			Message m = createMessage(messageType[0]);
			m.receiveMessage(messageLength, in);
			int length = lengthOfMessage(messageLength);
			
			String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());			
				//TODO convert these into a logger event		
				System.out.println("[" + date + "]: Peer [" + myPeersIds.get(index) + "] received message of type " + (int)messageType[0]+ " to Peer [" + peerId + "]");
			System.out.println("Message: " + Arrays.toString(m.getMessage(true)));
			System.out.println("NumPiecesPerPeer: " + numPiecesPerPeer.toString()+"\n");
			
			if ((int)messageType[0] == 5) {
				ByteBuffer bb = ByteBuffer.wrap(m.getMessage(true), 0, length);
				int numParts = bb.getInt();
				numPiecesPerPeer.set(index, numParts);
				
				if (currentParts < numParts) {
					sendMessageToPeer(index, new InterestedMessage());
					peerInterested.set(index, true);
					
					System.out.println("\nSending RequestMessage");
					Message request = RequestMessage.createMessage(intToByte(currentParts));
					sendMessageToPeer(index, request);
					
				}
				else {
					sendMessageToPeer(index, new NotInterestedMessage());
					peerInterested.set(index, false);
				}

			}
			
			else if ((int)messageType[0] == 2) {
				System.out.println("Received Interested Message");
				//receiveMessageFromPeer(index);
			}
			
			else if ((int)messageType[0] == 6) {
				byte [] request = m.getMessage(true);
				int pieceNum = byteToInt(request);
				File piece = getFile(pieceNum);
				Message pieceMessage = PieceMessage.createMessage(piece, pieceNum);
				sendMessageToPeer(index, pieceMessage);
			}
			
			else if ((int)messageType[0] == 7) {
				System.out.println("PIECE!!!!!");
				File pFile = new File(fileName+currentParts);
				String dirname = "null/" + peerId + "/";
				File newDir = new File(dirname);
				newDir.mkdirs();
				
				
				pFile.createNewFile();
				
				FileOutputStream fileOut = new FileOutputStream(pFile);
				fileOut.write(m.getMessage(true), 4, length-4);
				fileOut.close();
				
				currentParts++;
				
				//now that we have the file piece, send out have message
				for(int i = 0; i < myPeers.size(); i++) {
					//System.out.println("Sent Have Message for part: " + currentParts-1 + " 
					Message haveMessage = HaveMessage.createMessage(currentParts);
					
					sendMessageToPeer(i, haveMessage);
				}
			}
			
			else if ((int)messageType[0] == 4) {
				int pieceNum = byteToInt(m.getMessage(true));
				numPiecesPerPeer.set(index, pieceNum);
				System.out.println("Updated NumPiecesPerPeer: " + numPiecesPerPeer);
			}
			
			
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
			System.out.println("\n"+Arrays.toString(e.getStackTrace()));
			// e.printStackTrace();
			//System.exit(1);
		}
	}
	//Currently setting an empty bitfield message
	/* public void sendBitfieldMessageToPeer(int index) {
		try {
			//System.out.println("Trying to send bitfield message");
			Socket p = myPeers.get(index);
			//System.out.println(p+"");
			byte[] message = new byte[5];
			
			//TODO currently hardcoding in a message size
			//simply to identify who is sending the message
			//in the future, this needs to be defined based on
			//the common configuration data & the peers current
			//file piece status
			message[0] = (byte) index;
			message[1] = (byte) index;
			message[2] = (byte) index;
			message[3] = (byte) index;
			message[4] = '5';
			DataOutputStream out = new DataOutputStream(p.getOutputStream());
			out.write(message, 0, 5);
			//The last 4 bytes hold the peerId in string form
			out.write(peerId.getBytes(), 0, 4);
			String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());			
			//TODO convert these into a logger event		
			System.out.println("[" + date + "]: Peer [" + peerPorts[index] + "] sent bitfield message to Peer [" + peerId + "]");
		
			//System.out.println("Sent " + new String(message) + " with pId " +peerId + " to " + myPeers.get(index));
		} catch (Exception e) {
			
			System.out.println("****\n"+Arrays.toString(e.getStackTrace()));
			// e.printStackTrace();
			//System.exit(1);
		}
	}
	*/
	/* Receives the Bitfield message from peer
	*  Note: should only be received one time per peer
	*
	*/
	public void receiveBitfieldMessageFromPeer(int index) {
		int helper[] = new int[4];
		int messagePayloadSize = 0;
		int pow;
	
		try {
			Socket p =myInputs.get(index);
			byte[] messageLength = new byte[4];
			byte[] messageType = new byte[1];
			DataInputStream in = new DataInputStream(p.getInputStream());
			//System.out.println("Before Reading Bitfield Message");
			in.read(messageLength, 0, 4);
			in.read(messageType, 0, 1);
			//System.out.println("After Reading Bitfield Message");
			
			if ("5".equals(new String(messageType))) {
				//System.out.println("Confirmation, bitfield message received");
				
				helper[0] = messageLength[0];
				helper[1] = messageLength[1];
				helper[2] = messageLength[2];
				helper[3] = messageLength[3];
				
				for (int i = 0; i < 4; i++) {
					pow = 1;
				
					for (int j = 3; j > i; j--) {
						pow = pow * 256;
					}
				
					messagePayloadSize += helper[i]*pow;
				}
				String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());			
				//TODO convert these into a logger event		
				System.out.println("[" + date + "]: Peer [" + peerPorts[index] + "] received bitfield message to Peer [" + peerId + "]");
				
				//System.out.println("Message payload size = " + messagePayloadSize);
				//System.out.println(" " + helper[0] + " " + helper[1] + " " + helper[2] + " " + helper[3]);	
				
			}
			
			else {
				System.out.println("Not a bitfield message");
			}

		} catch (Exception e) {
			System.out.println("\n"+Arrays.toString(e.getStackTrace()));
			// e.printStackTrace();
			//System.exit(1);
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
	
	public String getPeerId() {
		return peerId;
	}
	
	public int messageType(byte[] bytes) {
		return (int)bytes[4];
	}
	
	public int lengthOfMessage(byte[] bytes) {
		/* 
			returns the length message sent by looking at the first 4 bytes
		*/
		ByteBuffer bb = ByteBuffer.wrap(bytes, 0, 4);
		return bb.getInt();
	}
	
	public static int byteToInt(byte [] bytes) {
		ByteBuffer bb = ByteBuffer.wrap(bytes, 0, 4);
		return bb.getInt();
	}
	
	public static byte [] intToByte (int length) {
		return ByteBuffer.allocate(4).putInt(length).array();
	}
	
	public File getFile(int pieceNum) {
		File file = new File(fileName+pieceNum);
		return file;
	}
	
	public int myPeersSize() {
		/* 
			returns how many Peers this Peer has
		*/
		return myPeers.size();
	}
	public int myInputsSize() {
		/* 
			returns how many Sockets this Peer will be receiving input from
		*/
		return myInputs.size();
	}
	
	public void setCurrentParts(int a) {
		currentParts = a;
	}
	
	public boolean getPeerInterested(int i) {
		return peerInterested.get(i);
	}
	
	/* public int createToPort(int port) {
		
			creates a unique port so the Socket can be identified easily
		
		return Integer.parseInt(port+ "" + myPort);
	} */
	/*public int createAddressPort(int port) {
		
			creates a unique port so the ServerSocket can be identified easily
	
		return Integer.parseInt(myPort+ "" + port);
	} */
	public ServerSocket createServer(String host, int port) throws Exception{
		/* 
			creates a ServerSocket with a timeout of 1 second
		*/
		ServerSocket ss = new ServerSocket();
		ss.bind(new InetSocketAddress(host, port));
		ss.setSoTimeout(1000);
		return ss;
	}
	
	
	
	public Message createMessage(byte message) {
		int type = Message.getTypeOfMessage(message);
		switch (type) {
			case 0: return ChokeMessage.createMessage();
					
			case 1: return UnchokeMessage.createMessage();
					
			case 2: return InterestedMessage.createMessage();
					
			case 3: return NotInterestedMessage.createMessage();
					
			case 4: return HaveMessage.createMessage();
					
			case 5: return BitfieldMessage.createMessage();
					
			case 6: return RequestMessage.createMessage();

			case 7: return PieceMessage.createMessage();
					
			default: return null;
					
		}
	}
}
