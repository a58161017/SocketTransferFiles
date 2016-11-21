package kiuno.function;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
	public String getDateTime(){
		Date date = new Date( );
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddhhmmss");
		return sdf.format(date);
	}
	
	/* str  => �n�Q�ɭȪ��r��
	 * size => �r�ꤣ���Ӥj�p�N�i��ɭ� */
	public String fullyVal(String str, int size){
		return fullyVal(str,size,"0",0);
	}
	
	/* str  => �n�Q�ɭȪ��r��
	 * size => �r�ꤣ���Ӥj�p�N�i��ɭ�
	 * c    => �ɭȱĥΪ��r��
	 * type => 0�ɥ���,1�ɥk�� */
	public String fullyVal(String str, int size, String c, int type){
		if(str != null && c != null && !"".equals(c.trim())){
			if(type == 0){
				while(str.length() < size) str = c + str;
			}else if(type == 1){
				while(str.length() < size) str = str + c;
			}else{
				System.out.println("�N�X��J�����T");
			}
		}
		return str;
	}
}
