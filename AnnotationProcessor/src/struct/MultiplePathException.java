package struct;

import javax.lang.model.element.Element;

public class MultiplePathException extends Exception {
	public Element methode;

	public MultiplePathException(Element methode) {
		super("MultiplePath");
		this.methode = methode;
	}
	
	
}
