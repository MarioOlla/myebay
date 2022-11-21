package it.prova.myebay.repository.annuncio;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.myebay.model.annuncio.Annuncio;

public interface AnnuncioRepository extends CrudRepository<Annuncio	, Long>,CustomAnnuncioRepository {
	
	@Modifying
	@Query(value = "delete from annuncio_categoria where annuncio_id = ?", nativeQuery = true)
	public void unlinkAnnunciFromCategorie(Long idAnnuncio);
}
