import java.nio.ByteBuffer;

public abstract class Message {
	protected byte [] message;
	
	public int getMessageLength() {
		/* 
			returns the length message sent by looking at the first 4 bytes
		*/
		ByteBuffer bb = ByteBuffer.wrap(message, 0, 4);
		return bb.getInt();
	}
	
	public void setMessageLength(int length) {
		/* 
			Sets message length, e.g., the first four bytes to the inputed int
		*/
		ByteBuffer bb = ByteBuffer.wrap(message);
		bb.putInt(0, length);
		message = bb.array();
	}
	
	public ByteBuffer getMessage() {
		/* 
			Gets the actual message
		*/
		int length = getMessageLength();
		return ByteBuffer.wrap(message, 4, length);
	}
	
	public byte getType() {
		/*
			returns the 5th byte which represents the type of message
		*/
		return message[4];
	}
	
	public static int getTypeOfMessage(byte [] message) {
		return new Integer(message[4]);
	}
	
	public static Message createMessage(byte [] message) {
		return null;
	}
	
	
}