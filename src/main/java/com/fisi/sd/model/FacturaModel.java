package com.fisi.sd.model;

import java.util.Date;
import java.util.List;

public class FacturaModel {
	private int nCodigoOrden;
	private String sRucCliente;
	private double nPrecioTotal;
	private String sEstado;
	private String fechaFactura;
	private String sDireccionCliente;
	private String sNombreCliente;
	private List<OrdenCompraDetalleModel> lDetalleCompra;

	public int getnCodigoOrden() {
		return nCodigoOrden;
	}

	public void setnCodigoOrden(int nCodigoOrden) {
		this.nCodigoOrden = nCodigoOrden;
	}

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

	public String getsEstado() {
		return sEstado;
	}

	public void setsEstado(String sEstado) {
		this.sEstado = sEstado;
	}

	public String getFechaFactura() {
		return fechaFactura;
	}

	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	public String getsDireccionCliente() {
		return sDireccionCliente;
	}

	public void setsDireccionCliente(String sDireccionCliente) {
		this.sDireccionCliente = sDireccionCliente;
	}

	public String getsNombreCliente() {
		return sNombreCliente;
	}

	public void setsNombreCliente(String sNombreCliente) {
		this.sNombreCliente = sNombreCliente;
	}

	public List<OrdenCompraDetalleModel> getlDetalleCompra() {
		return lDetalleCompra;
	}

	public void setlDetalleCompra(List<OrdenCompraDetalleModel> lDetalleCompra) {
		this.lDetalleCompra = lDetalleCompra;
	}

}
