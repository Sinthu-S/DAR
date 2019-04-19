package framework.routing;

public class MethodeCall {
	
	String methodeClass;
	String methodeName;
	
	public MethodeCall(String c, String m) {
		this.methodeClass = c;
		this.methodeName = m;
	}

	@Override
	public String toString() {
		return "Methode [methodeClass=" + methodeClass + ", methodeName=" + methodeName + "]";
	}

	public String getMethodeClass() {
		return methodeClass;
	}

	public String getMethodeName() {
		return methodeName;
	}

	public void setMethodeClass(String methodeClass) {
		this.methodeClass = methodeClass;
	}

	public void setMethodeName(String methodeName) {
		this.methodeName = methodeName;
	}
	
	
}
