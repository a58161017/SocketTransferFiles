package socket.network.kiuno.client;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class TransferFiles {
	String ip = "127.0.0.1"; //預設IP
	int port = 51239; //預設Port
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
			System.out.println("傳送檔案封包第"+count+"次");
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
				System.out.println("Socket建立有問題");
				e.printStackTrace();
			}
			try{
				System.out.println("sokect建立完成，準備傳輸檔案");
				transferFile();
			}catch(Exception e){
				System.out.println("檔案傳輸有問題");
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
