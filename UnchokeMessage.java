import java.io.DataInputStream;

public class UnchokeMessage extends Message{
	public UnchokeMessage () {
		message = new byte[5];
		message[0] = (byte)0;
		message[1] = (byte)0;
		message[2] = (byte)0;
		message[3] = (byte)0;
		message[4] = (byte)1;
	}
	public static Message createMessage(byte [] message) {
		UnchokeMessage me = new UnchokeMessage();
		me.message = message;
		return me;
	}
	
	public static Message createMessage() {
		return new UnchokeMessage();
	}
	
	public void receiveMessage(byte[] messageLength, DataInputStream in) {
		try {
			int length = getMessageLength(messageLength);
			byte[] mess = new byte[length];
			in.read(mess, 0, length);
			
			System.out.println("Unchoke Message Length: " + length +", Received: " + new String(mess));
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
