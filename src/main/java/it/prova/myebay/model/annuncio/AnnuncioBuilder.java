package it.prova.myebay.model.annuncio;

import java.util.Date;
import java.util.Set;

import it.prova.myebay.model.Categoria;
import it.prova.myebay.model.Utente;

public class AnnuncioBuilder {

	private Annuncio toBuild = new Annuncio();

	public AnnuncioBuilder id(Long id) {
		this.toBuild.setId(id);
		return this;
	}

	public AnnuncioBuilder testoAnnuncio(String testoAnnuncio) {
		toBuild.setTestoAnnuncio(testoAnnuncio);
		return this;
	}

	public AnnuncioBuilder data(Date data) {
		this.toBuild.setData(data);
		return this;
	}

	public AnnuncioBuilder prezzo(Integer prezzo) {
		this.toBuild.setPrezzo(prezzo);
		return this;
	}

	public AnnuncioBuilder aperto(Boolean aperto) {
		this.toBuild.setAperto(aperto);
		return this;
	}

	public AnnuncioBuilder utenteInserimento(Utente utenteInserimento) {
		this.toBuild.setUtenteInserimento(utenteInserimento);
		return this;
	}

	public AnnuncioBuilder categorie(Set<Categoria> categorie) {
		this.toBuild.setCategorie(categorie);
		return this;
	}

	public Annuncio build() {
		return this.toBuild;
	}
}
