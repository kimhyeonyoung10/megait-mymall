package com.megait.mymall;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableAspectJAutoProxy
@SpringBootApplication
public class MymallApplication {

	public static void main(String[] args) {

		SpringApplication.run(MymallApplication.class, args);
	}
	@Bean
	public PasswordEncoder passwordEncoder(){
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	// TODO
	@Bean
	public ServletWebServerFactory servletContainer() {

		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory(){
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");

				// 해당 URL들이 http로 요청되었을 때 https 로 리다이렉트
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/login");
				collection.addPattern("/logout");
				collection.addPattern("/email-check-token");
				collection.addPattern("/check-email");
				collection.addPattern("/cart/*");
				collection.addPattern("/item/like-list");
				// 모두 리다이렉트 하고 싶다면 "/*" 하나로 통일.

				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		tomcat.addAdditionalTomcatConnectors(createStandardConnector());
		return tomcat;
	}

	// TODO
	private Connector createStandardConnector() { // catlina import
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setSecure(false);
		connector.setPort(8080);
		connector.setRedirectPort(8443);
		return connector;
	}
}

