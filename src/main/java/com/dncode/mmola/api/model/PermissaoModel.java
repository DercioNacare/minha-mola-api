package com.dncode.mmola.api.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissaoModel extends RepresentationModel<PermissaoModel>
{
	private Long codigo;
	private String nome;
	private String descricao;
}
