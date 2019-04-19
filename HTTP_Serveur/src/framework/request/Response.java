package framework.request;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

import annotation.Get;
import framework.filed.Cookies;
import framework.filed.IFiled;
import framework.filed.SetCookie;

public class Response {
	
	private CodeHTTP code;
	private MethodeHTTP methode;
	private String path;
	private ArrayList<IFiled> filedList = new ArrayList<>();
	private StringBuffer content;
	
	
	
	public CodeHTTP getCode() {
		return code;
	}

	public void setCode(CodeHTTP code) {
		this.code = code;
	}

	public MethodeHTTP getMethode() {
		return methode;
	}

	public void setMethode(MethodeHTTP methode) {
		this.methode = methode;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ArrayList<IFiled> getFiledList() {
		return filedList;
	}

	public void setFiledList(ArrayList<IFiled> filedList) {
		this.filedList = filedList;
	}

	public StringBuffer getContent() {
		return content;
	}

	public void setContent(StringBuffer content) {
		this.content = content;
	}

	public Response(CodeHTTP code, MethodeHTTP methode, String path, StringBuffer content, ArrayList<IFiled> list) {
		super();
		this.code = code;
		this.methode = methode;
		this.path = path;
		this.filedList = list;
		this.content = content;
	}
	
	public Response(CodeHTTP code, MethodeHTTP methode, String path, ArrayList<IFiled> list) {
		super();
		this.code = code;
		this.methode = methode;
		this.path = path;
		this.filedList = list;
		this.content = new StringBuffer();
	}
	
	public Response(MethodeHTTP methode, String path) {
		super();
		this.code = code;
		this.methode = methode;
		this.path = path;
		this.content = new StringBuffer();
	}
	
	public void setCookie(SetCookie c){
		this.filedList.add(c);
	}
	
	@Override
	public String toString() {
		StringBuffer bf = new StringBuffer();
		bf.append("HTTP/1.1 "+ this.code.toString()+"\n");
		bf.append("Date:" + new Date() + "\n");
		for (IFiled iFiled : filedList) {
			bf.append(iFiled.displayInRespHeader());
		}
		bf.append("\n");
		bf.append(content);
		return bf.toString();
	}
	
	
	
	
	
	

}
