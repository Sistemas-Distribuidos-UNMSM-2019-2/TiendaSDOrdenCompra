package com.fisi.sd;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Clave {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
		 System.out.println("ADADSADS");
		 System.out.println(pe.encode("12345"));
		 
	}

}
