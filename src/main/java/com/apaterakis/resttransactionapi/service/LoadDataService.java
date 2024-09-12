package com.apaterakis.resttransactionapi.service;

import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.repository.AccountRepository;
import com.apaterakis.resttransactionapi.repository.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class LoadDataService {
    private final BeneficiaryRepository beneficiaryRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public LoadDataService(BeneficiaryRepository beneficiaryRepository, AccountRepository accountRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.accountRepository = accountRepository;
    }
        public void loadDataFromBeneficiariesCsv(String csvFilePath) {
        try {
            BufferedReader lineReader = CreateBufferedReader(csvFilePath);
            String lineText;
            //skip the header line
            lineReader.readLine();

            while((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String firstName = data[1];
                String lastName = data[2];

                Beneficiary beneficiary = new Beneficiary(firstName, lastName);
                beneficiaryRepository.save(beneficiary);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadDataFromAccountsCsv(String csvFilePath) {
        try {
            BufferedReader lineReader = CreateBufferedReader(csvFilePath);
            String lineText;
            //skip the header line
            lineReader.readLine();

            while((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                Long beneficiaryId = Long.parseLong(data[1]);
                Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(beneficiaryId);

                if (beneficiary.isPresent()) {
                    Account account = new Account(beneficiary.get(), BigDecimal.valueOf(1000.0));
                    System.out.println("--> " + account.getBalance());
                    accountRepository.save(account);
                } else {
                    System.out.println("Beneficiary with ID " + beneficiaryId + "not found.");
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }

    }

  private BufferedReader CreateBufferedReader(String csvFilePath) throws IOException {
      ClassPathResource resource = new ClassPathResource(csvFilePath);
      InputStream inputStream = resource.getInputStream();
      return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
  }
}
