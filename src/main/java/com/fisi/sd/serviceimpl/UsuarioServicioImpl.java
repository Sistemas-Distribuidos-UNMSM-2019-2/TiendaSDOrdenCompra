package com.fisi.sd.serviceimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fisi.sd.entity.Usuario;
import com.fisi.sd.repository.RepositoryUsuario;

@Service("userService")
public class UsuarioServicioImpl implements UserDetailsService{
	
	@Autowired
	@Qualifier("userRepository")
	private RepositoryUsuario usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {		
		Usuario user = usuarioRepository.findByVidUsuario(username);
		
		List<GrantedAuthority> authorities = buildAuthorities();
		return buildUser(user, authorities);
	}

	private User buildUser(Usuario user, List<GrantedAuthority> authorities) {
		return new User(user.getVidUsuario(), user.getVclave(), true, true, true, true, authorities);
	}

	private List<GrantedAuthority> buildAuthorities() {
		Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>();
		auths.add(new SimpleGrantedAuthority("cliente"));
		return new ArrayList<GrantedAuthority>();
	}
}
