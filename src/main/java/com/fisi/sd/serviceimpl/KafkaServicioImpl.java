package com.fisi.sd.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fisi.sd.model.OrdenCompraModel;
import com.fisi.sd.service.KafkaServicio;
import com.google.gson.Gson;

@Service("kafkaService")
public class KafkaServicioImpl implements KafkaServicio{
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@Autowired
    private Gson jsonConverter;
	
	@Override
	public void enviarMensaje(OrdenCompraModel ordenCompraModel) {
		kafkaTemplate.send("validar", jsonConverter.toJson(ordenCompraModel));
	}

	@Override
	public OrdenCompraModel recibirMensaje(String sMensaje) {
		OrdenCompraModel ordenCompraModel = (OrdenCompraModel) jsonConverter.fromJson(sMensaje, OrdenCompraModel.class);
		return ordenCompraModel;
	}

}
