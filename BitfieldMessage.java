import java.io.DataInputStream;
import java.nio.ByteBuffer;

public class BitfieldMessage extends Message{
	public BitfieldMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		BitfieldMessage me = new BitfieldMessage();
		me.message = message;
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