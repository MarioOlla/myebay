package it.prova.myebay.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import it.prova.myebay.service.AcquistoService;
import it.prova.myebay.service.UtenteService;

@Controller
@RequestMapping(value = "/acquisto")
public class AcquistoController {
	
	@Autowired
	private AcquistoService acquistoService;
	
	@Autowired
	private UtenteService utenteService;
	
	@GetMapping(value = "/list")
	public ModelAndView listAllAcquisti() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("list_acquisto_attr",acquistoService.listAllAcquisto());
		mv.setViewName("acquisto/list");
		return mv;
	}
	
//	@GetMapping(value = "/search")
//	public String searchAcquisto() {
//		
//	}
	
	
}
