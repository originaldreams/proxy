package com.originaldreams.proxycenter.service;

import org.springframework.http.ResponseEntity;

public interface VerificationService {

    ResponseEntity<?> sendVerificationCode(String phone);
}
