public class RequestMessage extends Message{
	public RequestMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		RequestMessage me = new RequestMessage();
		me.message = message;
		return me;
	}
}