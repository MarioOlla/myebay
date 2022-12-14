package it.prova.myebay.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import it.prova.myebay.dto.utente.UtenteDTO;
import it.prova.myebay.model.Categoria;
import it.prova.myebay.model.annuncio.Annuncio;
import it.prova.myebay.model.annuncio.AnnuncioBuilder;

public class AnnuncioDTO {

	private Long id;

	@NotBlank(message = "{testoannunci.notblank}")
	private String testoAnnuncio;
	@NotNull(message = "{prezzo.notnull}")
	private Integer prezzo;

	private Date data;

	private Boolean aperto;
	@NotNull(message = "{utenteInserimento.notnull")
	private String utenteInserimento;

	private Long[] categorieIds;

	public AnnuncioDTO() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTestoAnnuncio() {
		return testoAnnuncio;
	}

	public void setTestoAnnuncio(String testoAnnuncio) {
		this.testoAnnuncio = testoAnnuncio;
	}

	public Integer getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Integer prezzo) {
		this.prezzo = prezzo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Boolean getAperto() {
		return aperto;
	}

	public void setAperto(Boolean aperto) {
		this.aperto = aperto;
	}

	public String getUtenteInserimento() {
		return utenteInserimento;
	}

	public void setUtenteInserimento(String utenteInserimento) {
		this.utenteInserimento = utenteInserimento;
	}

	public Long[] getCategorieIds() {
		return categorieIds;
	}

	public void setCategorieIds(Long[] categorieIds) {
		this.categorieIds = categorieIds;
	}

	public AnnuncioDTO id(Long id) {
		this.setId(id);
		return this;
	}

	public AnnuncioDTO testoAnnuncio(String testoAnnuncio) {
		this.setTestoAnnuncio(testoAnnuncio);
		return this;
	}

	public AnnuncioDTO data(Date data) {
		this.setData(data);
		return this;
	}

	public AnnuncioDTO prezzo(Integer prezzo) {
		this.setPrezzo(prezzo);
		return this;
	}

	public AnnuncioDTO aperto(Boolean aperto) {
		this.setAperto(aperto);
		return this;
	}

	public AnnuncioDTO utenteInserimento(String utenteInserimento) {
		this.setUtenteInserimento(utenteInserimento);
		return this;
	}

	public AnnuncioDTO categorieIds(Long[] ids) {
		this.setCategorieIds(ids);
		return this;
	}

	public Annuncio buildAnnuncioModel() {
		AnnuncioBuilder builder = new AnnuncioBuilder();
		builder.id(this.id)
		.testoAnnuncio(this.testoAnnuncio)
		.prezzo(this.prezzo)
		.data(this.data)
		.aperto(this.aperto);
		
		if(this.categorieIds != null) {
		builder.categorie(Arrays.asList(this.categorieIds).stream().map(id -> new Categoria(id))
						.collect(Collectors.toSet()));
		}
		return builder.build();

	}

	public static AnnuncioDTO buildAnnuncioDTOFromModel(Annuncio model) {
		return new AnnuncioDTO()
				.id(model.getId())
				.testoAnnuncio((String) model.getTestoAnnuncio())
				.prezzo(model.getPrezzo()).data(model.getData())
				.aperto(model.getAperto())
				.utenteInserimento(UtenteDTO.buildUtenteDTOFromModel(model.getUtenteInserimento(), false).getUsername())
				.categorieIds(model.getCategorie().stream().map(c -> c.getId()).collect(Collectors.toList())
						.toArray(new Long[] {}));
	}

	public static List<AnnuncioDTO> buildAnnuncioDTOListFromModelList(List<Annuncio> listAnnunci) {
		return listAnnunci.stream().map(annuncioItem -> {
			return AnnuncioDTO.buildAnnuncioDTOFromModel(annuncioItem);
		}).collect(Collectors.toList());
	}

}
