import java.io.DataInputStream;

public class HaveMessage extends Message{
	public HaveMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		HaveMessage me = new HaveMessage();
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