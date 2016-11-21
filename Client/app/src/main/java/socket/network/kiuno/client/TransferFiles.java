package socket.network.kiuno.client;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class TransferFiles {
	String ip = "127.0.0.1"; //�w�]IP
	int port = 51239; //�w�]Port
	String filePath = "";
	private Socket socket = null;
	TransferFiles(String ip, int port){
		this.ip = ip;
		this.port = port;
	}
	
	public void start(String filePath) {
		this.filePath = filePath;
		new SocketTask().execute("");
	}
	
	public void transferFile() throws IOException {
		File file = new File(filePath);
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		byte[] buffer = new byte[4096];
		int count = 0;
		
		while (fis.read(buffer) > 0) {
			count++;
			System.out.println("�ǰe�ɮ׫ʥ]��"+count+"��");
			dos.write(buffer);
		}
		file.delete();
		fis.close();
		dos.close();	
		socket.close();
	}


	private class SocketTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			try{
				socket = new Socket(ip, port);
			}catch(Exception e){
				System.out.println("Socket�إߦ����D");
				e.printStackTrace();
			}
			try{
				System.out.println("sokect�إߧ����A�ǳƶǿ��ɮ�");
				transferFile();
			}catch(Exception e){
				System.out.println("�ɮ׶ǿ馳���D");
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}
}
