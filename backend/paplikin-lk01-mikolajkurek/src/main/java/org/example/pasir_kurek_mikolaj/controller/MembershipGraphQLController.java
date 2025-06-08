package org.example.pasir_kurek_mikolaj.controller;

import org.example.pasir_kurek_mikolaj.dto.GroupResponseDTO;
import org.example.pasir_kurek_mikolaj.dto.GroupTransactionDTO;
import org.example.pasir_kurek_mikolaj.dto.MembershipDTO;
import org.example.pasir_kurek_mikolaj.dto.MembershipResponseDTO;
import org.example.pasir_kurek_mikolaj.model.Group;
import org.example.pasir_kurek_mikolaj.model.Membership;
import org.example.pasir_kurek_mikolaj.model.User;
import org.example.pasir_kurek_mikolaj.repository.GroupRepository;
import org.example.pasir_kurek_mikolaj.repository.MembershipRepository;
import org.example.pasir_kurek_mikolaj.service.MembershipService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MembershipGraphQLController {
    private final MembershipService membershipService;
    private final MembershipRepository membershipRepository;
    private final GroupRepository groupRepository;

    public MembershipGraphQLController(MembershipService membershipService, MembershipRepository membershipRepository, GroupRepository groupRepository) {
        this.membershipService = membershipService;
        this.membershipRepository = membershipRepository;
        this.groupRepository = groupRepository;
    }

    @QueryMapping
    public List<MembershipResponseDTO> groupMembers(@Argument Long groupId){
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new IllegalArgumentException("Nie znaleziono grupy o ID: " + groupId)
        );

        return membershipRepository.findByGroupId(group.getId()).stream()
                .map(membership -> new MembershipResponseDTO(
                        membership.getId(),
                        membership.getUser().getId(),
                        membership.getGroup().getId(),
                        membership.getUser().getEmail()
                )).toList();
    }

    @MutationMapping
    public MembershipResponseDTO addMember(@Argument MembershipDTO membershipDTO) {
        Membership membership = membershipService.addMember(membershipDTO);
        return new MembershipResponseDTO(
                membership.getId(),
                membership.getUser().getId(),
                membership.getGroup().getId(),
                membership.getUser().getEmail()
        );
    }

    @QueryMapping
    public List<GroupResponseDTO> myGroups(){
        User user = membershipService.getCurrentUser();
        return groupRepository.findByMemberships_User(user).stream()
                .map(group -> new GroupResponseDTO(
                        group.getId(),
                        group.getName(),
                        group.getOwner().getId()
                )).toList();
    }

    @MutationMapping
    public Boolean removeMember(@Argument Long membershipId) {
        membershipService.removeMember(membershipId);
        return true;
    }
}
