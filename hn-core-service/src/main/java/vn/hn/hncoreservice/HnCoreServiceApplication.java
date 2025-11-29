package vn.hn.hncoreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		scanBasePackages = {
				"vn.hn.hncoreservice",
				"vn.hn.hncommonservice"
		}
)
public class HnCoreServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(HnCoreServiceApplication.class, args);
	}
	
}
