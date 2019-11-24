package com.fisi.sd.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fisi.sd.model.ProductoModel;
import com.fisi.sd.repository.RepositoryProducto;
import com.fisi.sd.service.ProductoServicio;
import com.fisi.sd.transform.ProductoTransform;

@Service("productoService")
public class ProductoServicioImpl implements ProductoServicio {
	@Autowired
	@Qualifier("productoRepository")
	private RepositoryProducto productoRepository;
	@Autowired
	@Qualifier("productoTransform")
	private ProductoTransform productoTransform;

	@Override
	public List<ProductoModel> listarProductos() {
		return productoTransform.transformEM(productoRepository.findAll());
	}

	@Override
	public ProductoModel buscarProducto(int nCodigoProducto) {
		return productoTransform.transformEM(productoRepository.findByVcodigo(nCodigoProducto));
	}

}
