import java.io.DataInputStream;
import java.util.Arrays;

public class HaveMessage extends Message{
	public HaveMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		HaveMessage me = new HaveMessage();
		me.message = message;
		return me;
	}
	

	
	public static Message createMessage(int part) {
		HaveMessage me = new HaveMessage();
		me.message = new byte[9];
		byte [] length = Peer.intToByte(4);
		me.message[0] = length[0];
		me.message[1] = length[1];
		me.message[2] = length[2];
		me.message[3] = length[3];
		me.message[4] = (byte)4;
		
		byte[] partArray = Peer.intToByte(part);
		
		me.message[5] = partArray[0];
		me.message[6] = partArray[1];
		me.message[7] = partArray[2];
		me.message[8] = partArray[3];
		return me;
	}
	
	public static Message createMessage() {
		return new HaveMessage();
	}
	
	public void receiveMessage(byte[] messageLength, DataInputStream in) {
		try {
			int length = getMessageLength(messageLength);
			byte[] mess = new byte[length];
			in.read(mess, 0, length);
			
			System.out.println("Have Message Length: " + length +", Received: " + Arrays.toString(mess));
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
