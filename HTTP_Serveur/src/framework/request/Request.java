package framework.request;

import java.beans.MethodDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import annotation.Get;
import framework.filed.ContentType;
import framework.filed.Cookies;
import framework.filed.GenericFiled;
import framework.filed.IFiled;
import framework.filed.SetCookie;
import framework.session.Session;
import framework.session.SessionHandler;
import jdk.nashorn.internal.parser.JSONParser;

public class Request {

	public BufferedReader inchan;
	private PrintWriter outchan;
	public MethodeHTTP methode;
	public HashMap<String, String> params;
	public HashMap<String, String> urlParams;
	public String path;
	public String protocol;
	public HashMap<String, IFiled> filedList;
	public String responseType;
	public Response resp;
	public Session session;
	public StringBuffer body;
	public int duration;

	public Request(Socket s, int d) throws IOException {
		inchan = new BufferedReader(new InputStreamReader(s.getInputStream()));
		outchan = new PrintWriter(s.getOutputStream(), true);
		filedList = new HashMap<>();
		params = new HashMap<>();
		urlParams = new HashMap<>();
		responseType = "";
		session= null;
		body = new StringBuffer();
		duration = d;
		this.resp = new Response(this.methode, this.path);
		this.constParam();
		this.constFields();
		this.constBody();
	}



	public Request() {
		super();
	}

	@Override
	public String toString() {
		return "Request [inchan=" + inchan + ", methode=" + methode + ", path=" + path + ", protocol=" + protocol
				+ ", filedList=" + filedList + "]";
	}
	public void toto(){
		System.out.println("toto get");
	}
	public StringBuffer dispalyHTML(){
		StringBuffer bf = new StringBuffer();
		bf.append("<!doctype html><html lang=\"fr\"><head><meta charset=\"utf-8\"><title>DAR</title></head><body><table>");
		bf.append("<tr><td>Name</td><td>Values</td></tr>" );
		//		for (IFiled iFiled : filedList) {
		//			bf.append(iFiled.displayHTML());
		//		}
		bf.append("</table></body></html>");
		return bf;
	}

	private StringBuffer dispalyPlain() {
		StringBuffer bf = new StringBuffer();
		//		for (IFiled iFiled : filedList) {
		//			bf.append(iFiled.dispalyPlain());
		//		}
		return bf;
	}


	private StringBuffer dispalyJSON() {
		StringBuffer bf = new StringBuffer();
		bf.append("{");
		for (int i = 0; i < filedList.size()-1; i++) {
			bf.append(filedList.get(i).dispalyJSON());
			bf.append(", ");
		}
		bf.append(filedList.get(filedList.size()-1).dispalyJSON());
		bf.append("}");
		return bf;
	}

	public StringBuffer dispalyResp(){
		switch (this.responseType) {
		case "text/html":
			return this.dispalyHTML();
		case "application/json":
			return this.dispalyJSON();
		default:
			return this.dispalyPlain();
		}
	}

	public String getResp(){
		ArrayList<IFiled> head = new ArrayList<>();
		head.add(new ContentType(this.responseType));
		Response res = new Response(CodeHTTP.OK, this.methode, this.path, this.dispalyResp(), head);
		return res.toString();
	}
	
	public boolean containsCookie(String key) {
		Cookies cookieMap = (Cookies) this.filedList.get("cookie");
		if(cookieMap == null)
			return false;
		return cookieMap.contains(key);
	}
	
	public String getCookieValue(String key){
		Cookies cookieMap = (Cookies) this.filedList.get("cookie");
		if(cookieMap == null)
			return null;
		return cookieMap.get(key);
		
	}

	public void sendResp(StringBuffer bf, CodeHTTP code){
		ArrayList<IFiled> head = new ArrayList<>();
		this.resp.setCode(code);
		this.resp.setContent(bf);
		outchan.println(resp.toString());
		outchan.flush();
		outchan.close();
	}
	
	public Session getSession(){
		return this.session;
	}

	private void constParam() throws IOException{
		String[] params=inchan.readLine().split(" ");
		this.methode = MethodeHTTP.valueOf(params[0]);
		String tmp[] = params[1].split("\\?");
		this.protocol = params[2];
		this.path = tmp[0];
		if(tmp.length > 1){
			params = tmp[1].split("&");
			for (String s : params) {
				tmp = s.split("=");
				if(tmp.length > 1)
					this.params.put(tmp[0], tmp[1]);
			}
		}
	}

	private void constFields() throws IOException{
		String line;
		String[] params = new String[2];
		boolean cookie = false;
		while((line = this.inchan.readLine()) != null && line.length() != 0 ){
			System.out.println("line --------->" + line);
			params = line.split(": ");
			this.filedList.put(params[0].toLowerCase(), FactoryFiled.createFiled(params[0], params[1]));
			if(params[0].toLowerCase().equals("accept"))
				this.responseType=params[1].toLowerCase().split(",")[0];
		}
		Cookies cookieMap = (Cookies) this.filedList.get("cookie");
		if(cookieMap == null){
			createSessionCookies();
			return;
		}

		if(cookieMap.contains("SESSION")){
			if(!chekSessionCookies(UUID.fromString(cookieMap.get("SESSION"))))
				createSessionCookies();
		}else{
			createSessionCookies();
		}
	}

	private void constBody() throws IOException {
		IFiled length = this.filedList.get("content-length");
		if(length != null){
			int taille = Integer.parseInt(length.getValues());
			char[] chars = new char[taille];
			this.inchan.read(chars, 0, taille);
			this.body.append(new String(chars));
		}
		System.out.println(this.body);
	}
	
	public boolean contentIsJSON(){
		IFiled type = this.filedList.get("content-type");
		return type.getValues().equals("application/json");
	}
	
	
	private boolean chekSessionCookies(UUID sessionKey){
		if(SessionHandler.contains(sessionKey)){
			Session s = SessionHandler.getSession(sessionKey);
			if(s.isExpired()){
				return false;
			}

			if(!s.isCorrectHash(this.filedList.get("user-agent").getValues()))
				return false;

			this.session=SessionHandler.getSession(sessionKey);
			this.session.incrementExpireDate(duration);
			return true;

		}else{
			return false;
		}

	}

	private void createSessionCookies(){
		UUID id = SessionHandler.addSession(this.filedList.get("user-agent").getValues(), duration);
		this.resp.setCookie(new SetCookie("SESSION="+id));
		this.session = SessionHandler.getSession(id);
	}
}
