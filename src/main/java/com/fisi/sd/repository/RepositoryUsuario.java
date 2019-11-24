package com.fisi.sd.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fisi.sd.entity.Usuario;

@Repository("userRepository")
public interface RepositoryUsuario extends JpaRepository<Usuario, String>{
	public Usuario findByVidUsuario(String vidUsuario);
}
