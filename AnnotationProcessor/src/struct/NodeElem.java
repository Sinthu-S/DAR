package struct;

import java.util.ArrayList;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class NodeElem {
	Type type;
	String name;
	Element methode;
	ArrayList<NodeElem> child;

	public NodeElem(Type type, String name) {
		super();
		this.type = type;
		this.name = name;
		this.child = new ArrayList<>();
	}

	public NodeElem() {
		this.child = new ArrayList<>();
		this.child = new ArrayList<>();

	}

	public NodeElem(String val) {
		this.name=val;
		this.child = new ArrayList<>();
	}
	
	public String toString(String path){
		StringBuffer bf =new StringBuffer();
		if(methode!=null){
			bf.append(path+"/"+name+
					" "
					+ ((TypeElement)methode.getEnclosingElement()).getQualifiedName()
					+" "+
					methode.getSimpleName()+ "\n");
		}
		String newPath= path+"/"+name;
		for (NodeElem nodeElem : child) {
			bf.append(nodeElem.toString(newPath));
		}
		return bf.toString();
	}

	public void addChild(String[] path, Element elem, int i) throws MultiplePathException{
		for (NodeElem n : child) {
			if(path[i].equals(n.name)){
				if(i == path.length-1 && ( n.type.equals(Type.ARGUMENT) || n.methode!=null)){
					throw(new MultiplePathException(elem));
				}
			}
		}
		String val = path[i];
		NodeElem node = new NodeElem(val);
		if(val.startsWith("<") && val.endsWith(">")){
			node.setType(Type.ARGUMENT);
		}else{
			node.setType(Type.PATH);
		}
		child.add(node);
		if(i == path.length-1){
			node.setMethode(elem);
		}else{
			node.addChild(path, elem, ++i);
		}
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public Element getMethode() {
		return methode;
	}

	public ArrayList<NodeElem> getChild() {
		return child;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMethode(Element methode) {
		this.methode = methode;
	}

	public void setChild(ArrayList<NodeElem> child) {
		this.child = child;
	}
}

