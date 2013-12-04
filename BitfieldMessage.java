import java.io.DataInputStream;
import java.nio.ByteBuffer;

public class BitfieldMessage extends Message{
	public BitfieldMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		BitfieldMessage me = new BitfieldMessage();
		byte [] mess = new byte [5 + message.length];
		byte [] length = Peer.intToByte(message.length);
		mess[0] = length[0];
		mess[1] = length[1];
		mess[2] = length[2];
		mess[3] = length[3];
		mess[4] = (byte)5;
		for (int i = 0; i < message.length; i++) {
			mess[5+i] = message[i];
		}
		
		me.message = mess;
		return me;
	}
	
	public static Message createMessage() {
		return new BitfieldMessage();
	}
	
	public void receiveMessage(byte [] messageLength, DataInputStream in) {
		try {
			int length = getMessageLength(messageLength);
			byte[] mess = new byte[length];
			in.read(mess, 0, length);
			this.message = mess;
			System.out.println("Message Length: " + length +", Received: " + new String(mess));
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