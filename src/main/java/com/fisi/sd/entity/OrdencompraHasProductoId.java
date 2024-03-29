package com.fisi.sd.entity;
// Generated 22/11/2019 01:09:25 PM by Hibernate Tools 4.0.1.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * OrdencompraHasProductoId generated by hbm2java
 */
@Embeddable
public class OrdencompraHasProductoId implements java.io.Serializable {

	private int ordenCompraVIdOrdenCompra;
	private int productoVCodigo;

	public OrdencompraHasProductoId() {
	}

	public OrdencompraHasProductoId(int ordenCompraVIdOrdenCompra, int productoVCodigo) {
		this.ordenCompraVIdOrdenCompra = ordenCompraVIdOrdenCompra;
		this.productoVCodigo = productoVCodigo;
	}

	@Column(name = "OrdenCompra_vIdOrdenCompra", nullable = false)
	public int getOrdenCompraVIdOrdenCompra() {
		return this.ordenCompraVIdOrdenCompra;
	}

	public void setOrdenCompraVIdOrdenCompra(int ordenCompraVIdOrdenCompra) {
		this.ordenCompraVIdOrdenCompra = ordenCompraVIdOrdenCompra;
	}

	@Column(name = "Producto_vCodigo", nullable = false)
	public int getProductoVCodigo() {
		return this.productoVCodigo;
	}

	public void setProductoVCodigo(int productoVCodigo) {
		this.productoVCodigo = productoVCodigo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof OrdencompraHasProductoId))
			return false;
		OrdencompraHasProductoId castOther = (OrdencompraHasProductoId) other;

		return (this.getOrdenCompraVIdOrdenCompra() == castOther.getOrdenCompraVIdOrdenCompra())
				&& (this.getProductoVCodigo() == castOther.getProductoVCodigo());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getOrdenCompraVIdOrdenCompra();
		result = 37 * result + this.getProductoVCodigo();
		return result;
	}

}
