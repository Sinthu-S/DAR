package framework.session;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;import java.util.Map;
import java.util.UUID;

public class SessionHandler {
	
	static HashMap<UUID, Session> currentSession = new HashMap<>();
	
	public static boolean contains(UUID key){
		synchronized (currentSession) {
			return SessionHandler.currentSession.containsKey(key);
		}
	}
	
	public static UUID addSession(String hash, int defaultDuration){
		UUID id = UUID.randomUUID();
		synchronized (currentSession) {
			currentSession.put(id, new Session(hash, defaultDuration));
		}
		return id;
	}
	
	public static Session getSession(UUID uid){
		synchronized (currentSession) {
			return currentSession.get(uid);
		}
		
	}
	
	public static void removeExpiredSession(){
		ArrayList<UUID> expired = new ArrayList<>();
		synchronized (currentSession) {
			for (Map.Entry<UUID, Session> map : SessionHandler.currentSession.entrySet()) {
				if(map.getValue().isExpired())
					expired.add(map.getKey());
			}
			
			for (UUID uuid : expired) {
				SessionHandler.currentSession.remove(uuid);
			}
		}
		
	}
}
