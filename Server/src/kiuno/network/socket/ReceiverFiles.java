package kiuno.network.socket;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import kiuno.function.Utility;

public class ReceiverFiles extends Thread {
	private String ip = "127.0.0.1"; //�w�]IP
	private int port = 51239; //�w�]Port
	private int backlog = 100; //�b��C�ݽШD�s�u���̤j����
	private ServerSocket serverSocket = null;
	/*backlog �d�һ���:
		(1)int backlog = 2
		(2)�@��client A�s���i�ӡAserver�w�gaccept���\
		(3)�@��client B�s���i�ӡAserver���b�B�zA�A�]��B�Q��m�b��C��
		(4)�@��client C�s���i�ӡAserver���b�B�zA�A�]��C�Q��m�b��C��
		(5)�@��client D�s���i�ӡAserver���b�B�zA�A���O�A��C�w�g�F��̤j��2�A�]���ڵ�D���s�u�ШD
		(6)����A�B�z����Aserver accept B�A�]�N�O�NB�q��C���X(FIFO:���i���X)�A���ɤU�@��client�~��s�i��
	*/
	ReceiverFiles(String ip, int port, int backlog){
		this.ip = ip;
		this.port = port;
		this.backlog = backlog;
	}
	
	public void start(){
		try{
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
	
	private void saveFile(Socket clientSock) {
		Utility ut = new Utility();
		DataInputStream dis = null;
		FileOutputStream fos = null;
		try{
			System.out.println("�������ɮסA�ǳƳB�z");
			dis = new DataInputStream(clientSock.getInputStream());
			fos = new FileOutputStream("D:/FTP/kiuno/AppFiles/P"+ut.getDateTime()+".jpg");
			byte[] buffer = new byte[4096];
			
			int filesize = 30*1024*1024; //�i�������ɮפj�p�A�w�]30M
			int read = 0;
			int totalRead = 0;
			int remaining = filesize;
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
