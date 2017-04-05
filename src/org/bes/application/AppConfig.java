package org.bes.application;


import org.bes.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configurable
//@ComponentScan("org.bes")
//@EnableWebMvc
public class AppConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//registry.addInterceptor(new AuthenticationInterceptor()).addPathPatterns("/page/**");
		//super.addInterceptors(registry);
	}
}
