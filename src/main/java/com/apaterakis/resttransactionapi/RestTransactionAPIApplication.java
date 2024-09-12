package com.apaterakis.resttransactionapi;

import com.apaterakis.resttransactionapi.service.LoadDataService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestTransactionAPIApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestTransactionAPIApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(LoadDataService service) {
		return runner -> {
			service.loadDataFromBeneficiariesCsv("beneficiaries.csv");
			service.loadDataFromAccountsCsv("accounts.csv");
		};
	}
}
