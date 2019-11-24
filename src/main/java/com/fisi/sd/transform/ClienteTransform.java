package com.fisi.sd.transform;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fisi.sd.entity.Cliente;
import com.fisi.sd.model.ClienteModel;

@Component("clienteTransform")
public class ClienteTransform implements Transform<ClienteModel, Cliente>{

	@Override
	public Cliente transformME(ClienteModel oModel) {
		if(oModel != null) {
			Cliente oCliente = new Cliente();
			oCliente.setVruc(oModel.getsRuc());
			oCliente.setVapellidoPaterno(oModel.getsApellidoPaterno());
			oCliente.setVapellidoMaterno(oModel.getsApellidoMaterno());
			oCliente.setVnombre(oModel.getsNombre());
			oCliente.setVtelefono(oModel.getsTelefono());
			oCliente.setVcelular(oModel.getsCelular());
			oCliente.setVdireccion(oModel.getsDireccion());
			
			return oCliente;
		}
		return null;
	}

	@Override
	public List<Cliente> transformME(List<ClienteModel> lModel) {
		if(lModel != null) {
			List<Cliente> lCliente = new ArrayList<>();
			
			for(ClienteModel auxiliar : lModel) {
				lCliente.add(transformME(auxiliar));
			}
			
			return lCliente;
		}
		return null;
	}

	@Override
	public ClienteModel transformEM(Cliente oEntity) {
		if(oEntity != null) {
			ClienteModel oClienteModel = new ClienteModel();
			oClienteModel.setsRuc(oEntity.getVruc());
			oClienteModel.setsApellidoPaterno(oEntity.getVapellidoPaterno());
			oClienteModel.setsApellidoMaterno(oEntity.getVapellidoMaterno());
			oClienteModel.setsNombre(oEntity.getVnombre());
			oClienteModel.setsTelefono(oEntity.getVtelefono());
			oClienteModel.setsCelular(oEntity.getVcelular());
			oClienteModel.setsDireccion(oEntity.getVdireccion());
			
			return oClienteModel;
		}
		return null;
	}

	@Override
	public List<ClienteModel> transformEM(List<Cliente> lEntity) {
		if(lEntity != null) {
			List<ClienteModel> lClienteModel = new ArrayList<>();
			
			for(Cliente auxiliar : lEntity) {
				lClienteModel.add(transformEM(auxiliar));
			}
			
			return lClienteModel;
		}
		return null;
	}

}
