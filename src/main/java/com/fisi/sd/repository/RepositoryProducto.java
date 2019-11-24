package com.fisi.sd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fisi.sd.entity.Producto;

@Repository("productoRepository")
public interface RepositoryProducto extends JpaRepository<Producto, Integer>{
	public Producto findByVcodigo(Integer vcodigo);
}
