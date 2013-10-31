public class NotInterestedMessage extends Message{
	public NotInterestedMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		NotInterestedMessage me = new NotInterestedMessage();
		me.message = message;
		return me;
	}
}