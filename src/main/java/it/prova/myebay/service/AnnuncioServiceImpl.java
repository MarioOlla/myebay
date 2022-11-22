package it.prova.myebay.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.myebay.model.Acquisto;
import it.prova.myebay.model.Utente;
import it.prova.myebay.model.annuncio.Annuncio;
import it.prova.myebay.repository.acquisto.AcquistoRepository;
import it.prova.myebay.repository.annuncio.AnnuncioRepository;
import it.prova.myebay.repository.utente.UtenteRepository;

@Service
public class AnnuncioServiceImpl implements AnnuncioService {
	
	@Autowired
	private AnnuncioRepository annuncioRepository;
	
	@Autowired
	private AcquistoRepository acquistoRepository;
	
	@Autowired
	private UtenteRepository utenteRepository;

	@Transactional(readOnly = true)
	public List<Annuncio> listAllAnnunci() {
		return (List<Annuncio>)annuncioRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Annuncio caricaSingoloAnnuncio(Long id) {
		return annuncioRepository.findById(id).orElse(null);
	}

	@Transactional
	public void aggiorna(Annuncio annuncioInstance) {
		annuncioRepository.save(annuncioInstance);
		
	}

	@Transactional
	public void inserisciNuovo(Annuncio annuncioInstance) {	
		annuncioRepository.save(annuncioInstance);		
	}
	
	@Transactional
	public void inserisciNuovo(Annuncio toInsert,Utente utenteInserimento) {	
		toInsert.setUtenteInserimento(utenteInserimento);
		toInsert.setAperto(true);
		toInsert.setData(new Date());
		this.inserisciNuovo(toInsert);
	}

	@Transactional
	public void rimuovi(Long idToDelete) {
		annuncioRepository.deleteById(idToDelete);
	}

	@Transactional(readOnly = true)
	public List<Annuncio> findByExample(Annuncio example) {
		return annuncioRepository.findByExample(example);
	}

	@Transactional
	public void unlinkCategorieDaAnnincio(Long id) {
		annuncioRepository.unlinkAnnunciFromCategorie(id);
		
	}

	@Transactional
	public void compra( Annuncio toBeBought, Utente utenteAcquirente) {
		Acquisto nuovo = new Acquisto(toBeBought.getTestoAnnuncio(), new Date(), toBeBought.getPrezzo(),
				utenteAcquirente);
		acquistoRepository.save(nuovo);
		utenteAcquirente.setCreditoResiduo(utenteAcquirente.getCreditoResiduo() - toBeBought.getPrezzo());
		utenteRepository.save(utenteAcquirente);
		toBeBought.setAperto(false);
		annuncioRepository.save(toBeBought);
	}

}
