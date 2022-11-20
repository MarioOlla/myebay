package it.prova.myebay.repository.annuncio;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.myebay.model.annuncio.Annuncio;

public interface AnnuncioRepository extends CrudRepository<Annuncio	, Long>,CustomAnnuncioRepository {
	
	@Query( value = "delete from annuncio_categoria where annuncio_id = ?", nativeQuery = true)
	public void unlinkAnnunciFromCategorie(Long idAnnuncio);
}
