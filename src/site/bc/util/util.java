package site.bc.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import org.json.JSONTokener;


/**
 * 
 * @author Administrator
 */

public class util {
	
	protected final static String[] HEXCHARS="0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F".split(",");
	public static final class Arrays{

		public static String last(String[] ss, boolean allocempty) {
			int i=ss==null?-1:ss.length-1;
			while(i>=0) {
				if("".equalsIgnoreCase(ss[i])&&(!allocempty)) {
					i--;
					continue;
				}
				return ss[i];
			}
			return null;
		}
		
	}
	public static int checksum(byte[] data, int spos, int epos) {
		int r=0;
		for(int i=spos;i<epos;i++) {
			r+=0xff&data[i];
		}
		return r&0xff;
	}

	public static Date parsetime(String tm) {
		SimpleDateFormat utcFormater = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");//20180412T034306Z
	    utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			Date r = utcFormater.parse(tm);
			return r;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String tohex(byte[] data) {
		if(data==null) {
			return "";
		}
		StringBuffer sb=new StringBuffer();
		for(byte b:data) {
			sb.append(tohex(b));
		}
		return sb.toString();
	}

	public static String tohex(byte b) {
		int iv=b;
		if(iv<0) {
			iv+=256;
		}
		return HEXCHARS[iv>>4]+HEXCHARS[iv&0x0f];
	}
	public static int toint(byte v) {
		int iv=v;
		if(iv<0) {
			iv+=256;
		}
		return iv;
	}
	public static String tohex(int v) {
		int iv=v;
		if(iv<0) {
			iv+=256;
		}
		return HEXCHARS[(iv>>4)]+HEXCHARS[iv&0x0f];
	}
	public static String tohex(byte[] data, int offset, int len, boolean revert) {
		if(data==null) {
			return "";
		}
		StringBuffer sb=new StringBuffer();
		if(revert) {
			for(int i=offset+len-1;i>=offset;i--) {
				sb.append(tohex(data[i]));
			}
		}else {
			for(int i=offset;i<offset+len;i++) {
				sb.append(tohex(data[i]));
			}
		}
		return sb.toString();
	}
	public static String tohex33(byte[] data, int offset, int len, boolean revert) {
		if(data==null) {
			return "";
		}
		StringBuffer sb=new StringBuffer();
		if(revert) {
			for(int i=offset+len-1;i>=offset;i--) {
				sb.append(tohex(data[i]-((byte)0x33)));
			}
		}else {
			for(int i=offset;i<offset+len;i++) {
				sb.append(tohex(data[i]-((byte)0x33)));
			}
		}
		return sb.toString();
	}

	public static double trim(double t, int i) {
		int p=(int)Math.pow(10,i);
		return ((double)Math.round(t*p))/p;
	}

	public static byte[] fromhex(String s) {
		int b=-1;
		int rl=0;
		byte[] r=new byte[s.length()];
		for(int i=0;i<s.length();i++) {
			int c=s.charAt(i);
			if('0'<=c&&c<='9') {
				c-='0';
			}else if('a'<=c&&c<='f') {
				c-='a';
				c+=10;
			}else if('A'<=c&&c<='F') {
				c-='A';
				c+=10;
			}else if(b<0){
				continue;
			}
			if(b<0) {
				b=c;
			}else{
				r[rl]=(byte) ((b<<4)+c);
				rl++;
				b=-1;
			}
		}
		return java.util.Arrays.copyOf(r, rl);
	}

	public static byte[] revert(byte[] buf) {
		int halfl=buf.length/2;
		for(int i=0;i<halfl;i++) {
			byte b=buf[i];
			buf[i]=buf[buf.length-1-i];
			buf[buf.length-1-i]=b;
		}
		return buf;
	}

	public static boolean[] tobin(int f, int c) {
		if(c<1) {
			return new boolean[0];
		}
		int n=f;
		boolean[] r=new boolean[c];
		for(int i=0;i<c;i++) {
			r[i]=(n&1)==1;
			n>>=1;
		}
		return r;
	}
	public static boolean[] tobin(byte[] data) {
		if(data==null) {
			return new boolean[0];
		}
		boolean[] r=new boolean[data.length*8];
		
		for(int i=0;i<data.length;i++) {
			int n=(256+data[i])%256;
			for(int j=0;j<8;j++) {
				r[i*8+j]=(n&1)==1;
				n>>=1;
			}
		}
		return r;
	}
	public static int toint(boolean[] b) {
		int r=0;
		for(int i=b.length-1;i>=0;i--) {
			r=r<<1;
			r+=b[i]?1:0;
		}
		return r;
	}

	public static long multiply_long(long...ls) {
		long r=1;
		for(long l0:ls) {
			r*=l0;
		}
		return r;
	}

	public static String tryutf8string(String s) {
		if(s!=null) {
			try {
				ByteBuffer bb = ByteBuffer.wrap(s.getBytes("iso-8859-1"));
				String s1=""+java.nio.charset.Charset.forName("iso-8859-1").decode(bb);
				if(java.util.Arrays.equals(s1.getBytes(),s.getBytes())) {
					return new String(s.getBytes("iso-8859-1"),"utf-8");
				}
			} catch (Exception e) {
			}
		}
		return s;
	}

	public static void fillbcd(int v, byte[] r, int offset, int len) {
		try {
		int t=v;
		for(int i=0;i<len;i++) {
			int b=t%100;
			t/=100;
			r[offset+i]=(byte)(((b/10)<<4)+b%10);
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static int tryToInt(Object v, int defaultvalue) {
		if(null!=v) {
			if(v instanceof Number) {
				return ((Number)v).intValue();
			}
			if(v instanceof String) {
				int r=0;
				String sv = (String)v;
				String[] ssv=sv.split("\\D");
				for(int i=0;i<ssv.length;i++) {
					String sv0=ssv[i];
					if(sv0.length()<3) {
						r*=100;
					}else if(sv0.length()<5) {
						r*=10000;
					}else if(sv0.length()<7) {
						r*=1000000;
					}else{
						r*=1000000;
					}
					try{
						r+=Integer.parseInt(sv0);
					}catch(Exception e1) {
						
					}
					
				}
				return r;
			}
		}
		return defaultvalue;
	}

	public static boolean strIn(boolean nocase, String str, String...strs) {
		if(null==str) {
			return false;
		}
		if(nocase) {
			for(String s:strs) {
				if((""+str).equalsIgnoreCase(s)) {
					return true;
				}
			}
		}else {
			for(String s:strs) {
				if((""+str).equals(s)) {
					return true;
				}
			}	
		}
		return false;
	}
	public static String formatdate(String sdate) {
		if(sdate==null) {
			return null;
		}
		Date d=util.parsetime(sdate);
		return formatdate(d);
	}
	private static DateFormat _df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat utcFormater = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");//20180412T034306Z
	
	public static String formatdate(Date date) {
		if(date==null) {
			return null;
		}
		return _df.format(date);
	}
	public static String formatUTCdate(Object date) {
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			return utcFormater.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String formatdate(long date) {
		return _df.format(date);
	}
	public static String formatdate() {
		return formatdate(System.currentTimeMillis());
	}
	public static String formatfloat(double v, int dotnum) {
		double q=Math.pow(10,dotnum);
		double nv=Math.round(v*q);
		return ""+(nv/(long)q);
	}

	public static String setLength(String s, int l) {
		String r=s==null?"":s;
		while(r.length()<l) {
			r="0"+r;
		}
		return r.substring(r.length()-l);
	}

	public static int ubyte(byte b) {
		return 0xff&b;
	}

	public static int toHexInt(byte[] b, int offset, int len, int bytesub,
			boolean revert) {
		int r = 0;
		int i, c, l;
		l = offset + len;
		if (!revert) {
			for (i = offset; i < l; i++) {
				c = (b[i] - bytesub + 0x200) & 0xff;
				r = (r << 8) + c;
			}
		} else {
			for (i = offset + len - 1; i >= offset; i--) {
				c = (b[i] - bytesub + 0x200) & 0xff;
				r = (r << 8) + c;
			}
		}
		return r;
	}

	public static String setLengthr(String s, int l) {
		String r=s==null?"":s;
		while(r.length()<l) {
			r=r+"0";
		}
		return r.substring(r.length()-l);
	}
	public static void close(Object...objs) {
		for(Object o:objs) {
			if(o!=null) {
				Class<? extends Object> cls = o.getClass();
				try {
					for(Method m:cls.getDeclaredMethods()) {
						if("close".equalsIgnoreCase(m.getName())) {
							m.invoke(o);
							break;
						}
					}
				}catch(Exception e) {
					
				}
			}
		}
	}
	public static String len(String s,int l) {
		return len(s,"0",l,true);
	}
	public static String len(String s,String fill,int l,boolean headfill) {
		String r=s==null?"":s;
		if(fill==null) {
			return null;
		}else if(fill.isEmpty()) {
			return null;
		}
		while(r.length()<l) {
			if(headfill) {
				r=""+fill+r;
			}else {
				r=r+fill;
			}
		}
		return r;
	}
	public static long zeroday(long t) {
		Calendar cl = java.util.Calendar.getInstance();
		cl.setTimeInMillis(t);
		cl.set(java.util.Calendar.HOUR_OF_DAY, 0);
		cl.set(java.util.Calendar.MINUTE, 0);
		cl.set(java.util.Calendar.SECOND, 0);
		cl.set(java.util.Calendar.MILLISECOND, 0);
		return cl.getTimeInMillis();
	}
	public static long zerominute(long t,int p) {
		Calendar cl = java.util.Calendar.getInstance();
		cl.setTimeInMillis(t);
		int minute=cl.get(java.util.Calendar.MINUTE);
		if(p==0) {
			minute=0;
		}else {
			minute=p*((int)Math.floor(minute/p));
		}
		cl.set(java.util.Calendar.MINUTE, p);
		cl.set(java.util.Calendar.SECOND, 0);
		cl.set(java.util.Calendar.MILLISECOND, 0);
		return cl.getTimeInMillis();
	}
	public static long zerohour(long t) {
		Calendar cl = java.util.Calendar.getInstance();
		cl.setTimeInMillis(t);
		cl.set(java.util.Calendar.MINUTE, 0);
		cl.set(java.util.Calendar.SECOND, 0);
		cl.set(java.util.Calendar.MILLISECOND, 0);
		return cl.getTimeInMillis();
	}
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-","");
	}
	public static double round(double v,int dotnum){
		double q=Math.pow(10,dotnum);
		return((0.0+Math.round(v*q))/q);
	}
}
