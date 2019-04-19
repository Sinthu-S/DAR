package struct;

import java.io.Serializable;
import java.util.Arrays;

public class Path implements Serializable{

	String path;
	String methode;
	String[] element;

	public Path(String path, String m) {
		super();
		this.path = path;
		this.methode=m;
		this.element = path.replaceFirst("/", "").split("/");
		for (int i = 0; i < element.length; i++) {
			if(element[i].startsWith("<") && element[i].endsWith(">"))
				element[i] = "<>";
		}
	}

	@Override
	public String toString() {
		return path+" "+methode;
	}

	public String getMethode() {
		return methode;
	}

	public void setMethode(String methode) {
		this.methode = methode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(element);
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		int size;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Path other = (Path) obj;
		if (element == null) {
			if (other.element != null)
				return false;
		} else {
			String[] str = ((Path) obj).element;
			if(str.length > this.element.length)
				return false;
			else
				size = str.length;
			System.out.println(size);
			for (int i = 0; i < size; i++) {
				if(this.element[i].equals("<>")){
					System.out.println("var");
					if( i == str.length-1)
						return true;
					else
						continue;
				}
				if(!str[i].equals(this.element[i]))
					return false;
				else if(i == str.length-1 && size < this.element.length)
					return false;
			}
		}
		return true;
	}


	public static void main(String[] args) {
		Path t = new Path("/hello3/<id>", null);
		Path p = new Path("/hello3/<id>/<id>", null);
		
		System.out.println(t.equals(p));
	}


}
