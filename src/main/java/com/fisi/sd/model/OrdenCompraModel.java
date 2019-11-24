package com.fisi.sd.model;

import java.util.Date;
import java.util.List;

public class OrdenCompraModel {
	private String sRucCliente;
	private double nPrecioTotal;
	private Date dFechaCompra;
	private Date dFechaPago;
	private List<OrdenCompraDetalleModel> lDetalleCompra;

	public String getsRucCliente() {
		return sRucCliente;
	}

	public void setsRucCliente(String sRucCliente) {
		this.sRucCliente = sRucCliente;
	}

	public double getnPrecioTotal() {
		return nPrecioTotal;
	}

	public void setnPrecioTotal(double nPrecioTotal) {
		this.nPrecioTotal = nPrecioTotal;
	}

	public Date getdFechaCompra() {
		return dFechaCompra;
	}

	public void setdFechaCompra(Date dFechaCompra) {
		this.dFechaCompra = dFechaCompra;
	}

	public Date getdFechaPago() {
		return dFechaPago;
	}

	public void setdFechaPago(Date dFechaPago) {
		this.dFechaPago = dFechaPago;
	}

	public List<OrdenCompraDetalleModel> getlDetalleCompra() {
		return lDetalleCompra;
	}

	public void setlDetalleCompra(List<OrdenCompraDetalleModel> lDetalleCompra) {
		this.lDetalleCompra = lDetalleCompra;
	}

}
