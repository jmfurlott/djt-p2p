import java.io.DataInputStream;
import java.util.Arrays;

public class RequestMessage extends Message{
	public RequestMessage () {
		
	}
	public static Message createMessage(byte [] input) {
		RequestMessage me = new RequestMessage();
		me.message = new byte[9];
		byte [] length = Peer.intToByte(4);
		me.message[0] = length[0];
		me.message[1] = length[1];
		me.message[2] = length[2];
		me.message[3] = length[3];
		me.message[4] = (byte)6;
		
		me.message[5] = input[0];
		me.message[6] = input[1];
		me.message[7] = input[2];
		me.message[8] = input[3];

		return me;
	}
	
	public static Message createMessage() {
		return new RequestMessage();
	}
	
	public void receiveMessage(byte[] messageLength, DataInputStream in) {
		try {
			int length = getMessageLength(messageLength);
			byte[] mess = new byte[length];
			in.read(mess, 0, length);
			this.message = mess;
			
			System.out.println("Request Message Length: " + length +", Received: " + Arrays.toString(mess));
		} catch (Exception e) {
			//something
		}
	}
	
	public byte [] sendMessage() {
		//do something
		return "".getBytes();
		//return messageBody;
	
	}
}
