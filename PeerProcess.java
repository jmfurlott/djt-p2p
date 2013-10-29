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
		while (!peer.serverFullyConnected() && !peer.socketsFullyConnected()) {
		//System.out.println("TY");
			if (!peer.socketsFullyConnected())
				peer.connect();
			if (!peer.serverFullyConnected())
				peer.accept();
		}
		System.out.println("Your mother");
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
	}
}