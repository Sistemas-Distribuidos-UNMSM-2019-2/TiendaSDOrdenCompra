package com.fisi.sd.entity;
// Generated 22/11/2019 01:09:25 PM by Hibernate Tools 4.0.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * OrdencompraHasProducto generated by hbm2java
 */
@Entity
@Table(name = "ordencompra_has_producto", catalog = "sdtienda")
public class OrdencompraHasProducto implements java.io.Serializable {

	private OrdencompraHasProductoId id;
	private Producto producto;
	private Ordencompra ordencompra;
	private Integer ncantidad;
	private Double ntotal;

	public OrdencompraHasProducto() {
	}

	public OrdencompraHasProducto(OrdencompraHasProductoId id, Producto producto, Ordencompra ordencompra) {
		this.id = id;
		this.producto = producto;
		this.ordencompra = ordencompra;
	}

	public OrdencompraHasProducto(OrdencompraHasProductoId id, Producto producto, Ordencompra ordencompra,
			Integer ncantidad, Double ntotal) {
		this.id = id;
		this.producto = producto;
		this.ordencompra = ordencompra;
		this.ncantidad = ncantidad;
		this.ntotal = ntotal;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "ordenCompraVIdOrdenCompra", column = @Column(name = "OrdenCompra_vIdOrdenCompra", nullable = false)),
			@AttributeOverride(name = "productoVCodigo", column = @Column(name = "Producto_vCodigo", nullable = false)) })
	public OrdencompraHasProductoId getId() {
		return this.id;
	}

	public void setId(OrdencompraHasProductoId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Producto_vCodigo", nullable = false, insertable = false, updatable = false)
	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OrdenCompra_vIdOrdenCompra", nullable = false, insertable = false, updatable = false)
	public Ordencompra getOrdencompra() {
		return this.ordencompra;
	}

	public void setOrdencompra(Ordencompra ordencompra) {
		this.ordencompra = ordencompra;
	}

	@Column(name = "nCantidad")
	public Integer getNcantidad() {
		return this.ncantidad;
	}

	public void setNcantidad(Integer ncantidad) {
		this.ncantidad = ncantidad;
	}

	@Column(name = "nTotal", precision = 22, scale = 0)
	public Double getNtotal() {
		return this.ntotal;
	}

	public void setNtotal(Double ntotal) {
		this.ntotal = ntotal;
	}

}
