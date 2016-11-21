package kiuno.network.socket;

public class ReceiverFilesExecute {

	public static void main(String[] args) {
		ReceiverFiles rf = new ReceiverFiles("127.0.0.1",51239,100);
		rf.start();
	}
}
