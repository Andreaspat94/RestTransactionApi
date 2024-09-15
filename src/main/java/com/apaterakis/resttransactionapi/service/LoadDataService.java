package com.apaterakis.resttransactionapi.service;

import com.apaterakis.resttransactionapi.exception.NotFoundException;
import com.apaterakis.resttransactionapi.model.Account;
import com.apaterakis.resttransactionapi.model.Beneficiary;
import com.apaterakis.resttransactionapi.model.Transaction;
import com.apaterakis.resttransactionapi.model.TransactionType;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class LoadDataService {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final BeneficiaryService beneficiaryService;


    @Autowired
    public LoadDataService(AccountService accountService, TransactionService transactionService, BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
        this.accountService = accountService;
        this.transactionService = transactionService;
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
                beneficiaryService.save(beneficiary);
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
                Optional<Beneficiary> beneficiary = beneficiaryService.findById(beneficiaryId);

                if (beneficiary.isPresent()) {
                    Account account = new Account(beneficiary.get(), BigDecimal.valueOf(5000.0));
                    accountService.save(account);
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

    /**
     * First add the data to an ArrayList, then sort the array based on date, and finally store the data to db.
     */
    public void loadDataFromTransactionsCsv(String csvFilePath) {
        try {
            BufferedReader lineReader = CreateBufferedReader(csvFilePath);
            String lineText;

            List<Transaction> transactions = new ArrayList<>();
            //skip the header line
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null) {

                String[] data = lineText.split(",");
                Long accountId = Long.parseLong(data[1]);
                Optional<Account> optionalAccount = accountService.findById(accountId);

                if (optionalAccount.isPresent()) {

                    // parsing data from csv line
                    BigDecimal amount = new BigDecimal(data[2]);
                    TransactionType type = TransactionType.valueOf(data[3].toUpperCase());

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
                    LocalDate date = LocalDate.parse(data[4], formatter);
                    Transaction transaction = new Transaction(accountId, amount, type, date);

                    // add to list
                    transactions.add(transaction);

                } else {
                    System.out.println("There is no account with this ID: " + accountId);
                }
            }

            // sort the transactions by date (oldest first)
            transactions.sort(Comparator.comparing(Transaction::getDate));

            //Process the sorted transactions
            for (Transaction transaction : transactions) {
                Long accountId = transaction.getAccountId();

                Optional<Account> optionalAccount = accountService.findById(accountId);
                if (optionalAccount.isEmpty()) {
                    throw new NotFoundException("This transaction with id: " + transaction.getTransactionId() + " has no account Id.");
                }

                //proceed to transaction
                transactionService.proceedTransaction(transaction,optionalAccount.get());
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isBeneficiaryTableEmpty() {
        return beneficiaryService.isBeneficiaryTableEmpty();
    }

    public boolean isAccountTableEmpty() {
        return accountService.isAccountTableEmpty();
    }

    public boolean isTransactionTableEmpty() {
        return transactionService.isTransactionTableEmpty();
    }
}
