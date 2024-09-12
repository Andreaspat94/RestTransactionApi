package com.apaterakis.resttransactionapi.service;

import com.apaterakis.resttransactionapi.exception.InsufficientBalanceException;
import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.model.Transaction;
import com.apaterakis.resttransactionapi.model.TransactionType;
import com.apaterakis.resttransactionapi.repository.AccountRepository;
import com.apaterakis.resttransactionapi.repository.BeneficiaryRepository;
import com.apaterakis.resttransactionapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class LoadDataService {
    private final BeneficiaryRepository beneficiaryRepository;
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    @Autowired
    public LoadDataService(BeneficiaryRepository beneficiaryRepository, AccountRepository accountRepository, TransactionService transactionService, TransactionRepository transactionRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    public void loadDataFromBeneficiariesCsv(String csvFilePath) {
        try {
            BufferedReader lineReader = CreateBufferedReader(csvFilePath);
            String lineText;
            //skip the header line
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null) {
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

            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                Long beneficiaryId = Long.parseLong(data[1]);
                Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(beneficiaryId);

                if (beneficiary.isPresent()) {
                    Account account = new Account(beneficiary.get(), BigDecimal.valueOf(5000.0));
                    accountRepository.save(account);
                } else {
                    System.out.println("Beneficiary with ID " + beneficiaryId + "not found.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedReader CreateBufferedReader(String csvFilePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(csvFilePath);
        InputStream inputStream = resource.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    public void loadDataFromTransactionsCsv(String csvFilePath) {
        try {
            BufferedReader lineReader = CreateBufferedReader(csvFilePath);
            String lineText;
            //skip the header line
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null) {

                String[] data = lineText.split(",");
                Long accountId = Long.parseLong(data[1]);
                Optional<Account> account = accountRepository.findById(accountId);

                if (account.isPresent()) {
                    BigDecimal amount = new BigDecimal(data[2]);
                    TransactionType type = TransactionType.valueOf(data[3].toUpperCase());

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
                    LocalDate date = LocalDate.parse(data[4], formatter);
                    Transaction transaction = new Transaction(account.get(), amount, type, date);
                    Transaction transactionSucceed = transactionService.makeTransaction(transaction);

                    BigDecimal result;
                    if (transactionSucceed.getType().equals(TransactionType.WITHDRAWAL)) {
                        result = account.get().getBalance().subtract(transaction.getAmount());
                    } else {
                        result = account.get().getBalance().add(transaction.getAmount());
                    }
                    account.get().setBalance(result);
                    accountRepository.save(account.get());

                } else {
                    System.out.println("There is no account with this ID: " + accountId);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isBeneficiaryTableEmpty() {
        return beneficiaryRepository.count() == 0;
    }

    public boolean isAccountTableEmpty() {
        return accountRepository.count() == 0;
    }

    public boolean isTransactionTableEmpty() {
        return transactionRepository.count() == 0;
    }
}
