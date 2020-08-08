/**
 * 
 */
package com.udacity.jwdnd.course1.cloudstorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Priority;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@RestController
@Priority(1)
public class CustomResponseEntityExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	protected ModelAndView handleNotFoundException(EntityNotFoundException ex, WebRequest request) {
		ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage());
		return buildResponse(apiError);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ModelAndView handleRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
		ApiError error = new ApiError( NOT_ACCEPTABLE, "Request method is not supported on the requested API", ex);
		return buildResponse(error);
	}

	@ExceptionHandler(RuntimeException.class)
	protected ModelAndView handleRuntimeException(HttpRequestMethodNotSupportedException ex) {
		ApiError error = new ApiError( INTERNAL_SERVER_ERROR, "Internal Server Error while performing the requested operation", ex);
		return buildResponse(error);
	}

	@ExceptionHandler(FileStorageException.class)
	protected ModelAndView handleFileStorageException(FileStorageException ex) {
		ApiError errorDetails = new ApiError(HttpStatus.NOT_ACCEPTABLE, ex.getMessage(), ex);
		return buildResponse(errorDetails);
	}

	private ModelAndView buildResponse(ApiError apiError) {
		ModelAndView model = new ModelAndView();
		model.addObject("apiError", apiError);
		model.setViewName("error");
		return model;
	}
}
