package it.prova.myebay.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.myebay.dto.AnnuncioDTO;
import it.prova.myebay.dto.CategoriaDTO;
import it.prova.myebay.model.Acquisto;
import it.prova.myebay.model.Utente;
import it.prova.myebay.model.annuncio.Annuncio;
import it.prova.myebay.model.annuncio.AnnuncioBuilder;
import it.prova.myebay.service.AcquistoService;
import it.prova.myebay.service.AnnuncioService;
import it.prova.myebay.service.CategoriaService;
import it.prova.myebay.service.UtenteService;

@Controller
@RequestMapping(value = "/annuncio")
public class AnnuncioController {
	
	@Autowired 
	private CategoriaService categoriaService;
	
	@Autowired 
	private AnnuncioService annuncioService;
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired 
	private AcquistoService acquistoService;
	
	@GetMapping("/listAll")
	public ModelAndView listAll(){
		ModelAndView mv = new ModelAndView();
		mv.addObject("annuncio_list_attr",
				AnnuncioDTO.buildAnnuncioDTOListFromModelList(annuncioService.listAllAnnunci()));
		mv.setViewName("annuncio/list");
		return mv;
	}
	
	@GetMapping("/annuncicurrent")
	public ModelAndView listAllOfCurrentUser(Authentication auth) {
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("annuncio_list_attr", 
				AnnuncioDTO.buildAnnuncioDTOListFromModelList(annuncioService.findByExample(new AnnuncioBuilder().utenteInserimento(utenteService.findByUsername(auth.getName())).build())));
		mv.setViewName("annuncio/list");
		return mv;
	}

