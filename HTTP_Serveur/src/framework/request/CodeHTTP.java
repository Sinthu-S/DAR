package framework.request;

public enum CodeHTTP {
	OK("200 OK"),
	BAD_REQUEST("400 Bad Request"),
	NOT_FOUND("404 Not Found");
	
	private String text;
	CodeHTTP(String s) {
		this.text=s;
	}
	
	public String toString(){
		return this.text;
	}
	
}
