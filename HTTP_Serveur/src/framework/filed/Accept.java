package framework.filed;

public class Accept extends AbstractFiled{

	public Accept( String fildeValue) {
		super("Accept", fildeValue);
	}

	@Override
	public String display() {
		return "{\""+this.filedName+"\""+":"+"\""+this.fildeValue+"\"}\n";
	}
	
	public String displayInRespHeader(){
		return filedName+": "+fildeValue +  " \n";
	}
}
