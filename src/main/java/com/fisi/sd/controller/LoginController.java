package com.fisi.sd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fisi.sd.entity.Usuario;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String showLogin(Model model, @RequestParam(name = "error", required = false) String error) {
		model.addAttribute("Usuario", new Usuario());
		model.addAttribute("error", error);
		return "view/login";
	}
	
}
