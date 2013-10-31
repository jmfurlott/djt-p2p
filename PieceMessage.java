public class PieceMessage extends Message{
	public PieceMessage () {
	
	}
	public static Message createMessage(byte [] message) {
		PieceMessage me = new PieceMessage();
		me.message = message;
		return me;
	}
}