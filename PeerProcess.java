import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.*;
import java.io.*;


public class PeerProcess {
	public static void main (String args[]) {
		ArrayList<String> test = getPeerInfoConfig();
		//check for first peer directory
		
		int position = -1;
		for(int i = 0; i < test.size(); i++) {
			String temp = test.get(i);
			if(temp.charAt(temp.length() - 1) == '1') {
				System.out.println("Had file at index: " + i);
				position = i;
			} else {
				System.out.println("Did not have file at index: " + i);
			}
		
		}

		FileSplitter fs = new FileSplitter();
		String whole = test.get(position);
		String peerID = whole.split(" ")[0];
		
		try {
			boolean test2 = fs.splitFile(new File("test.txt"), 1024*1024, peerID);
			System.out.println("FILE SPLITTING: " + test2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		System.out.println("Running");
		//System.out.println(Arrays.toString(args));
		Peer peer = new Peer(args);
		while (!peer.serverFullyConnected() || !peer.socketsFullyConnected()) {
		System.out.println("Looping until fully connected");
			if (!peer.socketsFullyConnected())
				peer.connect();
			if (!peer.serverFullyConnected())
				peer.accept();
		}
		
		System.out.println("Attempt to Send Handshake to Peers");
		try {
			for (int i = 0; i < peer.myPeersSize(); i++) {
				peer.sendHandShakeToPeer(i);
			}
			
			for (int i = 0; i < peer.myInputsSize(); i++) {
				peer.receiveHandShakeFromPeer(i);
			}
		}
		catch (Exception e){
			System.out.println("\n"+Arrays.toString(e.getStackTrace()));
			// e.printStackTrace();
			System.exit(1);
		}
		
		//Eventually merge this with above loops.. but for now, keep seperate for testing purposes
		System.out.println("Simulate bitfield messages");
		try {
			for (int i = 0; i < peer.myPeersSize(); i++) {
				peer.sendMessageToPeer(i, (byte)5);
			}
			
			for (int i = 0; i < peer.myInputsSize(); i++) {
				peer.receiveMessageFromPeer(i);
			}
		}
		catch (Exception e){
			System.out.println("\n"+Arrays.toString(e.getStackTrace()));
			// e.printStackTrace();
			System.exit(1);
		}
	}
	
	//Read in the common configuration seperately for now

	public void getCommonConfiguration() {
		String st;

		try {
			BufferedReader in = new BufferedReader(new FileReader("Common.cfg"));
			while((st = in.readLine()) != null) {
				String[] tokens = st.split("\\s+");
				System.out.println(st);
			}
			
			in.close();
		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
	
	public static ArrayList<String> getPeerInfoConfig() {
		ArrayList<String> config = new ArrayList<String>();
	
		String st;
		String[] tokens = null;
		try { 
			BufferedReader in = new BufferedReader( new FileReader("PeerInfo.cfg"));
			while((st = in.readLine()) != null) {
				tokens = st.split("\\s+");
				//System.out.println(st);
				config.add(st);
			}
			
			in.close();
			return config;
		}
		catch (Exception ex) {
			System.out.println(ex.toString());
			return config;
		}
	

	
	
	}
	
	
}
