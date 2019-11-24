package com.fisi.sd.service;

import java.util.List;

import com.fisi.sd.model.ProductoModel;

public interface ProductoServicio {
	public List<ProductoModel> listarProductos();
	
	public ProductoModel buscarProducto(int nCodigoProducto);
}
