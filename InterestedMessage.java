public class InterestedMessage extends Message{
	public InterestedMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		InterestedMessage me = new InterestedMessage();
		me.message = message;
		return me;
	}
}