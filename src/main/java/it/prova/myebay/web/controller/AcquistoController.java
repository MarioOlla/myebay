package it.prova.myebay.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import it.prova.myebay.dto.AcquistoDTO;
import it.prova.myebay.model.Acquisto;
import it.prova.myebay.model.Utente;
import it.prova.myebay.service.AcquistoService;
import it.prova.myebay.service.UtenteService;

@Controller
@RequestMapping(value = "/acquisto")
public class AcquistoController {
	
	@Autowired
	private AcquistoService acquistoService;
	
	@Autowired
	private UtenteService utenteService;
	
	@GetMapping("/myacquisti")
	public ModelAndView listAllAcquisti(Authentication authentication,@RequestParam(name = "successMessage") String successsMessage) {
		String currentUserName = "";
		if (!(authentication instanceof AnonymousAuthenticationToken)) 
		    currentUserName = authentication.getName();	    

		ModelAndView mv = new ModelAndView();
		Utente utenteInSessione = utenteService.findByUsername(currentUserName);
		Acquisto example = new Acquisto(null, null, null, utenteInSessione);
		mv.addObject("list_acquisto_attr", AcquistoDTO.buildAcquistoDtoListFromModelList(acquistoService.findByExample(example)));
		mv.addObject("successMessage",successsMessage);
		mv.setViewName("acquisto/list");
		return mv;
	}
	
	@GetMapping(value = "/search")
	public String searchAcquisto() {
		return "acquisto/search";
	}
	
	@GetMapping(value = "/list")
	public String listAcquisti(Acquisto example, ModelMap model,HttpRequest request) {	
		model.addAttribute("acquisto_list_attr", AcquistoDTO.buildAcquistoDtoListFromModelList(acquistoService.findByExample(example)));
		return "acquisto/list";
	}
	
	
}
