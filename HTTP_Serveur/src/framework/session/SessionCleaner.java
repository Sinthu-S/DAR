package framework.session;

import java.util.TimerTask;

public class SessionCleaner extends TimerTask{

	@Override
	public void run() {
		//System.out.println("Start-------> session cleaning");
		SessionHandler.removeExpiredSession();
		//System.out.println("End-------> session cleaning");
		
	}

}
