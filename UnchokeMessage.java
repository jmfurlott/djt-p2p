public class UnchokeMessage extends Message{
	public UnchokeMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		UnchokeMessage me = new UnchokeMessage();
		me.message = message;
		return me;
	}
}