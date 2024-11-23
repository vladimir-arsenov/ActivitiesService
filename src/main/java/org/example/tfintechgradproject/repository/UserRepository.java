package org.example.tfintechgradproject.repository;

import org.example.tfintechgradproject.model.Activity;
import org.example.tfintechgradproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
