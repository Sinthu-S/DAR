package applications;

import annotation.Delete;
import annotation.Get;
import annotation.Post;
import annotation.Put;
import framework.filed.SetCookie;
import framework.request.CodeHTTP;
import framework.request.Request;


public class HelloWorld {
	


	@Put(path="/hello2")
	public void hello(Request r){
		StringBuffer bf = new StringBuffer();
		bf.append("Hello World!");
		r.sendResp(bf, CodeHTTP.OK);
	}
	
	@Get(path="/hello2")
	public void hello1(Request t){
		StringBuffer bf = new StringBuffer();
		bf.append("M");
		t.resp.setCookie(new SetCookie("TOTO=1"));
		t.resp.setCookie(new SetCookie("TOPO=1"));
		System.out.println("sesssssssssion "+t.getSession().getInformation("toto"));
		t.sendResp(bf, CodeHTTP.OK);
	}
	
	@Get(path="/hello3/<id>/<p>/")
	public void hello3(Request t){
		StringBuffer bf = new StringBuffer();
		System.out.println(t.params.get("toto"));
		System.out.println(t.urlParams);
		System.out.println(t.urlParams.get("id"));
		System.out.println(t.urlParams.get("p"));

		bf.append(t.params);
		t.resp.setCookie(new SetCookie("panier=3"));
		t.sendResp(bf, CodeHTTP.OK);
	}
	
	
}
