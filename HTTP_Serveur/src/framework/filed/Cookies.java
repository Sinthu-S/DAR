package framework.filed;

import java.util.HashMap;
import java.util.Map;

public class Cookies extends AbstractFiled {
	
	public Map<String , String> mapCookies = new HashMap<String, String>();

	public Cookies(String fildeValue) {
		super("Cookie", fildeValue);
		//System.out.println("string cookie "+fildeValue);

		String[] partsCookies = fildeValue.split("; ");
		if(partsCookies.length > 0)
		for (String string : partsCookies) {
			String[] partsCookie = string.split("=");
			//System.out.println(partsCookie[0]+ " "+ partsCookie[1]);
			mapCookies.put(partsCookie[0], partsCookie[1]);
		}
		
		//System.out.println("map cookie "+mapCookies);
	}
	
	
	public boolean contains(String name){
		//System.out.println(name +" "+mapCookies.containsKey(name));
		return mapCookies.containsKey(name);
	}
	
	public String get(String name){
		//System.out.println("map  "+mapCookies.get(name));
		return mapCookies.get(name);
	}
	
	public String getValues(String key){
		return this.mapCookies.get(key);
	}

	@Override
	public String display() {
		return null;
	}

}
