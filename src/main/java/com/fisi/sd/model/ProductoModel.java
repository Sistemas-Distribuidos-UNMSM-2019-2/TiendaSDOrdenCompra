package com.fisi.sd.model;

public class ProductoModel {
	private int nCodigo;
	private String sNombre;
	private String sDescripcion;
	private double nPrecioUnitario;
	private int nCantidad;

	public int getnCodigo() {
		return nCodigo;
	}

	public void setnCodigo(int nCodigo) {
		this.nCodigo = nCodigo;
	}

	public String getsNombre() {
		return sNombre;
	}

	public void setsNombre(String sNombre) {
		this.sNombre = sNombre;
	}

	public String getsDescripcion() {
		return sDescripcion;
	}

	public void setsDescripcion(String sDescripcion) {
		this.sDescripcion = sDescripcion;
	}

	public double getnPrecioUnitario() {
		return nPrecioUnitario;
	}

	public void setnPrecioUnitario(double nPrecioUnitario) {
		this.nPrecioUnitario = nPrecioUnitario;
	}

	public int getnCantidad() {
		return nCantidad;
	}

	public void setnCantidad(int nCantidad) {
		this.nCantidad = nCantidad;
	}

}
