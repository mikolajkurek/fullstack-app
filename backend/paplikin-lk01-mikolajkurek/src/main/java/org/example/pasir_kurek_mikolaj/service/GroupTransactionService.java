package org.example.pasir_kurek_mikolaj.service;

import org.example.pasir_kurek_mikolaj.dto.GroupDTO;
import org.example.pasir_kurek_mikolaj.dto.GroupTransactionDTO;
import org.example.pasir_kurek_mikolaj.dto.TransactionDTO;
import org.example.pasir_kurek_mikolaj.model.*;
import org.example.pasir_kurek_mikolaj.repository.DebtRepository;
import org.example.pasir_kurek_mikolaj.repository.GroupRepository;
import org.example.pasir_kurek_mikolaj.repository.MembershipRepository;
import org.example.pasir_kurek_mikolaj.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupTransactionService {

    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final DebtRepository debtRepository;
    private final TransactionService transactionService;

    public GroupTransactionService(GroupRepository groupRepository, MembershipRepository membershipRepository, DebtRepository debtRepository, TransactionService transactionService) {
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
        this.debtRepository = debtRepository;
        this.transactionService = transactionService;
    }

    public void addGroupTransaction(GroupTransactionDTO dto, User currentUser) {
        Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono grupy"));

        List<Membership> members = membershipRepository.findByGroupId(group.getId());
        List<Long>selectedUserIds=dto.getSelectedUserIds();

        if (selectedUserIds ==null || selectedUserIds.isEmpty()) {
            throw new IllegalArgumentException("Nie wybrano żadnych użytkowników!");
        }

        double amountPerUser = dto.getAmount() / members.size();

        for (Membership member : members) {
            User debtor = member.getUser();
            if (!debtor.getId().equals(currentUser.getId()) && selectedUserIds.contains(debtor.getId())) {
                Debt debt = new Debt();
                debt.setDebtor(debtor);
                debt.setCreditor(currentUser);
                debt.setAmount(amountPerUser);
                debt.setGroup(group);
                debt.setTitle(dto.getTitle());
                transactionService.saveTransactions(
                        new TransactionDTO(
                                amountPerUser,
                                TransactionType.EXPENSE,
                                dto.getTitle(),
                                "GROUP",
                                LocalDateTime.now()
                        )
                );
                debtRepository.save(debt);
            }
        }
    }
}




