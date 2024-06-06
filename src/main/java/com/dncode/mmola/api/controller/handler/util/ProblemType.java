package com.dncode.mmola.api.controller.handler.util;

import lombok.Getter;

@Getter
public enum ProblemType 
{
	PARAMETRO_INVALIDO("/parametro-invalido","Parâmetro inválido"),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	ERRO_INTERNO_SISTEMA("/erro-interno-sistena","Erro interno de Sistema"),
	DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos");
	
	private String uri;
	private String title;
	
	private ProblemType(String path, String title) 
	{
		this.uri = "https://mmola.co.mz" + path;
		this.title = title;
	}
}