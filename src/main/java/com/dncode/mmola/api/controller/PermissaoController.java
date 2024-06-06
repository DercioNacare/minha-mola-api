package com.dncode.mmola.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dncode.mmola.api.assembler.PermissaoModelAssember;
import com.dncode.mmola.api.model.PermissaoModel;
import com.dncode.mmola.domain.repository.PermissaoRepository;

@RestController
@RequestMapping("/permissoes")
public class PermissaoController 
{
	@Autowired
	private PermissaoModelAssember permissaoModelAssembler;

	@Autowired
	private PermissaoRepository permissaoRepository;
	
	@GetMapping
	public List<PermissaoModel> listar()
	{
		return permissaoModelAssembler.toCollectionModel(permissaoRepository.findAll());
	}
}