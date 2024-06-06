package com.dncode.mmola.api.controller.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.dncode.mmola.api.controller.handler.util.Problem;
import com.dncode.mmola.api.controller.handler.util.ProblemType;
import com.dncode.mmola.domain.exception.EntidadeNaoEncontradaException;
import com.dncode.mmola.domain.exception.NegocioException;


@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler 

{
	private static final String MSG_ERRO_GENERICO_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. tente novamente e se o problema persistir, entre em contacto com o administrador do sistema";

	@Autowired
	private MessageSource messageSource;
		
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) 
	{
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		
		String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correcto e tente novamente";
		
		List<Problem.Field> problemFields = ex.getBindingResult().getFieldErrors()
				.stream()
				.map(fieldError -> 
					{
						String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
						
						return Problem.Field.builder()
						.name(fieldError.getField())
						.userMessage(message)
						.build();
					})
				.collect(Collectors.toList());
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.fields(problemFields)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) 
	{
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		Problem problem = createProblemBuilder(status, problemType, MSG_ERRO_GENERICO_USUARIO_FINAL).build();
	
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request)
	{
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ProblemType problemType = ProblemType.ERRO_INTERNO_SISTEMA;
		
		Problem problem = createProblemBuilder(status, problemType,MSG_ERRO_GENERICO_USUARIO_FINAL)
				.userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL)
				.build();
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) 
	{
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		
		String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correcto e tente novamente";
		
		List<Problem.Field> problemFields = ex.getBindingResult().getFieldErrors()
				.stream()
				.map(fieldError -> 
					{
						String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
						return Problem.Field.builder()
						.name(fieldError.getField())
						.userMessage(message)
						.build();
					})
				.collect(Collectors.toList());
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.fields(problemFields)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request)
	{
		HttpStatus status = HttpStatus.NOT_FOUND;
		String detail = ex.getMessage();
		
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocio(NegocioException ex, WebRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	private Problem.ProblemBuilder createProblemBuilder(HttpStatusCode status, ProblemType problemType, String detail)
	{
		return Problem.builder()
				.timestamp(LocalDateTime.now())
				.status(status.value())
				.detail(detail)
				.type(problemType.getUri())
				.title(problemType.getTitle());
	}

}