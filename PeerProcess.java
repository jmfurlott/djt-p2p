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
				peer.sendBitfieldMessageToPeer(i);
			}
			
			for (int i = 0; i < peer.myInputsSize(); i++) {
				peer.receiveBitfieldMessageFromPeer(i);
			}
		}
		catch (Exception e){
			System.out.println("\n"+Arrays.toString(e.getStackTrace()));
			// e.printStackTrace();
			System.exit(1);
		}
	}
	
	//Read in the common configuration seperately for now
	//TODO add to the other configuration, or establish a better naming convention
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
	
}
