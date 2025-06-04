package org.example.pasir_kurek_mikolaj.repository;

import org.example.pasir_kurek_mikolaj.model.Group;
import org.example.pasir_kurek_mikolaj.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByMemberships_User(User user);
}