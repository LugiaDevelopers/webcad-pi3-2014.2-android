package com.lugia.webcad.entidade;

public class RealizarReserva {

	private String curso;
	private String equipamento;
	private String data;

	public RealizarReserva(String curso, String equipamento, String data) {
		this.curso = curso;
		this.equipamento = equipamento;
		this.data = data;

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

}
