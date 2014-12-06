package com.lugia.webcad.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;

import com.lugia.webcad.entidade.Curso;
import com.lugia.webcad.entidade.Equipamento;
import com.lugia.webcad.entidade.Reserva;

public class ConnectionFactory {

	private String ip = "http://192.168.25.6:8080/";

	private ArrayList<Reserva> reservas; // preenchido com as reservas definidas
											// para um email especifico

	private ArrayList<Curso> cursos;
	
	private ArrayList<Equipamento> equipamentos;
	
	
	private String idCursoPesquisa;
	private int contadorObjetos;

	// Login ##############################################
	public boolean fazerLogin(String url) {
		boolean retorna = false;
		carregaStrictMode();

		try {
			String json = acessaWeb(ip + url);

			if (json.equalsIgnoreCase("true")) {
				retorna = true;
			}
		} catch (Exception e) {

		}

		return retorna;
	}

	// listar reservas ####################################
	public ArrayList<Reserva> listaDeReservas(String email) {
		carregaStrictMode();

		try {
			String json = acessaWeb(ip + "WebcadeService/reserva/listarTodas/"
					+ email);
			parseJSON(json, 1);
			return reservas;
		} catch (Exception e) {
			
			return reservas;
		}
	}

	// desistir reserva ##################################
	public boolean desistirReserva(String idReserva) {
		carregaStrictMode();

		try {
			acessaWeb(ip + "WebcadeService/reserva/desistirReserva/"
					+ idReserva);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public ArrayList<Curso> preencheCurso(){
		carregaStrictMode();

		try {
			String json = acessaWeb(ip + "WebcadeService/curso/listarTodos");
			parseJSON(json, 2);
			return cursos;
		} catch (Exception e) {
			return null;
		}
	}
	
	public ArrayList<Equipamento> preencheEquipamento(){
		carregaStrictMode();

		try {
			String json = acessaWeb(ip + "WebcadeService/equipamento/listarTodos");
			parseJSON(json, 3);
			return equipamentos;
		} catch (Exception e) {
			return null;
		}
	}
	
	public ArrayList<Reserva> realisaBusca(String nomeEquipPesquisa, String idCursoPesquisa, String dia, String mes, String ano ){
		this.idCursoPesquisa = idCursoPesquisa;
		carregaStrictMode();

		try {
			String json = acessaWeb(ip + "WebcadeService/reserva/buscarEquipamento/"+nomeEquipPesquisa+"&"+idCursoPesquisa+"&"+dia+"&"+mes+"&"+ano);
			parseJSON(json, 4);
			return reservas;
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public boolean realisaReserva(int id, String titulo, String dia, String mes, String ano, String email){
	
		carregaStrictMode();

		try {
			acessaWeb(ip + "WebcadeService/reserva/efetuarReserva/" +id+"&" +titulo+"&"+dia+"&"+mes+"&"+ano+"&"+email);
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	
	// Codigo padrao #######################################
	private void carregaStrictMode() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	private String acessaWeb(String link) {

		URL url;
		try {
			url = new URL(link);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setDoOutput(false);
			connection.setDoInput(true);

			connection.setRequestMethod("GET");
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String resposta = readString(connection.getInputStream());
				return resposta;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String readString(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				"iso-8859-1"));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = reader.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}

	private void parseJSON(String json, int tipo) {

		switch (tipo) {
		case 1:// para as reservas
			reservas = new ArrayList<Reserva>();

			String data = null;
			String ativo = null;
			String descricao = null;
			String disponivelParaLocacao = null;
			String id = null;
			String manutencao = null;
			String numeroDeSerie = null;
			String numeroTombamento = null;
			String idDeNovo = null; // (tipoEquipamento) << Novo objeto
			String nome = null;
			String qtd = null;
			String qtdAlocada = null;
			String idReserva = null;

			Reserva reserva = null;
			try {
				JSONObject meuJson = new JSONObject(json);
				JSONArray reservaArray = meuJson.getJSONArray("reserva");
				
				for (int i = 1; i < reservaArray.length(); i++) {
					JSONObject reservaObject = reservaArray.getJSONObject(i);
					data = reservaObject.getString("data");

					JSONObject equipamento = reservaObject
							.getJSONObject("equipamento");
					ativo = equipamento.getString("ativo");
					descricao = equipamento.getString("descricao");
					disponivelParaLocacao = equipamento
							.getString("disponivelParaLocacao");
					id = equipamento.getString("id");
					manutencao = equipamento.getString("manutencao");
					numeroDeSerie = equipamento.getString("numeroDeSerie");
					numeroTombamento = equipamento
							.getString("numeroTombamento");

					JSONObject tipoEquipamento = equipamento
							.getJSONObject("tipoEquipamento");
					idDeNovo = tipoEquipamento.getString("id");
					nome = tipoEquipamento.getString("nome");
					qtd = tipoEquipamento.getString("quantidade");
					qtdAlocada = tipoEquipamento.getString("quantidadeAlocada");

					idReserva = reservaObject.getString("id");

					reserva = new Reserva(Integer.parseInt(idReserva),
							"Reserva Realizada", "Mais informações", "-",
							descricao, data, numeroDeSerie);
					reservas.add(reserva);
				}
				
			} catch (JSONException e) {
//				reserva = new Reserva(0, "Não possui equip", "", "", "", "", "");
//				reservas.add(reserva);
			}

			break;

		case 2:
			String idCurso = null;
			String nomeCurso = null;

			Curso curso = null;

			cursos = new ArrayList<Curso>();
			try {
				JSONObject meuJson1 = new JSONObject(json);
				JSONArray cursoArray = meuJson1.getJSONArray("curso");

				for (int i = 0; i < cursoArray.length(); i++) {
					JSONObject cursoObject = cursoArray.getJSONObject(i);

					idCurso = cursoObject.getString("id");
					nomeCurso = cursoObject.getString("nome");

					curso = new Curso(idCurso, nomeCurso);
					cursos.add(curso);
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
				break;
			}
			break;
			
		case 3:
			
			String idEquipamento = null;
			String nomeEquipamento = null;
			String quantidade = null;
			String quantidadeAloc = null;
			
			Equipamento equipamento = null;
			
			
			try {
				equipamentos = new ArrayList<Equipamento>();
				JSONObject meuJson2 = new JSONObject(json);
				JSONArray equipamentoArray = meuJson2
						.getJSONArray("tipoEquipamento");

				for (int j = 0; j < equipamentoArray.length(); j++) {
					JSONObject equipamentoObject = equipamentoArray
							.getJSONObject(j);

					idEquipamento = equipamentoObject.getString("id");
					nomeEquipamento = equipamentoObject.getString("nome");
					quantidade = equipamentoObject.getString("quantidade");
					quantidadeAloc = equipamentoObject
							.getString("quantidadeAlocada");

					equipamento = new Equipamento(idEquipamento,
							nomeEquipamento);
					equipamentos.add(equipamento);
					
				}
			
			} catch (JSONException e) {
				e.printStackTrace();
				break;
			}
			break;
			
		case 4:
			
			String ativo4 = null;
		    String descricao4 = null;
		    String disponivelParaLocacao4 = null;
		    String id4 = null;
		    String manutencao4 = null;
		    String numeroDeSerie4 = null;
		    String numeroTombamento4 = null;
		    		
		    String idTipoEquipamento = null;
		    String nomeTipoEquipamento = null;
		    String quantidadeTipoEquipamento = null;
		    String quantidadeAlocadaTipoEquipamento = null;
		    
		    String idReserva4 = null;
		    
		    Reserva reserva4 = null;
			try{
				
				contadorObjetos = 0;
				reservas = new ArrayList<Reserva>();
				JSONObject meuJson3 = new JSONObject(json);
				JSONArray reservaArray = meuJson3.getJSONArray("reserva");
				
				//burlado com a gambiarra do servidor^^
				for (int j = 1; j < reservaArray.length(); j++) {
					JSONObject reservaObject = reservaArray.getJSONObject(j);
					
					
					JSONObject equipamentoJson = reservaObject.getJSONObject("equipamento");
					
					ativo4 = equipamentoJson.getString("ativo");
				    descricao4 = equipamentoJson.getString("descricao");
				    disponivelParaLocacao4 = equipamentoJson.getString("disponivelParaLocacao");
				    id4 = equipamentoJson.getString("id");
				    manutencao4 = equipamentoJson.getString("manutencao");
				    numeroDeSerie4 = equipamentoJson.getString("numeroDeSerie");
				    numeroTombamento4 = equipamentoJson.getString("numeroTombamento");
				    	
				    JSONObject tipoEquipamento = equipamentoJson.getJSONObject("tipoEquipamento");
				   	
				    idTipoEquipamento = tipoEquipamento.getString("id");
				    nomeTipoEquipamento = tipoEquipamento.getString("nome");
				    quantidadeTipoEquipamento = tipoEquipamento.getString("quantidade");
				    quantidadeAlocadaTipoEquipamento = tipoEquipamento.getString("quantidadeAlocada");
				    
				    idReserva4 = reservaObject.getString("id");
				    
				    reserva = new Reserva(Integer.parseInt(id4), nomeTipoEquipamento, null, idCursoPesquisa, descricao4, null, numeroDeSerie4);
				    reservas.add(reserva);
				    contadorObjetos++;
				}
			}catch (JSONException e) {
				e.printStackTrace();
				break;
			}
			
			break;
			
		default:
			break;
		}

	}
}
