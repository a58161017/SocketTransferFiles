package kiuno.function;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
	public String getDateTime(){
		Date date = new Date( );
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddhhmmss");
		return sdf.format(date);
	}
	
	/* str  => 璶砆干﹃
	 * size => ﹃ぃì赣碞秈︽干 */
	public String fullyVal(String str, int size){
		return fullyVal(str,size,"0",0);
	}
	
	/* str  => 璶砆干﹃
	 * size => ﹃ぃì赣碞秈︽干
	 * c    => 干蹦ノじ
	 * type => 0干オ娩,1干娩 */
	public String fullyVal(String str, int size, String c, int type){
		if(str != null && c != null && !"".equals(c.trim())){
			if(type == 0){
				while(str.length() < size) str = c + str;
			}else if(type == 1){
				while(str.length() < size) str = str + c;
			}else{
				System.out.println("絏块ぃタ絋");
			}
		}
		return str;
	}
}
