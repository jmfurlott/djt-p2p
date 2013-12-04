import java.io.DataInputStream;
import java.util.Arrays;

public class InterestedMessage extends Message{
	public InterestedMessage () {
		message = new byte[5];
		message[0] = (byte)0;
		message[1] = (byte)0;
		message[2] = (byte)0;
		message[3] = (byte)0;
		message[4] = (byte)2;
	}
	public static Message createMessage(byte [] message) {
		InterestedMessage me = new InterestedMessage();
		me.message = message;
		return me;
	}
	
	public static Message createMessage() {
		return new InterestedMessage();
	}
	
	public void receiveMessage(byte[] messageLength, DataInputStream in) {
		try {
			int length = getMessageLength(messageLength);
			byte[] mess = new byte[length];
			in.read(mess, 0, length);
			
			System.out.println("Interested Message Length: " + length +", Received: " + Arrays.toString(mess));
		} catch (Exception e) {
			//something
		}
	}
	
	public byte [] sendMessage() {
		//do something
		
		return message;
		//return messageBody;
	
	}
}
