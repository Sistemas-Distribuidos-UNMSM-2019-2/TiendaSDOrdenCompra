package com.fisi.sd.entity;
// Generated 22/11/2019 01:09:25 PM by Hibernate Tools 4.0.1.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Ordencompra generated by hbm2java
 */
@Entity
@Table(name = "ordencompra", catalog = "sdtienda")
public class Ordencompra implements java.io.Serializable {

	private Integer vidOrdenCompra;
	private Cliente cliente;
	private Double nprecioTotal;
	private Date dfechaCompra;
	private Date dfechaPago;
	private Set<Factura> facturas = new HashSet<Factura>(0);
	private Set<OrdencompraHasProducto> ordencompraHasProductos = new HashSet<OrdencompraHasProducto>(0);

	public Ordencompra() {
	}

	public Ordencompra(Cliente cliente) {
		this.cliente = cliente;
	}

	public Ordencompra(Cliente cliente, Double nprecioTotal, Date dfechaCompra, Date dfechaPago, Set facturas,
			Set ordencompraHasProductos) {
		this.cliente = cliente;
		this.nprecioTotal = nprecioTotal;
		this.dfechaCompra = dfechaCompra;
		this.dfechaPago = dfechaPago;
		this.facturas = facturas;
		this.ordencompraHasProductos = ordencompraHasProductos;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "vIdOrdenCompra", unique = true, nullable = false)
	public Integer getVidOrdenCompra() {
		return this.vidOrdenCompra;
	}

	public void setVidOrdenCompra(Integer vidOrdenCompra) {
		this.vidOrdenCompra = vidOrdenCompra;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Cliente_vRUC", nullable = false)
	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Column(name = "nPrecioTotal", precision = 22, scale = 0)
	public Double getNprecioTotal() {
		return this.nprecioTotal;
	}

	public void setNprecioTotal(Double nprecioTotal) {
		this.nprecioTotal = nprecioTotal;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "dFechaCompra", length = 10)
	public Date getDfechaCompra() {
		return this.dfechaCompra;
	}

	public void setDfechaCompra(Date dfechaCompra) {
		this.dfechaCompra = dfechaCompra;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "dFechaPago", length = 10)
	public Date getDfechaPago() {
		return this.dfechaPago;
	}

	public void setDfechaPago(Date dfechaPago) {
		this.dfechaPago = dfechaPago;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ordencompra")
	public Set<Factura> getFacturas() {
		return this.facturas;
	}

	public void setFacturas(Set<Factura> facturas) {
		this.facturas = facturas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ordencompra")
	public Set<OrdencompraHasProducto> getOrdencompraHasProductos() {
		return this.ordencompraHasProductos;
	}

	public void setOrdencompraHasProductos(Set<OrdencompraHasProducto> ordencompraHasProductos) {
		this.ordencompraHasProductos = ordencompraHasProductos;
	}

}
