package org.example.pasir_kurek_mikolaj.service;

import org.example.pasir_kurek_mikolaj.dto.MembershipDTO;
import org.example.pasir_kurek_mikolaj.model.Group;
import org.example.pasir_kurek_mikolaj.model.Membership;
import org.example.pasir_kurek_mikolaj.model.User;
import org.example.pasir_kurek_mikolaj.repository.GroupRepository;
import org.example.pasir_kurek_mikolaj.repository.MembershipRepository;
import org.example.pasir_kurek_mikolaj.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipService {
    private final MembershipRepository membershipRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public MembershipService(MembershipRepository membershipRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.membershipRepository = membershipRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public List<Membership> getGroupMembers(Long groupId){
        return membershipRepository.findByGroupId(groupId);
    }

    public Membership addMember(MembershipDTO membershipDTO) {
        User user = userRepository.findByEmail(membershipDTO.getUserEmail()).orElseThrow(
                () -> new EntityNotFoundException("Nie znaleziono uzytkownika o emialu: " + membershipDTO.getUserEmail())
        );

        Group group = groupRepository.findById(membershipDTO.getGroupId()).orElseThrow(
                () -> new EntityNotFoundException("Nie znaleziono grupy o ID: " + membershipDTO.getGroupId())
        );

        boolean alreadyMember = membershipRepository.findByGroupId(group.getId()).stream()
                .anyMatch(membership -> membership.getUser().getId().equals(user.getId()));

        if (alreadyMember) {
            throw new IllegalStateException("Uzytkownik jest juz czlonkiem tej grupy");
        }

        Membership membership = new Membership();
        membership.setUser(user);
        membership.setGroup(group);
        return membershipRepository.save(membership);
    }

    public void removeMember(Long membershipId){
        Membership membership = membershipRepository.findById(membershipId).orElseThrow(
                () -> new EntityNotFoundException("Czlonkostwo nie istnieje")
        );

        User currentUser = getCurrentUser();
        User groupOwner = membership.getGroup().getOwner();

        if(!currentUser.getId().equals(groupOwner.getId())){
            throw new SecurityException("Tylko wlasciciel grupy moze usuwac czlonkow");
        }

        membershipRepository.deleteById(membershipId);
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Nie znaleziono uzytkownika o email: " + email)
        );
    }
}

