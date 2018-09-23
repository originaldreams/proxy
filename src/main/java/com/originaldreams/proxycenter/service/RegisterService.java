package com.originaldreams.proxycenter.service;

import com.originaldreams.proxycenter.dto.RegisterDTO;
import org.springframework.http.ResponseEntity;

public interface RegisterService {

    ResponseEntity<?> register(RegisterDTO registerDTO);
}
