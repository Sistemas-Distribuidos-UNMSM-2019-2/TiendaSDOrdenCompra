package com.fisi.sd.model;

public class OrdenCompraDetalleModel {
	private int nCodigoProducto;
	private String sNombreProducto;
	private int nCantidadProducto;
	private double nTotalParcial;
	private boolean bExistencia;

	public int getnCodigoProducto() {
		return nCodigoProducto;
	}

	public void setnCodigoProducto(int nCodigoProducto) {
		this.nCodigoProducto = nCodigoProducto;
	}

	public String getsNombreProducto() {
		return sNombreProducto;
	}

	public void setsNombreProducto(String sNombreProducto) {
		this.sNombreProducto = sNombreProducto;
	}

	public int getnCantidadProducto() {
		return nCantidadProducto;
	}

	public void setnCantidadProducto(int nCantidadProducto) {
		this.nCantidadProducto = nCantidadProducto;
	}

	public double getnTotalParcial() {
		return nTotalParcial;
	}

	public void setnTotalParcial(double nTotalParcial) {
		this.nTotalParcial = nTotalParcial;
	}

	public boolean isbExistencia() {
		return bExistencia;
	}

	public void setbExistencia(boolean bExistencia) {
		this.bExistencia = bExistencia;
	}

}
