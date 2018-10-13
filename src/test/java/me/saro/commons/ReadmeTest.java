package me.saro.commons;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import me.saro.commons.web.Web;
public class ReadmeTest {
	
	@Test	
	public void justPrintReadme() {
		
		
		System.out.println("# STATIC");
		System.out.println();
		
		outClassInfo(Converter.class);
		outClassInfo(Utils.class);
		outClassInfo(Files.class);
		outClassInfo(Valids.class);
		outClassInfo(Lambdas.class);
		
		System.out.println("# INSTANCE");
		System.out.println();
		
		outClassInfo(Web.class);
		outClassInfo(Ftp.class);
		outClassInfo(DateFormat.class);
		outClassInfo(JsonReader.class);
		
		System.out.println("# FUNCTION INTERFACE");
		System.out.println("#### me.saro.commons.function.*");
		System.out.println("```\n" + 
			"ThrowableRunnable\n" + 
			"ThrowableSupplier<R>\n" + 
			"ThrowablePredicate<T>\n" + 
			"ThrowableConsumer<T>\n" + 
			"ThrowableBiConsumer<T, U>\n" + 
			"ThrowableTriConsumer<T, U, V>\n" + 
			"ThrowableFunction<T, R>\n" + 
			"ThrowableBiFunction<T, U, R>\n" + 
			"ThrowableTriFunction<T, U, V, R>\n" + 
		"```");
	}
	
	public void outClassInfo(Class<?> clazz) {
		
		String className = clazz.getSimpleName();
		
		System.out.println();
		System.out.println("## " + className);
		System.out.println();
		System.out.println("#### " + clazz.getName());
		System.out.println();
		
		System.out.println("```");
		Stream.of(clazz.getDeclaredMethods()).map(method -> {
			return Stream.of(
				getName(className, method) + getParameters(method)
			,	getGenericReturnType(method)
			)
			.collect(Collectors.joining(" "));
		})
		.filter(e -> !e.startsWith("0"))
		.sorted()
		.map(e -> e.substring(1))
		.forEach(e -> System.out.println(e) );
		System.out.println("```");
		System.out.println();
	}
	
	public String getGenericReturnType(Method method) {
		String rv = method.getGenericReturnType().getTypeName().replaceAll("[a-z]+\\.", "");
		return rv.equals("void") ? "" : ": " +rv; 
	}
	
	public String getName(String className, Method method) {
		String prefix = Modifier.toString(method.getModifiers());
		prefix = (prefix.startsWith("public static") ? ("1") : (prefix.startsWith("public") ? "2" : "0"));
		return prefix + (prefix.equals("1") ? (className + ".") : "") + method.getName();
	}
	
	public String getParameters(Method method) {
		// need -parameters options
		return "(" + Stream.of(method.getParameters())
		.map(e -> e.getParameterizedType().getTypeName().replaceAll("[a-z]+\\.", ""))
		.collect(Collectors.joining(", ")) + ")";
		
	}
}
