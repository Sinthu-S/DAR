package framework.request;

import framework.filed.AbstractFiled;
import framework.filed.Accept;
import framework.filed.ContentType;
import framework.filed.Cookies;
import framework.filed.GenericFiled;
import framework.filed.IFiled;

public class FactoryFiled {

	public static IFiled  createFiled(String name, String value){
		AbstractFiled cur ; 

		name = name.toLowerCase();
		switch (name) {
		case "accept":
			return new Accept(value) ; 
		
		case "cookie":
			Cookies c = new Cookies(value);
			//System.out.println(c.toString());
			return c;
			
		default:
			GenericFiled g = new GenericFiled(name, value);
			//System.out.println(g.toString());
			return g ;
		}
	
	}
}
