package site.bc.util;

import java.lang.reflect.Field;

import org.json.JSONArray;


public class JsonUtil {
	public static String toJsonString(Class<?> c, Object o) {
		org.json.JSONObject r = new org.json.JSONObject();
		for (Field f : c.getDeclaredFields()) {
			Class<?> t = f.getType();
			String n = f.getName();
			try {
				if (t.equals(String.class)) {
					f.setAccessible(true);
					r.putOpt(n, f.get(o));
				} else if (t.isPrimitive()) {
					try {
						if (t.getName().equalsIgnoreCase("int") || t.getName().equalsIgnoreCase("short")) {
							f.setAccessible(true);
							r.putOpt(n, f.get(o));
						} else if (t.getName().equalsIgnoreCase("long")) {
							f.setAccessible(true);
							r.putOpt(n, f.get(o));
						} else if (t.getName().equalsIgnoreCase("double") || t.getName().equalsIgnoreCase("float")) {
							f.setAccessible(true);
							r.putOpt(n, f.get(o));
						}
					} catch (Exception e) {

					}
				}
			} catch (Exception e) {

			}
		}
		return r.toString();
	}

	public static String[] json2Strings(JSONArray ja) {
		String[] r=new String[ja.length()];
		for(int i=0;i<r.length;i++) {
			r[i]=ja.optString(i);
		}
		return r;
	}
}
