package org.bes.application;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class ApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(AppConfig.class);
		ctx.setServletContext(servletContext);
		
		servletContext.setInitParameter("contextConfigLocation", "classpath*:applicationContext.xml");
		
		XmlWebApplicationContext wct = new XmlWebApplicationContext();
		wct.setConfigLocation("classpath*:springmvc-servlet.xml");
		
		Dynamic registration = servletContext.addServlet("springmvc", new DispatcherServlet(wct));
		registration.setLoadOnStartup(1);
		registration.addMapping("/");

		long maxFileSize = 1024 * 1024 * 10;
		MultipartConfigElement multipartConfig = new MultipartConfigElement(
				"E:/tmp", maxFileSize, maxFileSize * 2, 0);
		registration.setMultipartConfig(multipartConfig);


		servletContext.addListener(ContextLoaderListener.class);
	}

}
