package com.dncode.mmola.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.dncode.mmola.api.controller.PermissaoController;
import com.dncode.mmola.api.model.PermissaoModel;
import com.dncode.mmola.domain.model.Permissao;

@Component
public class PermissaoModelAssember extends RepresentationModelAssemblerSupport<Permissao, PermissaoModel>
{
	@Autowired
	private ModelMapper model;
	
	public PermissaoModelAssember() 
	{
		super(PermissaoController.class, PermissaoModel.class);
	}

	@Override
	public PermissaoModel toModel(Permissao permissao) 
	{
		return model.map(permissao, PermissaoModel.class);
	}
	
	public List<PermissaoModel> toCollectionModel(List<Permissao> permissoes)
	{
		return permissoes.stream()
				.map((permissao) -> toModel(permissao))
				.collect(Collectors.toList());
	}
}