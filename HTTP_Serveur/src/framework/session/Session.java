package framework.session;

import java.util.Date;
import java.util.HashMap;

public class Session {
	
	String hash;
	Date creationDate;
	Date expireDate;
	HashMap<String, String> information;
	
	public Session(String str, int duration) {
		this.hash = str;
		this.creationDate = new Date();
		this.information = new HashMap<>();
		this.expireDate = new Date(creationDate.getTime()+ 1000*60*duration);
	}
	
	public boolean isExpired(){
		if(expireDate.before(new Date()))
			return true;
		return false;
	}
	
	public boolean isCorrectHash(String h){
		return this.hash.equals(h);
	}

	@Override
	public String toString() {
		return "Session [userAgent=" + hash + ", creationDate=" + creationDate + ", expireDate=" + expireDate
				+ ", information=" + information + "]";
	}
	
	public void putInformation(String key, String value){
		synchronized (information) {
			information.put(key, value);
		}
		
	}
	
	public String getInformation(String key){
		synchronized (information) {
			return information.get(key);
		}
		
	}
	
	public void incrementExpireDate(int duration){
		this.expireDate = new Date(new Date().getTime() + 1000*60*duration);
	}
	
	public String removeInformation(String key){
		synchronized (information) {
			return information.remove(key);
		}
	}
	
	

}
