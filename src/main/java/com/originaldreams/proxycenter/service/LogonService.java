package com.originaldreams.proxycenter.service;

import com.originaldreams.proxycenter.dto.LogonDTO;
import org.springframework.http.ResponseEntity;

public interface LogonService {

    ResponseEntity<?> logon(LogonDTO logonDTO);
}
