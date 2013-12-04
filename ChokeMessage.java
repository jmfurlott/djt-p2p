import java.io.DataInputStream;

public class ChokeMessage extends Message{
	public ChokeMessage () {
		message = new byte[5];
		message[0] = (byte)0;
		message[1] = (byte)0;
		message[2] = (byte)0;
		message[3] = (byte)0;
		message[4] = (byte)0;
	}
	public static Message createMessage(byte [] message) {
		ChokeMessage me = new ChokeMessage();
		me.message = message;
		return me;
	}
	
	public static Message createMessage() {
		return new ChokeMessage();
	}
	
	public void receiveMessage(byte[] messageLength, DataInputStream in) {
		try {
			int length = getMessageLength(messageLength);
			byte[] mess = new byte[length];
			in.read(mess, 0, length);
			
			System.out.println("Choke Message Length: " + length +", Received: " + new String(mess));
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
