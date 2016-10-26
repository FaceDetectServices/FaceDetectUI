package com.yoshio3;

import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.sun.faces.config.FacesInitializer;

/**
 *
 * @author Toshiaki Maki
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableAsync
public class FaceDetectUiApplication {

	@Configuration
	static class MvcConfig extends WebMvcConfigurerAdapter {
		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController("/").setViewName("redirect:/index.xhtml");
		}
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	AsyncRestTemplate asyncRestTemplate(@LoadBalanced RestTemplate restTemplate) {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setTaskExecutor(threadPoolTaskScheduler());
		return new AsyncRestTemplate(requestFactory, restTemplate);
	}

	@Bean
	ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(5);
		return taskScheduler;
	}

	@Bean
	ServletRegistrationBean facesServletRegistration() {
		return new ServletRegistrationBean() {
			@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
				FacesInitializer facesInitializer = new FacesInitializer();
				facesInitializer.onStartup(
						Collections.singleton(FaceDetectUiApplication.class),
						servletContext);
			}
		};
	}

	@Configuration
	static class JsfConfig implements ServletContextInitializer {

		@Override
		public void onStartup(ServletContext servletContext) throws ServletException {
			servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");
			servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
			servletContext.setInitParameter(
					"javax.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL",
					"true");
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(FaceDetectUiApplication.class, args);
	}
}
