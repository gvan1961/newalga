package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;




@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@GetMapping
	public List<Restaurante> listar() {
		List<Restaurante> restaurantes = restauranteRepository.findAll();	
		
				
		return restaurantes;
	}
	
	@GetMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {
		Optional<Restaurante> restaurante = restauranteRepository.findById(restauranteId);
		
		if (restaurante.isPresent()) {
			return ResponseEntity.ok(restaurante.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Restaurante adicionar(@RequestBody Restaurante restaurante) {
		return cadastroRestaurante.salvar(restaurante);
	}
	
	/*
	@PutMapping("/{restauranteId}")
	    public ResponseEntity<?> atualizar(@PathVariable Long restauranteId,
	        @RequestBody Optional<Restaurante> restauranteAtual2) {
	        try {
				Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);
				
				if (restauranteAtual.isPresent()) {
					BeanUtils.copyProperties(restauranteAtual2, restauranteAtual.get(),
							"id", "formasPagamento", "endereco");
					
					Restaurante restauranteSalvo = cadastroRestaurante.salvar(restauranteAtual.get());
					return ResponseEntity.ok(restauranteSalvo);
				}
				
				return ResponseEntity.notFound().build();
			
			} catch (EntidadeNaoEncontradaException e) {
				return ResponseEntity.badRequest()
						.body(e.getMessage());
			}
	    }
	    
	    */
	
	@PutMapping("/{restauranteId}")
	public ResponseEntity<?> atualizar(@PathVariable Long restauranteId,
			@RequestBody Optional<Restaurante> restauranteAtual2) {
		try {
			Restaurante restauranteAtual = restauranteRepository
					.findById(restauranteId).orElse(null);
			
			if (restauranteAtual != null) {
				BeanUtils.copyProperties(restauranteAtual2, restauranteAtual, 
						"id", "formasPagamento", "endereco", "dataCadastro", "produtos");
				
				restauranteAtual = cadastroRestaurante.salvar(restauranteAtual);
				return ResponseEntity.ok(restauranteAtual);
			}
			
			return ResponseEntity.notFound().build();
		
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest()
					.body(e.getMessage());
		}
	}
	
	
	  
	  @PatchMapping("/{restauranteId}")
	  public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId,
			  @RequestBody Map<String, Object> campos) {
		  
		 Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);
		  
		 if (restauranteAtual.isPresent()) {
			 return ResponseEntity.notFound().build();
		 }
		 
		  merge(campos, restauranteAtual);
		  
		   return atualizar(restauranteId, restauranteAtual);
		  }

	private void merge(Map<String, Object> dadosOrigem, Optional<Restaurante> restauranteAtual) {
		ObjectMapper  objectMapper = new ObjectMapper();
		Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
		
		System.out.println(restauranteOrigem);
		
		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
			field.setAccessible(true);
			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
			
			System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
			
			ReflectionUtils.setField(field, restauranteAtual, novoValor);
		  });
	}
	  
	  
	  
	  @DeleteMapping("/{restauranteId}")
		public ResponseEntity<?> remover(@PathVariable Long restauranteId) {
			try {
				cadastroRestaurante.excluir(restauranteId);	
				return ResponseEntity.noContent().build();
				
			} catch (EntidadeNaoEncontradaException e) {
				return ResponseEntity.notFound().build();
				
			} catch (EntidadeEmUsoException e) {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body(e.getMessage());
			}
		}
	
	  
	
}