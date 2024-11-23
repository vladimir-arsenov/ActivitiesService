package org.example.tfintechgradproject.repository;

import org.example.tfintechgradproject.model.Activity;
import org.example.tfintechgradproject.model.ActivityRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRequestRepository extends JpaRepository<ActivityRequest, Long> {
}
