package site.bc.util;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public final class debug {
	private static DateFormat _df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat utcFormater = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");//20180412T034306Z
	public static boolean isDebug() {
		for(String e:ManagementFactory.getRuntimeMXBean().getInputArguments()) {
			 if (e.startsWith("-Xrunjdwp") || e.startsWith("-agentlib:jdwp")) {
				 return true;
			 }
		}
		return false;
	}

	public static String getInput() {
		byte[] buf=new byte[1024];
		try {
			int rl=System.in.read(buf);
			return new String(buf,0,rl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static String formatdate(long date) {
		// TODO Auto-generated method stub
		return _df.format(date);
	}
	public static String formatfloat(double v, int dotnum) {
		double q=Math.pow(10,dotnum);
		double nv=Math.round(v*q);
		return ""+(nv/(long)q);
	}
}
