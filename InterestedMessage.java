public class InterestedMessage extends Message{
	public InterestedMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		InterestedMessage me = new InterestedMessage();
		me.message = message;
		return me;
	}
	
	public void receiveMessage(byte[] messageLength, DataInputStream in) {
		int length = getMessageLength(messageLength);
		byte[] mess = new byte[length];
		in.read(mess, 0, length);
		
		System.out.println("Message Length: " + length +", Received: " + new String[mess]);
	}
}