package it.prova.myebay.web.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.myebay.dto.utente.UtenteChangePasswordDTO;
import it.prova.myebay.service.UtenteService;

@Controller
@RequestMapping(value = "/account")
public class AccountController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UtenteService utenteService;
	
	@GetMapping("/cambiapassword")
	public String cambiaPassword(Principal principal, Model model) {
		model.addAttribute("change_password_utente_attr", new UtenteChangePasswordDTO(principal.getName()));
		return "account/cambiapass";
	}
	
	@PostMapping("/eseguicambio")
	public String eseguiCambio(@ModelAttribute("change_password_utente_attr") UtenteChangePasswordDTO utenteDTO,
			BindingResult result, Model model, RedirectAttributes redirectAttrs,Principal principal) {
		
		if(principal.getName()==null) {
			redirectAttrs.addAttribute("errorMessage", "Nessun utente loggato.");
			return "redirect:/home";
		}
		
		if(!passwordEncoder.matches(utenteDTO.getPassword(), utenteService.findByUsername(principal.getName()).getPassword())) {
			result.rejectValue("password", "password.notYourPassword");
			return "account/cambiapass";
		}
		
		if (!result.hasFieldErrors("password") && !utenteDTO.getNuovaPassword().equals(utenteDTO.getConfermaNuovaPassword())) {
			result.rejectValue("confermaNuovaPassword", "password.diverse");
			return "account/cambiapass";
		}
					
		utenteService.cambiaPassword(utenteDTO.getConfermaNuovaPassword(), principal.getName());
		
		redirectAttrs.addAttribute("successMessage", "Password cambiata con successo.");
		return "redirect:/logout";
	}
}
