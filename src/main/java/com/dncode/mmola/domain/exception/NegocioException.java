package com.dncode.mmola.domain.exception;

public abstract class NegocioException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public NegocioException(String mensagem) {
		super(mensagem);
	}
}