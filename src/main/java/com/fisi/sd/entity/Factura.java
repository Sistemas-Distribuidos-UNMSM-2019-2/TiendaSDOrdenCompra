package com.fisi.sd.entity;
// Generated 22/11/2019 01:09:25 PM by Hibernate Tools 4.0.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Factura generated by hbm2java
 */
@Entity
@Table(name = "factura", catalog = "sdtienda")
public class Factura implements java.io.Serializable {

	private Integer vidFactura;
	private Ordencompra ordencompra;
	private Date dfecha;

	public Factura() {
	}

	public Factura(Ordencompra ordencompra) {
		this.ordencompra = ordencompra;
	}

	public Factura(Ordencompra ordencompra, Date dfecha) {
		this.ordencompra = ordencompra;
		this.dfecha = dfecha;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "vIdFactura", unique = true, nullable = false)
	public Integer getVidFactura() {
		return this.vidFactura;
	}

	public void setVidFactura(Integer vidFactura) {
		this.vidFactura = vidFactura;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OrdenCompra_vIdOrdenCompra", nullable = false)
	public Ordencompra getOrdencompra() {
		return this.ordencompra;
	}

	public void setOrdencompra(Ordencompra ordencompra) {
		this.ordencompra = ordencompra;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "dFecha", length = 10)
	public Date getDfecha() {
		return this.dfecha;
	}

	public void setDfecha(Date dfecha) {
		this.dfecha = dfecha;
	}

}
