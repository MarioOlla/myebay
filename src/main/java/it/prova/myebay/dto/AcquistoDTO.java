package it.prova.myebay.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import it.prova.myebay.dto.utente.UtenteDTO;
import it.prova.myebay.model.Acquisto;

public class AcquistoDTO {

	private Long id;
	@NotBlank
	private String descrizione;

	private Date data;
	@NotNull
	private Integer prezzo;
	@NotNull
	private UtenteDTO utenteAcquirente;

	public AcquistoDTO(String descrizione, Date data, Integer prezzo, UtenteDTO utenteAcquirente) {
		this.descrizione = descrizione;
		this.data = data;
		this.prezzo = prezzo;
		this.utenteAcquirente = utenteAcquirente;
	}

	public AcquistoDTO(Long id, String descrizione, Date data, Integer prezzo, UtenteDTO utenteAcquirente) {
		this.id = id;
		this.descrizione = descrizione;
		this.data = data;
		this.prezzo = prezzo;
		this.utenteAcquirente = utenteAcquirente;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Integer prezzo) {
		this.prezzo = prezzo;
	}

	public UtenteDTO getUtenteAcquirente() {
		return utenteAcquirente;
	}

	public void setUtenteAcquirente(UtenteDTO utenteAcquirente) {
		this.utenteAcquirente = utenteAcquirente;
	}

	public Acquisto buildAcquistoModel(boolean includeId) {
		Acquisto result = new Acquisto(this.descrizione, this.data, this.prezzo,
				this.utenteAcquirente.buildUtenteModel(false));
		if (this.id != null && includeId) {
			result.setId(this.id);
		}
		return result;
	}

	public static AcquistoDTO buildAcquistoDTOFromModel(Acquisto model) {
		return new AcquistoDTO(model.getId(), model.getDescrizione(), model.getData(), model.getPrezzo(),
				UtenteDTO.buildUtenteDTOFromModel(model.getUtenteAcquirente(), false));
	}

	public static List<AcquistoDTO> buildAcquistoDtoListFromModelList(List<Acquisto> models) {
		return models.stream().map(acquisto -> {
			return AcquistoDTO.buildAcquistoDTOFromModel(acquisto);
		}).collect(Collectors.toList());
	}
}
