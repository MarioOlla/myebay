package it.prova.myebay.repository.annuncio;

import java.util.List;

import it.prova.myebay.model.annuncio.Annuncio;

public interface CustomAnnuncioRepository {

	List<Annuncio> findByExample(Annuncio example);

}
