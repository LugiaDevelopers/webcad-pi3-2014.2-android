package com.lugia.webcad.entidade;

public class Reserva {

	private int id;
	private String titulo;
	private String subtitulo;
	private String curso;
	private String equipamento;
	private String data;
	private String tempo;
	
	public Reserva(String titulo, String subtitulo, String curso, String equipamento, String data, String tempo){
		this.titulo = titulo;
		this.subtitulo = subtitulo;
		this.curso = curso;
		this.equipamento = equipamento;
		this.data = data;
		this.tempo = tempo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(String equipamento) {
		this.equipamento = equipamento;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTempo() {
		return tempo;
	}

	public void setTempo(String tempo) {
		this.tempo = tempo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	
	
	
}
