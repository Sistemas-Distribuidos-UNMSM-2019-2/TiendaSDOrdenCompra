package com.fisi.sd.transform;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fisi.sd.entity.Usuario;
import com.fisi.sd.model.ClienteModel;
import com.fisi.sd.model.UsuarioModel;

@Component("usuarioTransform")
public class UsuarioTransform implements Transform<UsuarioModel, Usuario>{

	@Override
	public Usuario transformME(UsuarioModel oModel) {
		if(oModel != null) {
			Usuario oUsuario = new Usuario();
			oUsuario.setVidUsuario(oModel.getsIdUsuario());
			oUsuario.setVclave(oModel.getsClave());
			
			return oUsuario;
		}
		return null;
	}

	@Override
	public List<Usuario> transformME(List<UsuarioModel> lModel) {
		if(lModel != null) {
			List<Usuario> lUsuario = new ArrayList<>();
			
			for(UsuarioModel auxiliar : lModel) {
				lUsuario.add(transformME(auxiliar));
			}
			
			return lUsuario;
		}
		return null;
	}

	@Override
	public UsuarioModel transformEM(Usuario oEntity) {
		if(oEntity != null) {
			UsuarioModel oUsuarioModel = new UsuarioModel();
			oUsuarioModel.setsIdUsuario(oEntity.getVidUsuario());
			oUsuarioModel.setsRUC(oEntity.getCliente().getVruc());
			oUsuarioModel.setsClave(oEntity.getVclave());
			
			return oUsuarioModel;
		}
		return null;
	}

	@Override
	public List<UsuarioModel> transformEM(List<Usuario> lEntity) {
		if(lEntity != null) {
			List<UsuarioModel> lUsuarioModel = new ArrayList<>();
			
			for(Usuario auxiliar : lEntity) {
				lUsuarioModel.add(transformEM(auxiliar));
			}
			
			return lUsuarioModel;
		}
		return null;
	}

}
