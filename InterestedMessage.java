import java.io.DataInputStream;

public class InterestedMessage extends Message{
	public InterestedMessage () {
	
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