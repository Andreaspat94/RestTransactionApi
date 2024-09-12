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
			if (service.isBeneficiaryTableEmpty())
				service.loadDataFromBeneficiariesCsv("beneficiaries.csv");
			if (service.isAccountTableEmpty())
				service.loadDataFromAccountsCsv("accounts.csv");
			if (service.isTransactionTableEmpty())
				service.loadDataFromTransactionsCsv("transactions.csv");
		};
	}
}
