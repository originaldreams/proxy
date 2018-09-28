package com.originaldreams.proxycenter.service;

import com.originaldreams.proxycenter.dto.HttpParametersDTO;
import org.springframework.http.ResponseEntity;

public interface HttpService {

    ResponseEntity<?> get(HttpParametersDTO httpParametersDTO, String token);

    ResponseEntity<?> post(HttpParametersDTO httpParametersDTO, String token);

    ResponseEntity<?> put(HttpParametersDTO httpParametersDTO, String token);

    ResponseEntity<?> delete(HttpParametersDTO httpParametersDTO, String token);

}