	@GetMapping(value = {"/open",""})
	public ModelAndView listAllAperti() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("annuncio_list_attr",
				AnnuncioDTO.buildAnnuncioDTOListFromModelList(annuncioService.findByExample(new AnnuncioBuilder().aperto(true).build())));
		mv.setViewName("annuncio/list");
		return mv;
	}
	
	@GetMapping("/search")
	public String searchAnnuncio() {
		return "annuncio/search";
	}

	@GetMapping("/show/{idAnnuncio}")
	public String showUtente(@PathVariable(required = true) Long idAnnuncio, Model model) {
		Annuncio annuncioModel = annuncioService.caricaSingoloAnnuncio(idAnnuncio);
		AnnuncioDTO result = AnnuncioDTO.buildAnnuncioDTOFromModel(annuncioModel);
		model.addAttribute("show_annuncio_attr", result);
		model.addAttribute("categorie_annuncio_attr", CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.cercaCategorieByIds(result.getCategorieIds())));
		return "annuncio/show";
	}

	@GetMapping("/insert")
	public String create(Model model) {
		model.addAttribute("categorie_tutte_attr", CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAll()));
		model.addAttribute("insert_annuncio_attr", new AnnuncioDTO());
		return "annuncio/insert";
	}

	@PostMapping("/save")
	public String save(
			@Validated @ModelAttribute("insert_annuncio_attr") AnnuncioDTO annuncioDTO,
			BindingResult result, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		
		Utente utenteInserimento = utenteService.findByUsername(annuncioDTO.getUtenteInserimento());	
		if(annuncioDTO.getPrezzo()!=null && annuncioDTO.getPrezzo()<=0) {
			model.addAttribute("categorie_tutte_attr", CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAll()));
			result.rejectValue("prezzo", "prezzo.notpositive");
			return "annuncio/insert";
		}
		
		if(result.hasErrors()) {
			model.addAttribute("categorie_tutte_attr", CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAll()));
			return "annuncio/insert";
		}
		
		Annuncio toInsert = annuncioDTO.buildAnnuncioModel();
		toInsert.setUtenteInserimento(utenteInserimento);
		toInsert.setAperto(true);
		toInsert.setData(new Date());
		System.out.println("sono arrivato qui");
		annuncioService.inserisciNuovo(toInsert);
		
		redirectAttrs.addFlashAttribute("successMessage", "Inserimento nuovo annuncio avvenuto con successo");
		return "redirect:/annuncio/open";		
	}

	@GetMapping("/edit/{idAnnuncio}")
	public String edit(@PathVariable(required = true) Long idAnnuncio, Model model) {
		Annuncio annuncioModel = annuncioService.caricaSingoloAnnuncio(idAnnuncio);
		AnnuncioDTO result = AnnuncioDTO.buildAnnuncioDTOFromModel(annuncioModel);
		model.addAttribute("categorie_tutte_attr", CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAll()));
		model.addAttribute("toUpdate_annuncio_attr", result);
		return "annuncio/insert";
	}

	@PostMapping("/update")
	public String update(@Validated @ModelAttribute("toUpdate_annuncio_attr") AnnuncioDTO annuncioDTO,
			BindingResult result, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {

		if(result.hasErrors()) {
			model.addAttribute("categorie_tutte_attr", CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAll()));
			return "annuncio/update";
		}
		
		Annuncio toUpdate = annuncioDTO.buildAnnuncioModel();
		if(!annuncioDTO.getAperto()) {
			redirectAttrs.addFlashAttribute("errorMessage", "Impossibile aggiornare un annuncio già chiuso.");
			return "redirect:/annuncio/open";
		}
			
		annuncioService.aggiorna(toUpdate);
		
		redirectAttrs.addFlashAttribute("successMessage", "Aggiornamento annuncio avvenuto con successo");
		return "redirect:/annuncio/open";
	}
	
	@GetMapping("/preDelete/{idAnnuncio}")
	public String prepareDelete() {
		return null;
	}
	
	@PostMapping("/delete")
	public String delete(){
		return null;
	}
	
	@PostMapping("/buy")
	public String buy(@RequestParam(name = "idAnnuncioDaComprare", required = true) Long idAnnuncioDaComprare,
			@RequestParam(name = "callerPage", required = true) String callerPage,
			@RequestParam(name = "currentUser", required = true) String currentUserUsername, RedirectAttributes redirectAttrs) {
		
		if (currentUserUsername.isBlank()) {
			redirectAttrs.addAttribute("redirectPage", callerPage);
			return "utente/login";
		}
		
		Annuncio toBeBought = annuncioService.caricaSingoloAnnuncio(idAnnuncioDaComprare);
		Utente utenteAcquirente = utenteService.findByUsername(currentUserUsername);
		
		if (!toBeBought.getAperto()) {
			redirectAttrs.addAttribute("errorMessage", "Impossibile effettuare l'acquisto : qualcun altro ha già acquistato l'articolo di questo annuncio.");
			return "redirect:"+callerPage;
		}
		
		if (toBeBought.getUtenteInserimento().getId() == utenteAcquirente.getId()) {
			redirectAttrs.addAttribute("errorMessage", "Impossibile effettuare l'acquisto : questo annuncio è stato postato da te.");
			return "redirect:"+callerPage;
		}
		
		if(toBeBought.getPrezzo() > utenteAcquirente.getCreditoResiduo()) {
			redirectAttrs.addAttribute("errorMessage", "Impossibile effettuare l'acquisto : credito residuo insufficiente");
			return "redirect:"+callerPage;
		}
		
		Acquisto nuovo = new Acquisto(toBeBought.getTestoAnnuncio() ,new Date(), toBeBought.getPrezzo(),utenteAcquirente);
		acquistoService.inserisciNuovo(nuovo);
		utenteAcquirente.setCreditoResiduo(utenteAcquirente.getCreditoResiduo()-toBeBought.getPrezzo());
		utenteService.aggiorna(utenteAcquirente);
		toBeBought.setAperto(false);
		annuncioService.aggiorna(toBeBought);
		
		redirectAttrs.addAttribute("successMessage", "Acquisto avvenuto con successo!");
		return "redirect:/acquisto/myacquisti";
	}
	
}
