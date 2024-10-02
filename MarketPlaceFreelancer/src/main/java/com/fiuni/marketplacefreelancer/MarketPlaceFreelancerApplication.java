package com.fiuni.marketplacefreelancer;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EntityScan("com.fiuni.marketplacefreelancer.domain")

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class MarketPlaceFreelancerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketPlaceFreelancerApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
