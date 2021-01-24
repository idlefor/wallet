package com.wallet.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Custom class that extends base class {@link ResponseEntityExceptionHandler}
 * class that wish to provide exception handling on wallet rest controller
 * operation
 * 
 * @author Iden.Teo
 * 
 */
@RestController
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler
	public final ResponseEntity<?> handleWalletException(final WalletException ex, final WebRequest request) {
		WalletExceptionResponse response = new WalletExceptionResponse(ex.getMessage());
		return new ResponseEntity<WalletExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}
}
