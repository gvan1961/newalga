package com.algaworks.algafood.infrastructure.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries  {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Restaurante> find(String nome,
			BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		
		//Pode ser : var builder = manager.getCriteriaBuilder();
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		
		//Pode ser : var criteria = builder.createQuery(Restaurante.class);
		CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);
		
		//Pode ser : var root = criteria.from(Restaurante.class);
		Root<Restaurante> root = criteria.from(Restaurante.class);
	
		var predicates = new ArrayList<Predicate>();
		
		if(StringUtils.hasText(nome)) {
		predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
		}
		
		if(taxaFreteInicial != null) {
		predicates.add(builder
				.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial));
		}
		
		if(taxaFreteFinal != null) {
			predicates.add(builder
				.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));
		}
		
		criteria.where(predicates.toArray(new Predicate[0]));
		
		//pode ser : var query = manager.createQuery(criteria);
		TypedQuery<Restaurante> query = manager.createQuery(criteria);
		return query.getResultList();
		
	//	return manager.createQuery( criteria)				
	//			.getResultList();
		
	}
}
