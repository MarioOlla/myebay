package it.prova.myebay.repository.acquisto;

import java.util.List;

import it.prova.myebay.model.Acquisto;
import it.prova.myebay.model.Utente;

public interface CustomAcquistoRepository {
	List<Acquisto> findByExample(Acquisto example,Utente current);
}
