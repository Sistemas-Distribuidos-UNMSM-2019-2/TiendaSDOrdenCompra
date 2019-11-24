package com.fisi.sd.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fisi.sd.model.UsuarioModel;
import com.fisi.sd.repository.RepositoryUsuario;
import com.fisi.sd.service.UsuarioServicio;
import com.fisi.sd.transform.UsuarioTransform;

@Service("usuarioService")
public class UsuarioClienteServicioImpl implements UsuarioServicio{
	@Autowired
	@Qualifier("userRepository")
	private RepositoryUsuario usuarioRepository;
	@Autowired
	@Qualifier("usuarioTransform")
	private UsuarioTransform usuarioTransform;
	
	@Override
	public UsuarioModel buscarUsuario(String username) {
		return usuarioTransform.transformEM(usuarioRepository.findByVidUsuario(username));
	}

}
