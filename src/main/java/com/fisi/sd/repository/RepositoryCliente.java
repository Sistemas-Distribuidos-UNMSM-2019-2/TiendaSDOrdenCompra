package com.fisi.sd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fisi.sd.entity.Cliente;

@Repository("clienteRepository")
public interface RepositoryCliente extends JpaRepository<Cliente, String>{
	public abstract Cliente findByVruc(String vruc);
}
