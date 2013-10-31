public class BitfieldMessage extends Message{
	public BitfieldMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		BitfieldMessage me = new BitfieldMessage();
		me.message = message;
		return me;
	}
}