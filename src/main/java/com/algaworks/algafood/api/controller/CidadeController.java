package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping(value = "/cidades")
public class CidadeController {
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroCidadeService cadastroCidade;
	
	@GetMapping
	public List<Cidade> listar() {
		return cidadeRepository.findAll();
	}
	
	@GetMapping("/{cidadeId}")
	public Cidade buscar(@PathVariable Long cidadeId) {
		return cadastroCidade.buscarOuFalhar(cidadeId);
		}	
	
		
		
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cidade adicionar(@RequestBody Cidade cidade) {
		try {
			return cadastroCidade.salvar(cidade);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	  
	  @PutMapping("/{cidadeId}")
		public Cidade atualizar(@PathVariable Long cidadeId,
				@RequestBody Cidade cidade) {
			Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);
			
			BeanUtils.copyProperties(cidade, cidadeAtual, "id");
			
			try {
				return cadastroCidade.salvar(cidadeAtual);
			} catch (EntidadeNaoEncontradaException e) {
				throw new NegocioException(e.getMessage());
			}
		}
	
	  @DeleteMapping("/{cidadeId}")
		public ResponseEntity<?> remover(@PathVariable Long cidadeId) {
			try {
				cadastroCidade.excluir(cidadeId);	
				return ResponseEntity.noContent().build();
				
			} catch (EntidadeNaoEncontradaException e) {
				return ResponseEntity.notFound().build();
				
			} catch (EntidadeEmUsoException e) {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body(e.getMessage());
			}
		}
	

}
