package framework.routing;

import java.util.ArrayList;

import framework.request.Request;

public class PathTree {

	public PathType type;
	public String path;
	public MethodeCall methode;
	public ArrayList<PathTree> children;

	public PathTree(PathType type, String name) {
		super();
		this.type = type;
		this.path = name;
		this.children = new ArrayList<>();
	}

	public PathTree() {
		this.children = new ArrayList<>();
	}

	public PathTree(String val) {
		this.path=val;
		this.children = new ArrayList<>();
	}

	public void addChild(String[] arg, MethodeCall m, int i){
		String str = arg[i];
		for (PathTree p : this.children) {
			if(p.type.equals(PathType.ARGUMENT) || str.equals(p.path)){
				if(arg.length-1 == i)
					p.methode=m;
				else
					p.addChild(arg, m, ++i);
				return;
			}
		}
		PathTree newPath = new PathTree(str);
		if(str.startsWith("<") && str.endsWith(">"))
			newPath.type=PathType.ARGUMENT;
		else
			newPath.type=PathType.SIMPLE_PATH;

		if(arg.length-1 == i){
			newPath.methode = m;
		}else{
			newPath.addChild(arg, m, ++i);
		}
		this.children.add(newPath);

	}
	
	public MethodeCall getMethode(Request r, String[] url, int i){
		String str = url[i];
		for (PathTree p : this.children) {
			if(p.type.equals(PathType.ARGUMENT)){
				r.urlParams.put(p.getUrlParam(), str);
			}
			if(p.type.equals(PathType.ARGUMENT) || str.equals(p.path)){
				if(url.length-1 == i)
					return p.methode;
				else
					return p.getMethode(r, url, ++i);
			}
		}
		return null;
	}
	
	private String getUrlParam(){
		String str = this.path;

		str=str.replaceFirst("<", "");
		str=str.replaceFirst(">", "");
		return str;
	}

	@Override
	public String toString() {
		StringBuffer bf = new StringBuffer();
		bf.append(this.path+" ");
		bf.append(this.methode+" ");
		bf.append(this.type+" ");
		for (PathTree pathTree : children) {
			bf.append("------>\n "+pathTree.toString()+"");
		}
		return bf.toString();
	}
}
