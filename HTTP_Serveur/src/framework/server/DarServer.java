package framework.server;


import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

import framework.request.Request;
import framework.routing.Mapping;
import framework.routing.MethodeCall;
import framework.routing.PathTree;
import framework.session.Session;
import framework.session.SessionCleaner;
import framework.templates.TemplateModel;

public class DarServer{

	public  void start(int nbThread, int maxThread, int delay, int duration, int port) throws IOException  {
		Mapping mapper = new Mapping();
		ServerSocket socketserver = new ServerSocket(port);
		SessionCleaner cleaner = new SessionCleaner();
		Timer timer = new Timer(true);
		timer.schedule(cleaner, 0, 1000*60*duration);
		
	    ExecutorService executorService = new ThreadPoolExecutor(nbThread, maxThread, delay, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		
		while(true){
			Socket socket = socketserver.accept();
			executorService.submit(new Runnable() {
				
				@Override
				public void run() {
					try {
						Request r = new Request(socket, duration);
						mapper.callMethode(r);
					} catch (IOException e) {
						e.printStackTrace();
					}	
				}
			});
			
		}
		
		
		
//		TemplateModel t = new TemplateModel("template/toto.html");
//				try {
//					t.addObject(new Point(0, 1));
//				} catch (IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		t.addVariable("id","3");
//		System.out.println(t);

		
	}

	@Override
	public String toString() {
		
		return "Server []";
	}
	
	
}
