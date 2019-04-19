package framework.filed;

public class ContentType extends AbstractFiled{
	
	public ContentType(String value) {
		super("Content-type", value);
	}

	@Override
	public String display() {
		return "{\""+this.filedName+"\""+":"+"\""+this.fildeValue+"\"}\n";
	}
	
	public String displayInRespHeader(){
		return filedName+": "+fildeValue +  " \n";
	}

}
