package it.prova.myebay.web.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.myebay.dto.AnnuncioDTO;
import it.prova.myebay.dto.CategoriaDTO;
import it.prova.myebay.model.Utente;
import it.prova.myebay.model.annuncio.Annuncio;
import it.prova.myebay.model.annuncio.AnnuncioBuilder;
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

	@GetMapping("/listAll")
	public ModelAndView listAll() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("annuncio_list_attr",
				AnnuncioDTO.buildAnnuncioDTOListFromModelList(annuncioService.listAllAnnunci()));
		mv.setViewName("annuncio/list");
		return mv;
	}

	@GetMapping("/annuncicurrent")
	public ModelAndView listAllOfCurrentUser(Authentication auth) {
		ModelAndView mv = new ModelAndView();

		mv.addObject("annuncio_list_attr", AnnuncioDTO.buildAnnuncioDTOListFromModelList(annuncioService.findByExample(
				new AnnuncioBuilder().utenteInserimento(utenteService.findByUsername(auth.getName())).build())));
		mv.setViewName("annuncio/mylist");
		return mv;
	}

	@GetMapping(value = { "/open", "" })
	public ModelAndView listAllAperti() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("annuncio_list_attr", AnnuncioDTO.buildAnnuncioDTOListFromModelList(
				annuncioService.findByExample(new AnnuncioBuilder().aperto(true).build())));
		mv.setViewName("annuncio/list");
		return mv;
	}
	
	@GetMapping("/searchRes")
	public String listAnnunci(AnnuncioDTO annuncioExample, ModelMap model) {
		model.addAttribute("annuncio_list_attr",
				AnnuncioDTO.buildAnnuncioDTOListFromModelList(annuncioService.findByExample(annuncioExample.buildAnnuncioModel())));
		return "annuncio/list";
	}

	@GetMapping("/search")
	public String searchAnnuncio(Model model) {
		model.addAttribute("categorie_tutte_attr",
				CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAll()));
		return "annuncio/search";
	}

	@GetMapping("/show/{idAnnuncio}")
	public String showUtente(@PathVariable(required = true) Long idAnnuncio, Model model) {
		model.addAttribute("show_annuncio_attr", AnnuncioDTO.buildAnnuncioDTOFromModel(annuncioService.caricaSingoloAnnuncio(idAnnuncio)));
		model.addAttribute("categorie_annuncio_attr", CategoriaDTO
				.createCategoriaDTOListFromModelList(categoriaService.cercaCategorieByIds(AnnuncioDTO.buildAnnuncioDTOFromModel(annuncioService.caricaSingoloAnnuncio(idAnnuncio)).getCategorieIds())));
		return "annuncio/show";
	}

	@GetMapping("/insert")
	public String create(Model model) {
		model.addAttribute("categorie_tutte_attr",
				CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAll()));
		model.addAttribute("insert_annuncio_attr", new AnnuncioDTO());
		return "annuncio/insert";
	}

	@PostMapping("/save")
	public String save(@Validated @ModelAttribute("insert_annuncio_attr") AnnuncioDTO annuncioDTO, BindingResult result,
			Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {

		if (annuncioDTO.getPrezzo() != null && annuncioDTO.getPrezzo() <= 0) {
			model.addAttribute("categorie_tutte_attr",
					CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAll()));
			result.rejectValue("prezzo", "prezzo.notpositive");
			return "annuncio/insert";
		}

		if (result.hasErrors()) {
			model.addAttribute("categorie_tutte_attr",
					CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAll()));
			return "annuncio/insert";
		}

		Annuncio toInsert = annuncioDTO.buildAnnuncioModel();
	
		annuncioService.inserisciNuovo(toInsert);

		redirectAttrs.addFlashAttribute("successMessage", "Inserimento nuovo annuncio avvenuto con successo");
		return "redirect:/annuncio/open";
	}

	@GetMapping("/preUpdate/{idAnnuncio}")
	public String edit(@PathVariable(required = true) Long idAnnuncio, Model model, Authentication auth,
			RedirectAttributes redirectAttrs) {
		Annuncio annuncioModel = annuncioService.caricaSingoloAnnuncio(idAnnuncio);

		if (annuncioModel.getUtenteInserimento().getId() != utenteService.findByUsername(auth.getName()).getId()) {
			redirectAttrs.addFlashAttribute("errorMessage",
					"L'annuncio può essere modificato solo da chi lo ha creato.");
			return "redirect:/annuncio/open";
		}

		if (!annuncioModel.getAperto()) {
			redirectAttrs.addFlashAttribute("errorMessage", "Un annuncio chiuso non può essere modificato");
			return "redirect:/annuncio/open";
		}

		AnnuncioDTO result = AnnuncioDTO.buildAnnuncioDTOFromModel(annuncioModel);
		model.addAttribute("categorie_tutte_attr",
				CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAll()));
		model.addAttribute("toUpdate_annuncio_attr", result);
		return "annuncio/update";
	}

	@PostMapping("/update")
	public String update(@Validated @ModelAttribute("toUpdate_annuncio_attr") AnnuncioDTO annuncioDTO,
			BindingResult result, Model model, RedirectAttributes redirectAttrs) {

		if (result.hasErrors()) {
			model.addAttribute("categorie_tutte_attr",
					CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAll()));
			return "annuncio/update";
		}

		Annuncio toUpdate = annuncioDTO.buildAnnuncioModel();
		toUpdate.setUtenteInserimento(utenteService.findByUsername(annuncioDTO.getUtenteInserimento()));
		if (!annuncioDTO.getAperto()) {
			redirectAttrs.addFlashAttribute("errorMessage", "Impossibile aggiornare un annuncio già chiuso.");
			return "redirect:/annuncio/open";
		}

		annuncioService.aggiorna(toUpdate);

		redirectAttrs.addFlashAttribute("successMessage", "Aggiornamento annuncio avvenuto con successo");
		return "redirect:/annuncio/annuncicurrent";
	}

	@GetMapping("/preDelete/{idAnnuncio}")
	public String prepareDelete(@PathVariable(required = true) Long idAnnuncio, Model model, Authentication auth,
			RedirectAttributes redirectAttrs) {
		Annuncio annuncioModel = annuncioService.caricaSingoloAnnuncio(idAnnuncio);

		if (annuncioModel.getUtenteInserimento().getId() != utenteService.findByUsername(auth.getName()).getId()) {
			redirectAttrs.addFlashAttribute("errorMessage",
					"L'annuncio può essere eliminato solo da chi lo ha creato.");
			return "redirect:/annuncio/open";
		}

		if (!annuncioModel.getAperto()) {
			redirectAttrs.addFlashAttribute("errorMessage", "Un annuncio chiuso non può essere eliminato.");
			return "redirect:/annuncio/open";
		}

		AnnuncioDTO result = AnnuncioDTO.buildAnnuncioDTOFromModel(annuncioModel);
		model.addAttribute("categorie_annuncio_attr",
				CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.cercaCategorieByIds(result.getCategorieIds())));
		model.addAttribute("toDelete_annuncio_attr", result);
		return "annuncio/delete";
	}

	@PostMapping("/delete")
	public String delete(@RequestParam(required = true, name = "idToDelete") Long idToDelete,RedirectAttributes redirectAttrs) {
		
		if(!annuncioService.caricaSingoloAnnuncio(idToDelete).getAperto()) {
			redirectAttrs.addFlashAttribute("errorMessage", "Un annuncio chiuso non può essere eliminato.");
			return "redirect:/annuncio/open";
		}
		
		annuncioService.unlinkCategorieDaAnnincio(idToDelete);
		annuncioService.rimuovi(idToDelete);
		
		redirectAttrs.addFlashAttribute("successMessage", "Annuncio rimosso con successo.");
		return "redirect:/annuncio/open";
	}

	@PostMapping("/buy")
	public String buy(@RequestParam(required = true) Long idAnnuncioDaComprare,
			Model model, RedirectAttributes redirectAttrs,HttpServletRequest request,Principal principal) {

		Annuncio toBeBought = annuncioService.caricaSingoloAnnuncio(idAnnuncioDaComprare);
		Utente utenteAcquirente = utenteService.findByUsername(principal.getName());

		if (!toBeBought.getAperto()) {
			redirectAttrs.addAttribute("errorMessage",
					"Impossibile effettuare l'acquisto : qualcun altro ha già acquistato l'articolo di questo annuncio.");
			return "redirect: /show/{"+idAnnuncioDaComprare+"}";
		}

		if (toBeBought.getUtenteInserimento().getId() == utenteAcquirente.getId()) {
			redirectAttrs.addAttribute("errorMessage",
					"Impossibile effettuare l'acquisto : questo annuncio è stato postato da te.");
			return "redirect: /show/{"+idAnnuncioDaComprare+"}";
		}

		if (toBeBought.getPrezzo() > utenteAcquirente.getCreditoResiduo()) {
			redirectAttrs.addAttribute("errorMessage",
					"Impossibile effettuare l'acquisto : credito residuo insufficiente");
			return "redirect: /show/{"+idAnnuncioDaComprare+"}";
		}
		
		annuncioService.compra( toBeBought,  utenteAcquirente);

		redirectAttrs.addAttribute("successMessage", "Acquisto avvenuto con successo!");
		return "redirect:/acquisto/myacquisti";
	}
	
	@GetMapping("/acquistaWithoutAuth")
	public String acquistaWithoutAuth(@RequestParam(required = true) Long idAnnuncioWithNoAuth,
			Model model, RedirectAttributes redirectAttrs,HttpServletRequest request, Principal principal) {
		System.out.println("maledetto   "+idAnnuncioWithNoAuth);
		if (principal != null) {
			return this.buy(idAnnuncioWithNoAuth, model, redirectAttrs, request, principal);
		}
		model.addAttribute("idAnnuncioWithNoAuth", idAnnuncioWithNoAuth);
		return "/login";
	}
}
