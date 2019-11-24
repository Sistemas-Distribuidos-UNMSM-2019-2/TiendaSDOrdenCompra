package com.fisi.sd.service;

import com.fisi.sd.model.OrdenCompraModel;

public interface KafkaServicio {
	public void enviarMensaje(OrdenCompraModel ordenCompraModel);
	public OrdenCompraModel recibirMensaje(String sMensaje);
}
