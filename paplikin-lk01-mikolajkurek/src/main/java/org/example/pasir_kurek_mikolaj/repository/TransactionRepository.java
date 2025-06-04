package org.example.pasir_kurek_mikolaj.repository;

import org.example.pasir_kurek_mikolaj.model.Transaction;
import org.example.pasir_kurek_mikolaj.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUser(User user);

    List<Transaction> findByUser(User user);
}