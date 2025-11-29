package vn.hn.hneurekaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class HnEurekaServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(HnEurekaServiceApplication.class, args);
	}
	
}
