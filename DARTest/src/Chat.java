import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import annotation.Get;
import framework.filed.SetCookie;
import framework.request.CodeHTTP;
import framework.request.Request;
import framework.templates.TemplateModel;

public class Chat {

	@Get(path="/")
	public void bienvenue(Request t) throws IOException{
		String value = t.getCookieValue("CHAT");
		if(value == null){
			this.inscription(t);
		}else{
			if(!value.equals(t.session.getInformation("chatId"))){
				this.inscription(t);
			}else{
				StringBuffer bf = new StringBuffer();
				TemplateModel tm = new TemplateModel("Template/Chat.html");
				Main.listUser.add(t.session.getInformation("name"));
				tm.addVariable("nameUser", t.session.getInformation("name"));
				t.sendResp(bf.append(tm), CodeHTTP.OK );
			}
		}
		
	}
	private void inscription(Request t){
		StringBuffer bf = new StringBuffer();
		try {
			TemplateModel tm = new TemplateModel("Template/Bienvenue.html");
			t.sendResp(bf.append(tm), CodeHTTP.OK );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Put(path="/inscription")
	public void ajouteUser(Request t) throws IOException{
		StringBuffer bf = new StringBuffer();
		TemplateModel tm = new TemplateModel("Template/Chat.html");
		Main.listUser.add(t.params.get("name"));
		tm.addVariable("nameUser",t.params.get("name") );
		String id = UUID.randomUUID().toString();
		t.resp.setCookie(new SetCookie("CHAT="+id));
		t.session.putInformation("chatId", id);
		t.session.putInformation("name", t.params.get("name"));
		t.session.putInformation("numero", Main.listMsg.size()+"");
		t.sendResp(bf.append(tm), CodeHTTP.OK );	
	}
	
	
	@Get(path="/deconnexion")
	public void deconnexion(Request t) throws IOException{
		String value = t.getCookieValue("CHAT");
		StringBuffer bf = new StringBuffer();
		t.session.removeInformation("chatId");
		Main.listUser.remove(t.session.getInformation("name"));
		t.sendResp(new StringBuffer(), CodeHTTP.OK );
	}
	
	
	@Put(path="/sendMsg")
	public void sendMsg(Request t) throws IOException{
		String m = t.params.get("message");
		m = m.replaceAll("\\%20", " ");
		Main.listMsg.add(m);
		t.sendResp(new StringBuffer(), CodeHTTP.OK);
	}

	
	
	@Get(path="/afficheUser")
	public void afficheUser(Request t){
		StringBuffer bf = new StringBuffer();
		for (String string : Main.listUser) {
			bf.append(" " + string+"<br>");
		}
		t.sendResp(bf, CodeHTTP.OK);
	}
	
	@Get(path="/afficheMsg")
	public void afficheMsg(Request t){
		StringBuffer bf = new StringBuffer();
		for(int i = Integer.parseInt(t.session.getInformation("numero")) ; i<Main.listMsg.size();i++){
			String string = Main.listMsg.get(i);
		//for (String string : Test.listMsg) {
			bf.append(" " + string+"<br>");
		}
		t.sendResp(bf, CodeHTTP.OK);
	}
}
