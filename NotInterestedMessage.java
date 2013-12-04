import java.io.DataInputStream;
import java.util.Arrays;

public class NotInterestedMessage extends Message{
	public NotInterestedMessage () {
		message = new byte[5];
		message[0] = (byte)0;
		message[1] = (byte)0;
		message[2] = (byte)0;
		message[3] = (byte)0;
		message[4] = (byte)3;
	}
	public static Message createMessage(byte [] message) {
		NotInterestedMessage me = new NotInterestedMessage();
		me.message = message;
		return me;
	}
	
	public static Message createMessage() {
		return new NotInterestedMessage();
	}
	
	public void receiveMessage(byte[] messageLength, DataInputStream in) {
		try {
			int length = getMessageLength(messageLength);
			byte[] mess = new byte[length];
			in.read(mess, 0, length);
			
			System.out.println("Not Interested Message Length: " + length +", Received: " + Arrays.toString(mess));
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
