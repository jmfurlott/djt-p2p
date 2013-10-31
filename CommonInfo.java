
/*	
*	Holds all the common information about the P2P system
*	Loaded through the Common.cfg file
*	Not sure if this is overkill or not
*	Went ahead and coded it up
*/

public class CommonInfo {
	public int numberOfPreferredNeighbors;
	public int unchokingInterval;
	public int optimisticUnchokingInterval;
	public String filename;
	public int fileSize;
	public int pieceSize;
	
	public CommonInfo(int numNeigh, int unchoke, int optimistic, String fname, int fsize, int psize) {
		numberOfPreferredNeighbors = numNeigh;
		unchokingInterval = unchoke;
		optimisticUnchokingInterval = optimistic;
		filename = fname;
		fileSize = fsize;
		pieceSize = psize;
	}
}
