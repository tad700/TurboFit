package com._3.TurboFit;

import com._3.TurboFit.api.configuration.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class TurboFitApplication {

	public static void main(String[] args) {
		SpringApplication.run(TurboFitApplication.class, args);
	}

}
