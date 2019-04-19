package framework.filed;

public class Date extends AbstractFiled {

	public Date() {
		super("Date", null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String display() {
		return "Date:" + new Date() + "\n";
	}

}
