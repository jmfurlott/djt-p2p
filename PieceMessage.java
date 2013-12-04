import java.io.DataInputStream;
import java.util.Arrays;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class PieceMessage extends Message{
	public PieceMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		PieceMessage me = new PieceMessage();
		me.message = message;
		return me;
	}
	
	public static Message createMessage() {
		return new PieceMessage();
	}
	
	public static Message createMessage(File file, int pieceNum) {
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		
			int len = in.available();
			byte [] data = new byte[len+9];
			byte [] length = Peer.intToByte(len+4);
			data[0] = length[0];
			data[1] = length[1];
			data[2] = length[2];
			data[3] = length[3];
			data[4] = (byte)7;
		
			byte [] piece = Peer.intToByte(pieceNum);
			data[5] = piece[0];
			data[6] = piece[1];
			data[7] = piece[2];
			data[8] = piece[3];
		
			in.read(data, 9, len);
		
			PieceMessage me = new PieceMessage();
			me.message = data;
			return me;
		} catch (Exception e){
			System.out.println("Error: " + e.toString());
			System.out.println("\n"+Arrays.toString(e.getStackTrace()));
			return null;
		}
	}
	
	public void receiveMessage(byte[] messageLength, DataInputStream in) {
		try {
			int length = getMessageLength(messageLength);
			byte[] mess = new byte[length];
			in.read(mess, 0, length);
			
			System.out.println("Piece Message Length: " + length +", Received: You don't want to see this");//Arrays.toString(mess));
			message = mess;
		} catch (Exception e) {
			//something
		}
	}
	
	public byte [] sendMessage() {
		//do something
		return "BitFieldMessage".getBytes();
		//return messageBody;
	
	}
}
