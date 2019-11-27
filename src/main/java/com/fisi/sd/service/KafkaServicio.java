package com.fisi.sd.service;

import com.fisi.sd.model.OrdenCompraModel;

public interface KafkaServicio {
	public void enviarMensaje(OrdenCompraModel ordenCompraModel, String topico);
	public OrdenCompraModel recibirMensaje(String sMensaje);
}
