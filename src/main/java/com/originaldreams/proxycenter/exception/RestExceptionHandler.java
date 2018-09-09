package com.originaldreams.proxycenter.exception;


import com.originaldreams.common.exception.BadRequestException;
import com.originaldreams.common.exception.ResourceNotFoundException;
import com.originaldreams.common.response.MyServiceResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 自定义异常
 * @author magaofei
 */
@ControllerAdvice
@RestController
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String NOT_FOUND_MESSAGE = "你访问的资源不存在";
    private static final String BAD_REQUEST_MESSAGE = "参数错误";

    private static ResponseEntity<Object> buildBadRequestEntity(Exception ex, WebRequest request) {
        String message = ex.getMessage();
        if (message == null) {
            message = BAD_REQUEST_MESSAGE;
        }

        return new ResponseEntity<>(
                new MyServiceResponse(MyServiceResponse.SUCCESS_CODE_FAILED, message),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildBadRequestEntity(ex, request);
    }


    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildBadRequestEntity(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildBadRequestEntity(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildBadRequestEntity(ex,request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildBadRequestEntity(ex, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        String message = ex.getMessage();
        if (message == null) {
            message = NOT_FOUND_MESSAGE;
        }

        return new ResponseEntity<>(
                new MyServiceResponse(MyServiceResponse.SUCCESS_CODE_FAILED, message),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleBadRequestException(Exception ex, WebRequest request) {
        return buildBadRequestEntity(ex, request);
    }





    @ExceptionHandler(Exception.class)
    public ResponseEntity<MyServiceResponse> handleAllExceptions(Exception ex, WebRequest request) {

        MyServiceResponse errorDetails = new MyServiceResponse(
                MyServiceResponse.SUCCESS_CODE_FAILED,
                ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
