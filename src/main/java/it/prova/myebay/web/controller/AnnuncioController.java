package it.prova.myebay.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.myebay.dto.AnnuncioDTO;
import it.prova.myebay.dto.CategoriaDTO;
import it.prova.myebay.dto.UtenteDTO;
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
	public ModelAndView listAll(){
		ModelAndView mv = new ModelAndView();
		mv.addObject("annuncio_list_attr",
				AnnuncioDTO.buildAnnuncioDTOListFromModelList(annuncioService.listAllAnnunci()));
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
		model.addAttribute("insert_annuncio_attr", new UtenteDTO());
		return "annuncio/insert";
	}

	@PostMapping("/save")
	public String save(
			@Validated @ModelAttribute("insert_annuncio_attr") AnnuncioDTO annuncioDTO,
			BindingResult result, Model model, RedirectAttributes redirectAttrs) {
		
		if(result.hasErrors()) {
			model.addAttribute("categorie_tutte_attr", CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAll()));
			return "annuncio/insert";
		}
		
		Annuncio toInsert = annuncioDTO.buildAnnuncioModel();
		toInsert.setData(new Date());
		annuncioService.inserisciNuovo(toInsert);
		
		redirectAttrs.addFlashAttribute("successMessage", "Inserimento nuovo annuncio avvenuto con successo");
		return "redirect:/annuncio/open";		
	}

	@GetMapping("/edit/{idAnnuncio}")
	public String edit(@PathVariable(required = true) Long idUtente, Model model) {
		return null;
	}

	@PostMapping("/update")
	public String update(@Validated @ModelAttribute("edit_utente_attr") UtenteDTO utenteDTO,
			BindingResult result, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {

		return null;
	}
	
	@GetMapping("/preDelete/{idAnnuncio}")
	public String prepareDelete() {
		return null;
	}
	
	@PostMapping("/delete")
	public String delete(){
		return null;
	}

	@GetMapping("/preBuy/{idAnnuncio}")
	public String prepareBuy() {
		return null;
	}
	
	@PostMapping("/buy")
	public String buy() {
		return null;
	}
	
}
