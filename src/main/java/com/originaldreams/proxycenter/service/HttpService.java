package com.originaldreams.proxycenter.service;

import com.originaldreams.proxycenter.dto.HttpParametersDTO;
import org.springframework.http.ResponseEntity;

public interface HttpService {

    ResponseEntity<?> get(HttpParametersDTO httpParametersDTO);

    ResponseEntity<?> post(HttpParametersDTO httpParametersDTO);

    ResponseEntity<?> put(HttpParametersDTO httpParametersDTO);

    ResponseEntity<?> delete(HttpParametersDTO httpParametersDTO);

}
