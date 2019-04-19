package framework.filed;

public class GenericFiled extends AbstractFiled {

	public GenericFiled(String filedName, String fildeValue) {
		super(filedName, fildeValue);
	}

	@Override
	public String display() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "{\""+this.filedName+"\""+":"+"\""+this.fildeValue+"\"}\n";
	}
}
