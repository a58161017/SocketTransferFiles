package kiuno.network.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import kiuno.function.Utility;

public class ReceiverFiles extends Thread {
	private String ip = "127.0.0.1"; //�w�]IP
	private int port = 51239; //�w�]Port
	private int backlog = 100; //�b��C�ݽШD�s�u���̤j����
	private String output = ""; //�ɮ׿�X���|
	private int filesize = 30*1024*1024; //�i�������ɮפj�p�A�w�]30M
	private ServerSocket serverSocket = null;
	/*backlog �d�һ���:
		(1)int backlog = 2
		(2)�@��client A�s���i�ӡAserver�w�gaccept���\
		(3)�@��client B�s���i�ӡAserver���b�B�zA�A�]��B�Q��m�b��C��
		(4)�@��client C�s���i�ӡAserver���b�B�zA�A�]��C�Q��m�b��C��
		(5)�@��client D�s���i�ӡAserver���b�B�zA�A���O�A��C�w�g�F��̤j��2�A�]���ڵ�D���s�u�ШD
		(6)����A�B�z����Aserver accept B�A�]�N�O�NB�q��C���X(FIFO:���i���X)�A���ɤU�@��client�~��s�i��
	*/
	
	public void start(){
		try{
			initial();
			checkOutput();
			serverSocket = new ServerSocket(port, backlog, InetAddress.getByName(ip));
		}catch(Exception e){
			System.out.println("ServerSocket�إߦ����D");
			e.printStackTrace();
		}
		
		while (true) {
			try {
				System.out.println("���ݫȤ�ݳs�u...");
				Socket clientSock = serverSocket.accept();
				System.out.println("����@�ӫȤ�ݳs�u");
				System.out.println("�Ȥ�ݳs�u��T:" + clientSock);
				
				Runnable runnable = () -> saveFile(clientSock);
				new Thread(runnable).start();
			} catch (IOException e) {
				System.out.println("Socket�إߦ����D");
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
			System.out.println("�������ɮסA�ǳƳB�z");
			dis = new DataInputStream(clientSock.getInputStream());
			fos = new FileOutputStream(output + "/P"+ut.getDateTime()+".jpg");
			byte[] buffer = new byte[4096];
			
			int read = 0;
			int totalRead = 0;
			int remaining = this.filesize;
			while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
				totalRead += read;
				remaining -= read;
				System.out.println("�ثe�wŪ�� " + totalRead + " bytes");
				fos.write(buffer, 0, read);
			}
		}catch(Exception e){
			System.out.println("�ɮױ��������D");
			e.printStackTrace();
		}finally{
			try{
				if(fos!=null)fos.close();
				if(dis!=null)dis.close();
			}catch(Exception e){}
		}
	}
}
