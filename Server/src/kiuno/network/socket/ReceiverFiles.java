package kiuno.network.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import kiuno.function.Utility;

public class ReceiverFiles extends Thread {
	private String ip = "127.0.0.1"; //預設IP
	private int port = 51239; //預設Port
	private int backlog = 100; //在佇列端請求連線的最大長度
	private String output = ""; //檔案輸出路徑
	private int filesize = 30*1024*1024; //可接受的檔案大小，預設30M
	private ServerSocket serverSocket = null;
	/*backlog 範例說明:
		(1)int backlog = 2
		(2)一個client A連接進來，server已經accept成功
		(3)一個client B連接進來，server正在處理A，因此B被放置在佇列中
		(4)一個client C連接進來，server正在處理A，因此C被放置在佇列中
		(5)一個client D連接進來，server正在處理A，但是，佇列已經達到最大值2，因此拒絕D的連線請求
		(6)直到A處理完後，server accept B，也就是將B從佇列取出(FIFO:先進先出)，此時下一個client才能連進來
	*/
	
	public void start(){
		try{
			initial();
			checkOutput();
			serverSocket = new ServerSocket(port, backlog, InetAddress.getByName(ip));
		}catch(Exception e){
			System.out.println("ServerSocket建立有問題");
			e.printStackTrace();
		}
		
		while (true) {
			try {
				System.out.println("等待客戶端連線...");
				Socket clientSock = serverSocket.accept();
				System.out.println("收到一個客戶端連線");
				System.out.println("客戶端連線資訊:" + clientSock);
				
				Runnable runnable = () -> saveFile(clientSock);
				new Thread(runnable).start();
			} catch (IOException e) {
				System.out.println("Socket建立有問題");
				e.printStackTrace();
			}
		}
	}
	
	private void initial(){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("config.ini"));
			String line;
			while ((line=br.readLine()) != null) {
				if(line.contains("=")) parseIni(line);
			}
		}catch (IOException e) {System.out.println(e);
		}finally{ 
			if(br != null){
				try{br.close();}catch(Exception e){};
			}
		}
	}
	
	private void parseIni(String line){
		String[] str = line.split("=");
		if("ip".equals(str[0].toLowerCase()))
			this.ip = str[1];
		else if("port".equals(str[0].toLowerCase()))
			this.port = Integer.parseInt(str[1]);
		else if("backlog".equals(str[0].toLowerCase()))
			this.backlog = Integer.parseInt(str[1]);
		else if("output".equals(str[0].toLowerCase()))
			this.output = str[1];
		else if("filesize".equals(str[0].toLowerCase()))
			this.filesize = Integer.parseInt(str[1]);
	}
	
	private void checkOutput(){
		if(!"".equals(output.trim())){
			File f = new File(this.output);
			if(!f.exists()) f.mkdirs();
		}
	}
	
	private void saveFile(Socket clientSock) {
		Utility ut = new Utility();
		DataInputStream dis = null;
		FileOutputStream fos = null;
		try{
			System.out.println("接收到檔案，準備處理");
			dis = new DataInputStream(clientSock.getInputStream());
			fos = new FileOutputStream(output + "/P"+ut.getDateTime()+".jpg");
			byte[] buffer = new byte[4096];
			
			int read = 0;
			int totalRead = 0;
			int remaining = this.filesize;
			while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
				totalRead += read;
				remaining -= read;
				System.out.println("目前已讀取 " + totalRead + " bytes");
				fos.write(buffer, 0, read);
			}
		}catch(Exception e){
			System.out.println("檔案接收有問題");
			e.printStackTrace();
		}finally{
			try{
				if(fos!=null)fos.close();
				if(dis!=null)dis.close();
			}catch(Exception e){}
		}
	}
}
