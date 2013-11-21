public class BitfieldMessage extends Message{
	public BitfieldMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		BitfieldMessage me = new BitfieldMessage();
		me.message = message;
		return me;
	}
	public void receiveMessage(byte [] messageLength, DataInputStream in) {
		int length = getMessageLength(messageLength);
		byte [] mess = new byte [length];
		in.read(mess, 0, length);
		System.out.println("Weee " + length + " Received " + new String(mess));
	}
}