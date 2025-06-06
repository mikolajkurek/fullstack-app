package org.example.pasir_kurek_mikolaj.controller;


import org.example.pasir_kurek_mikolaj.dto.TransactionDTO;
import org.example.pasir_kurek_mikolaj.model.Transaction;
import org.example.pasir_kurek_mikolaj.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @PostMapping
    public ResponseEntity<Transaction> saveTransactions(@Valid @RequestBody TransactionDTO transactionDetails) {
        Transaction transaction = transactionService.saveTransactions(transactionDetails);
        return ResponseEntity.ok(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionDTO transactionDetails) {
        Transaction transaction = transactionService.updateTransaction(id, transactionDetails);
        return ResponseEntity.ok(transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Transaction> removeTransaction(@PathVariable Long id) {
        Transaction transaction = transactionService.removeTransaction(id);
        return ResponseEntity.ok(transaction);
    }

}
