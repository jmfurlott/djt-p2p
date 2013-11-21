import java.io.DataInputStream;

public class RequestMessage extends Message{
	public RequestMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		RequestMessage me = new RequestMessage();
		me.message = message;
		return me;
	}
	
	public static Message createMessage() {
		return new RequestMessage();
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