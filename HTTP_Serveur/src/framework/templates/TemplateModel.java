package framework.templates;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import framework.routing.MethodeCall;
import framework.session.Session;
import framework.session.SessionHandler;

public class TemplateModel {
	
	public String fileContent;
	public HashMap<String, String> context;
	
	public TemplateModel(String filePath) throws IOException {
		
		fileContent = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
		context = new HashMap<>();
	}
	
	public void addVariable(String key, String value){
		context.put(key, value);
	}
	
	public void addObject(Object obj) throws IllegalArgumentException, IllegalAccessException{
		Field[] attribut = obj.getClass().getDeclaredFields();
		for (Field field : attribut) {
			field.setAccessible(true);
			context.put(field.getName(), field.get(obj).toString());
		}
	}
	
	
	@Override
	public String toString() {
		for (Map.Entry<String, String> map : context.entrySet()) {
		
			fileContent = fileContent.replaceAll("\\{\\{"+map.getKey()+"\\}\\}", map.getValue());
		}
		return fileContent;
	}
	
	
}
