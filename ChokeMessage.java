public class ChokeMessage extends Message{
	public ChokeMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		ChokeMessage me = new ChokeMessage();
		me.message = message;
		return me;
	}
}