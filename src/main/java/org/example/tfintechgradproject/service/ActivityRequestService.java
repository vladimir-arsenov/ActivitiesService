package org.example.tfintechgradproject.service;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.repository.ActivityRequestRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityRequestService {
    private final ActivityRequestRepository activityRequestRepository;



}
