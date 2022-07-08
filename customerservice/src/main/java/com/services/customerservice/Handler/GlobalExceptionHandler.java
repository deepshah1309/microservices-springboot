package com.services.customerservice.Handler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.services.customerservice.Error.ErrorMessage;



@RestControllerAdvice
@EnableWebMvc
public class GlobalExceptionHandler {
	

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> simcardValidation(Exception e){
		
		ErrorMessage e1 = new ErrorMessage();

		
		e1.setStatusCode(HttpStatus.BAD_REQUEST.value());
		
		e1.setMessage(e.getMessage());
		
		e.printStackTrace();
		System.out.println("VALUE2");
		
		return ResponseEntity.badRequest().body(e1);
		
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> simcardValidation(MethodArgumentNotValidException e){
		
		ErrorMessage e1 = new ErrorMessage();
         e.printStackTrace();
		String bindMsg = "";
		for(ObjectError err:e.getAllErrors()) {
			  bindMsg += err.getDefaultMessage()+",";
		}
		e1.setStatusCode(HttpStatus.BAD_REQUEST.value());
		
		e1.setMessage(bindMsg);
		System.out.println("VALUE3");
		return ResponseEntity.badRequest().body(e1);
		
	}


}
