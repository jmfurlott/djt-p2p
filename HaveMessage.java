public class HaveMessage extends Message{
	public HaveMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		HaveMessage me = new HaveMessage();
		me.message = message;
		return me;
	}
}