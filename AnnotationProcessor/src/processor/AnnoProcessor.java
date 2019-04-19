package processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import annotation.Delete;
import annotation.Get;
import annotation.Post;
import annotation.Put;
import struct.NodeElem;
import struct.Path;
import struct.Type;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AnnoProcessor extends AbstractProcessor{

	NodeElem root;
	Messager messager;
	Filer filer;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		root = new NodeElem(Type.PATH, "");
		messager=processingEnv.getMessager();
		filer = processingEnv.getFiler();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		ArrayList<Path> paths = new ArrayList<>();
		ArrayList<Class> anno = new ArrayList<Class>();
		anno.add(Get.class);
		anno.add(Post.class);
		anno.add(Put.class);
		anno.add(Delete.class);

		Types    types = processingEnv.getTypeUtils();
		Elements elems = processingEnv.getElementUtils();
		TypeMirror tm= elems.getTypeElement("framework.request.Request").asType();
		String methode, result, chemin = null;
		String line;
		StringBuffer bf = new StringBuffer();
		String filename;
		int i = 0;
		while (i < 4) {
			bf.setLength(0);
			paths.clear();
			filename= anno.get(i).getSimpleName();
			for (Element elem : roundEnv.getElementsAnnotatedWith(anno.get(i))) {
				Annotation annotation = elem.getAnnotation(anno.get(i));
				methode=elem.getEnclosingElement() +" "+elem.getSimpleName();
				if(i==0)
					chemin = ((Get) annotation).path();
				else if(i == 1)
					chemin = ((Post) annotation).path();
				else if(i == 2)
					chemin = ((Put) annotation).path();
				else if(i == 3)
					chemin = ((Delete) annotation).path();
				
				ExecutableType m = (ExecutableType)elem.asType();
				if(m.getParameterTypes().size() != 1){
					messager.printMessage(Kind.ERROR, "doit avoir un unique parametre", elem);
				}else if(!types.isSameType(tm, m.getParameterTypes().get(0))){
					messager.printMessage(Kind.ERROR, "doit avoir un unique parametre request", elem);
				}
				Path tmp = new Path(chemin, methode);
				result = this.contains(tmp, paths);
				if(result != null){
					messager.printMessage(Kind.ERROR, "Erreur : conflit avec "+ result, elem);
				}else{
					paths.add(tmp);
				}
			}
			try {
				FileObject fo = filer.getResource(StandardLocation.SOURCE_OUTPUT, "", filename);
				BufferedReader br = new BufferedReader(new InputStreamReader(fo.openInputStream()));
				//ObjectInputStream oi = new ObjectInputStream(fo.openInputStream());
				// paths.addAll((Collection<? extends Path>) oi.readObject());
				while((line=br.readLine()) != null){
					bf.append(line+"\n");
				}
				br.close();
			} catch (IOException e) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "peut pas trouver " + e.getMessage());
			}
			try {
				PrintWriter pw = new PrintWriter(filer.createResource( StandardLocation.SOURCE_OUTPUT, "", filename).openOutputStream());
				//ObjectOutputStream o = new ObjectOutputStream(filer.createResource( StandardLocation.SOURCE_OUTPUT, "", path1).openOutputStream());
				//o.writeObject(paths);
				pw.print(bf);
				for (Path path : paths) {
					pw.println(path);
				}
				pw.close();
			} catch (IOException e) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "peut pas crer " + e.getMessage());
			}
			i++;
		}
		return true;

	}
	private String contains(Path tmp, ArrayList<Path> list){
		for (Path path : list) {
			if(path.equals(tmp))
				return path.toString();
		}
		return null;
	}
	//	@Override
	//	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
	//		HashMap<String, String> map = new HashMap<>();
	//		HashMap<String, Element> mapElement = new HashMap<>();
	//		Types    types = processingEnv.getTypeUtils();
	//		Elements elems = processingEnv.getElementUtils();
	//		TypeMirror tm= elems.getTypeElement("framework.request.Request").asType();
	//		String methode, chemin;
	//		for (Element elem : roundEnv.getElementsAnnotatedWith(Get.class)) {
	//			Get annotation = elem.getAnnotation(Get.class);
	//			methode=elem.getEnclosingElement() +" "+elem.getSimpleName();
	//			chemin = annotation.path();
	//			ExecutableType m = (ExecutableType)elem.asType();
	//			if(m.getParameterTypes().size() != 1){
	//				messager.printMessage(Kind.ERROR, "doit avoir un unique parametre", elem);
	//			}else if(!types.isSameType(tm, m.getParameterTypes().get(0))){
	//				messager.printMessage(Kind.ERROR, "doit avoir un unique parametre request", elem);
	//			}
	//			if(map.containsKey(chemin)){
	//				messager.printMessage(Kind.ERROR, "Code bien pd : conflit avec "+ map.get(chemin), elem);
	//				messager.printMessage(Kind.ERROR, "Code bien pd : conflit avec "+ methode, mapElement.get(chemin));
	//			}else{
	//				map.put(chemin, methode);
	//				mapElement.put(chemin, elem);
	//			}
	//		}
	//		
	//		String line;
	//		StringBuffer bf = new StringBuffer();
	//		try {
	//			FileObject fo = filer.getResource(StandardLocation.SOURCE_OUTPUT, "", "toto.txt");
	//			//BufferedReader br = new BufferedReader(new InputStreamReader(fo.openInputStream()));
	//			ObjectInputStream oi = new ObjectInputStream(fo.openInputStream());
	//			 map.putAll((Map<String,String>) oi.readObject());
	//		} catch (IOException | ClassNotFoundException e) {
	//			processingEnv.getMessager().printMessage(Kind.ERROR, "peut pas trouver " + e.getMessage());
	//		}
	//		try {
	//			String path1 = "toto.txt";
	//			//PrintWriter pw = new PrintWriter(filer.createResource( StandardLocation.CLASS_OUTPUT, "", path1).openOutputStream());
	//			ObjectOutputStream o = new ObjectOutputStream(filer.createResource( StandardLocation.SOURCE_OUTPUT, "", path1).openOutputStream());
	//			o.writeObject(map);
	//			o.close();
	//		} catch (IOException e) {
	//			processingEnv.getMessager().printMessage(Kind.ERROR, "peut pas crer " + e.getMessage());
	//		}
	//		return true;
	//	}
}
