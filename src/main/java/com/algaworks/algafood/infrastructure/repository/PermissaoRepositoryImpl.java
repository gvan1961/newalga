package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.PermissaoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class PermissaoRepositoryImpl implements PermissaoRepository {

	 @PersistenceContext
     private EntityManager manager;
	
	@Override
	public List<Permissao> listar() {
		return manager.createQuery("from Permissao", Permissao.class)
                .getResultList();
	}

	@Override
	public Permissao buscar(Long id) {
		return manager.find(Permissao.class, id);
	}

	@Transactional
	@Override
	public Permissao salvar(Permissao permissao) {
		return manager.merge(permissao);
	}

	@Transactional
	@Override
	public void remover(Permissao permissao) {
		 permissao = buscar(permissao.getId());
         manager.remove(permissao);
		
	}
	

}
