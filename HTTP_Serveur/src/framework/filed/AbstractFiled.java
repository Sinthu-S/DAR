package framework.filed;

public abstract class AbstractFiled implements IFiled{

	String filedName ;
	String fildeValue ;
	
	
	public AbstractFiled(String filedName, String fildeValue) {
		super();
		this.filedName = filedName;
		this.fildeValue = fildeValue;
	}
	
	public String displayInRespHeader(){
		return filedName+": "+fildeValue +  " \n";
	}
	
	public String display(){
		return filedName+": "+fildeValue +  " \n";
	}
	
	@Override
	public String getName(){
		return filedName;
	}
	
	@Override
	public String getValues(){
		return fildeValue;
	}
	@Override
	public String displayHTML() {
		return "<tr><td>"+this.filedName+"</td><td>"+this.fildeValue+"</td></tr>" ;
	}

	@Override
	public String dispalyJSON() {
		return "\""+this.filedName+"\""+":"+"\""+this.fildeValue+"\"";
	}
	@Override
	public String dispalyPlain(){
		return filedName+" : "+fildeValue +  " \n";
	}
	
}
