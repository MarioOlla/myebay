package it.prova.myebay.dto.utente;

import javax.validation.constraints.NotBlank;

public class UtenteChangePasswordDTO {
	
	@NotBlank(message = "{username.notblank}")
	private String username;
	@NotBlank(message = "{username.notblank}")
	private String password;
	@NotBlank(message = "{username.notblank}")
	private String nuovaPassword;
	@NotBlank(message = "{username.notblank}")
	private String confermaNuovaPassword;
	
	public UtenteChangePasswordDTO(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNuovaPassword() {
		return nuovaPassword;
	}

	public void setNuovaPassword(String nuovaPassword) {
		this.nuovaPassword = nuovaPassword;
	}

	public String getConfermaNuovaPassword() {
		return confermaNuovaPassword;
	}

	public void setConfermaNuovaPassword(String confermaNuovaPassword) {
		this.confermaNuovaPassword = confermaNuovaPassword;
	}
	
	
}
