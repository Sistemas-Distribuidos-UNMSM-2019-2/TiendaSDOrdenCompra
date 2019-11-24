package com.fisi.sd.transform;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fisi.sd.entity.Producto;
import com.fisi.sd.model.ProductoModel;

@Component("productoTransform")
public class ProductoTransform implements Transform<ProductoModel, Producto>{

	@Override
	public Producto transformME(ProductoModel oModel) {
		if(oModel != null) {
			Producto oProducto = new Producto();
			oProducto.setVcodigo(oModel.getnCodigo());
			oProducto.setVnombre(oModel.getsNombre());
			oProducto.setVdescripcion(oModel.getsDescripcion());
			oProducto.setNprecioUnitario(oModel.getnPrecioUnitario());
			oProducto.setNcantidad(oModel.getnCantidad());
			
			return oProducto;
		}
		return null;
	}

	@Override
	public List<Producto> transformME(List<ProductoModel> lModel) {
		if(lModel != null) {
			List<Producto> lProducto = new ArrayList<>();
			
			for(ProductoModel auxiliar : lModel) {
				lProducto.add(transformME(auxiliar));
			}
			
			return lProducto;
		}
		return null;
	}

	@Override
	public ProductoModel transformEM(Producto oEntity) {
		if(oEntity != null) {
			ProductoModel oProductoModel = new ProductoModel();
			oProductoModel.setnCodigo(oEntity.getVcodigo());
			oProductoModel.setsNombre(oEntity.getVnombre());
			oProductoModel.setsDescripcion(oEntity.getVdescripcion());
			oProductoModel.setnCantidad(oEntity.getNcantidad());
			oProductoModel.setnPrecioUnitario(oEntity.getNprecioUnitario());
			
			return oProductoModel;
		}
		return null;
	}

	@Override
	public List<ProductoModel> transformEM(List<Producto> lEntity) {
		if(lEntity != null) {
			List<ProductoModel> lProductoModel = new ArrayList<>();
			
			for(Producto auxiliar : lEntity) {
				lProductoModel.add(transformEM(auxiliar));
			}
			
			return lProductoModel;
		}
		return null;
	}

}
