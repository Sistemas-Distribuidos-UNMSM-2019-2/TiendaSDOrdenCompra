package com.fisi.sd.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fisi.sd.model.ClienteModel;
import com.fisi.sd.repository.RepositoryCliente;
import com.fisi.sd.service.ClienteServicio;
import com.fisi.sd.transform.ClienteTransform;

@Service("clienteService")
public class ClienteServicioImpl implements ClienteServicio {
	@Autowired
	@Qualifier("clienteRepository")
	private RepositoryCliente clienteRepository;
	@Autowired
	@Qualifier("clienteTransform")
	private ClienteTransform clienteTransform;

	@Override
	public ClienteModel buscarCliente(String sRUC) {
		return clienteTransform.transformEM(clienteRepository.findByVruc(sRUC));
	}
}
