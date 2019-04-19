import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import framework.server.DarServer;

public class Main {
	
	static Set<String> listUser = new HashSet<String>() ;
	static ArrayList<String> listMsg = new ArrayList<String>() ;
	
	public static void main(String[] args) {
		DarServer serveur = new DarServer();
		
		try {
			serveur.start(5, 10, 20, 60, 8080);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
