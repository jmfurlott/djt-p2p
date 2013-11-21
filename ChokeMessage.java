import java.io.DataInputStream;

public class ChokeMessage extends Message{
	public ChokeMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		ChokeMessage me = new ChokeMessage();
		me.message = message;
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