package it.prova.myebay.repository.annuncio;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import it.prova.myebay.model.annuncio.Annuncio;

public class CustomAnnuncioRepositoryImpl implements CustomAnnuncioRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Annuncio> findByExample(Annuncio example){
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder("select a from Annuncio a left join fetch a.categorie c left join fetch a.utenteInserimento u where a.id = a.id ");

		if (StringUtils.isNotEmpty(example.getTestoAnnuncio())) {
			whereClauses.add(" a.testoAnnuncio  like :testoAnnuncio ");
			parameterMap.put("testoAnnuncio", "%" + example.getTestoAnnuncio() + "%");
		}
		if (example.getPrezzo()!=null) {
			whereClauses.add(" a.prezzo >= :prezzo");
			parameterMap.put("prezzo", example.getPrezzo());
		}
		if (example.getData()!=null && !example.getData().after(new Date())) {
			whereClauses.add(" a.data >= :data ");
			parameterMap.put("data", example.getData());
		}
		if (example.getAperto()!= null) {
			whereClauses.add(" a.aperto = :aperto ");
			parameterMap.put("aperto", example.getAperto());
		}
		if (example.getUtenteInserimento()!=null&& StringUtils.isNotEmpty(example.getUtenteInserimento().getUsername())) {
			whereClauses.add(" u.username like :username ");
			parameterMap.put("username", "%"+example.getUtenteInserimento().getUsername()+"%");
		}
		
		queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Annuncio> typedQuery = entityManager.createQuery(queryBuilder.toString(), Annuncio.class);

		for (String key : parameterMap.keySet()) {
			typedQuery.setParameter(key, parameterMap.get(key));
		}

		return typedQuery.getResultList();
	}
}
