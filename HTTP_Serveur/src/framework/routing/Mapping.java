package framework.routing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import framework.request.CodeHTTP;
import framework.request.MethodeHTTP;
import framework.request.Request;

public class Mapping {
	String file = "source/";
	PathTree rootGet;
	PathTree rootPost;
	PathTree rootPut;
	PathTree rootDelete;


	public Mapping() throws IOException {
		super();
		this.rootDelete = this.mappingMethodes("Delete");
		this.rootPut = this.mappingMethodes("Put");
		this.rootPost = this.mappingMethodes("Post");
		this.rootGet = this.mappingMethodes("Get");

	}

	private PathTree mappingMethodes(String methode) throws IOException{
		PathTree root = new PathTree();
		String line;
		String[] arg;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file+methode)));
		while((line=br.readLine()) != null){
			arg = line.split(" ");
			root.addChild(arg[0].replaceFirst("^/", "").split("/"), new MethodeCall(arg[1], arg[2]), 0);
		}
		return root;
	}

	public void callMethode(Request r){
		MethodeCall m = null;
		switch (r.methode) {
		case GET:
			m = rootGet.getMethode(r, r.path.replaceFirst("/", "").split("/"), 0);
			break;
		case POST:
			m = rootPost.getMethode(r, r.path.replaceFirst("/", "").split("/"), 0);
			break;
		case PUT:
			m = rootPut.getMethode(r, r.path.replaceFirst("/", "").split("/"), 0);
			break;
		case DELETE:
			m = rootDelete.getMethode(r, r.path.replaceFirst("/", "").split("/"), 0);
			break;
		}
		if(m == null){
			StringBuffer bf = new StringBuffer();
			bf.append("Chemin invalide");
			r.sendResp(bf, CodeHTTP.NOT_FOUND);
		}else{
			try {
				Object obj = Class.forName(m.getMethodeClass()).newInstance();
				Method m1 = obj.getClass().getDeclaredMethod(m.getMethodeName(), new Class[] {Request.class} );
				m1.invoke(obj, r);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
