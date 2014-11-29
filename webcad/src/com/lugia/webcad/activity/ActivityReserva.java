package com.lugia.webcad.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.lugia.webcad.R;
import com.lugia.webcad.entidade.Curso;
import com.lugia.webcad.entidade.Equipamento;
import com.lugia.webcad.entidade.Reserva;

public class ActivityReserva extends Activity {

	private int contadorObjetos;
	
	private Spinner spn1;
	private Spinner spn2;

	private List<String> nomesCursos = new ArrayList<String>();
	private List<String> nomesEquipamentos = new ArrayList<String>();

	private String nomeCurso;
	private String nomeEquipamento;

	private List<Curso> cursos;
	private List<Equipamento> equipamentos;

	// ## variaveis para pesquisa de equipamentos disponiveis
	private String nomeEquipPesquisa;
	private String idCursoPesquisa;
	
	private String dia;
	private String mes;
	private String ano;
	private String data;
	
	private List<Reserva> reservas;
	
	private String email;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserva);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Realizar Reserva");
		actionBar.setDisplayHomeAsUpEnabled(true);

		preencheCurso();
		preencheEquipamento();
		preencheData();
		
		Intent it = getIntent();
		email = it.getStringExtra("email");
	}

	private void preencheCurso() {
		acessaWebService(
				"http://192.168.25.7:8080/WebcadeService/curso/listarTodos", 1);

		spn1 = (Spinner) findViewById(R.id.id_spinner_curso);
		// Cria um ArrayAdapter usando um padr�o de layout da classe R do
		// android, passando o ArrayList nomes
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, nomesCursos);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spn1.setAdapter(spinnerArrayAdapter);

		// M�todo do Spinner para capturar o item selecionado
		spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int posicao, long id) {
				// pega nome pela posi��o
				nomeCurso = parent.getItemAtPosition(posicao).toString();
				// imprime um Toast na tela com o nome que foi selecionado

				for (int i = 0; i < cursos.size(); i++) {
					if (cursos.get(i).getNome().equalsIgnoreCase(nomeCurso) == true) {
						idCursoPesquisa = cursos.get(i).getId().toString();

					}

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	private void preencheEquipamento() {
		acessaWebService(
				"http://192.168.25.7:8080/WebcadeService/equipamento/listarTodos",
				2);

		spn2 = (Spinner) findViewById(R.id.id_spinner_equipamento);
		ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item,
				nomesEquipamentos);
		ArrayAdapter<String> spinnerArrayAdapter2 = arrayAdapter2;
		spinnerArrayAdapter2
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spn2.setAdapter(spinnerArrayAdapter2);

		spn2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int posicao, long id) {
				nomeEquipamento = parent.getItemAtPosition(posicao).toString();
				for (int i = 0; i < equipamentos.size(); i++) {
					if (equipamentos.get(i).getNome()
							.equalsIgnoreCase(nomeEquipamento) == true) {
						nomeEquipPesquisa = equipamentos.get(i).getNome();

					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void preencheData() {
		CalendarView calendarView = (CalendarView) findViewById(R.id.id_caledar_viewe);

		// quando selecionado alguma data diferente da padr�o
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {

			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				
				dia = ""+dayOfMonth;
				month = month+1;
				mes = ""+month;
				ano = ""+year;
				
				data = dia+"/"+mes+"/"+ano;
			}
		});
	}

	private void realisaBusca(){
		acessaWebService("http://192.168.25.7:8080/WebcadeService/reserva/buscarEquipamento/"+nomeEquipPesquisa+"&"+idCursoPesquisa+"&"+dia+"&"+mes+"&"+ano, 3);
		
	}
	
	private void realisaReserva(){
		acessaWebService("http://192.168.25.7:8080/WebcadeService/reserva/efetuarReserva/" +
				reservas.get(contadorObjetos-1).getId()+"&" +
				reservas.get(contadorObjetos-1).getTitulo()+"&"+dia+"&"+mes+"&"+ano+"&"+email, 0);
		
		
//		Toast.makeText(ActivityReserva.this,
//				reservas.get(contadorObjetos-1).getId()+"&" +
//						reservas.get(contadorObjetos-1).getTitulo()+"&"+dia+"&"+mes+"&"+ano+"&"+email,
//				Toast.LENGTH_SHORT).show();
	}
	public void acao(View view) {

	
		realisaBusca();
		
		
		AlertDialog.Builder alertaConfirmarReserva = new AlertDialog.Builder(
				ActivityReserva.this);
		
		if(contadorObjetos == 0){
			alertaConfirmarReserva.setIcon(R.drawable.ic_action_warning);
			
			alertaConfirmarReserva.setTitle("Equipamento Indisponivel");
			alertaConfirmarReserva.setMessage("N�o existe equipamentos dispon�veis para reserva na data e o curso escolhido.");
			alertaConfirmarReserva.setPositiveButton("Ok", null);
			alertaConfirmarReserva.show();
		
		}else{
		
			alertaConfirmarReserva.setIcon(R.drawable.ic_action_warning);
			
			alertaConfirmarReserva.setTitle("Confirmar Reserva?");
			alertaConfirmarReserva.setMessage("Disponibilisamos de "+contadorObjetos+" equipamento(s) para "+nomesCursos.get(Integer.parseInt(idCursoPesquisa)-1)+"."
							+ "\n\n" + "Deseja confirmar a reserva do equipamento?");

			// M�todo executado se escolher Sim
			alertaConfirmarReserva.setPositiveButton("Sim",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							realisaReserva();
							
							Toast.makeText(ActivityReserva.this,
									"Reserva Realizada com Sucesso!",
									Toast.LENGTH_SHORT).show();
							Intent itPrincipal = new Intent(ActivityReserva.this,
									PrincipalActivity.class);
							itPrincipal.putExtra("email", email);
							startActivity(itPrincipal);
						}
					});
			// M�todo executado se escolher N�o
			alertaConfirmarReserva.setNegativeButton("N�o",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							realisaReserva();
						}
					});

			alertaConfirmarReserva.show();
		}
		

	}

	private void acessaWebService(String url, int tipo) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try {
			if(tipo == 0){
				acessaWeb(url);
			}else{
				String json = acessaWeb(url);
				parseJSON(json, tipo);
			}
			

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Sem conex�o com a internet!", Toast.LENGTH_LONG).show();
		}
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
		String idCurso = null;
		String nomeCurso = null;

		Curso curso = null;

		// #####################

		String idEquipamento = null;
		String nomeEquipamento = null;
		String quantidade = null;
		String quantidadeAloc = null;
		
		Equipamento equipamento = null;
		
		//######################
		
		String ativo = null;
	    String descricao = null;
	    String disponivelParaLocacao = null;
	    String id = null;
	    String manutencao = null;
	    String numeroDeSerie = null;
	    String numeroTombamento = null;
	    		
	    String idTipoEquipamento = null;
	    String nomeTipoEquipamento = null;
	    String quantidadeTipoEquipamento = null;
	    String quantidadeAlocadaTipoEquipamento = null;
	    
	    String idReserva = null;
	    
	    Reserva reserva = null;
		try {

			if (tipo == 1) {

				cursos = new ArrayList<Curso>();
				JSONObject meuJson1 = new JSONObject(json);
				JSONArray cursoArray = meuJson1.getJSONArray("curso");

				for (int i = 0; i < cursoArray.length(); i++) {
					JSONObject cursoObject = cursoArray.getJSONObject(i);

					idCurso = cursoObject.getString("id");
					nomeCurso = cursoObject.getString("nome");

					curso = new Curso(idCurso, nomeCurso);
					cursos.add(curso);
					nomesCursos.add(curso.getNome());
				}

			}

			if (tipo == 2) {

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
					nomesEquipamentos.add(equipamento.getNome());
				}
			}
			
			if(tipo == 3){
				contadorObjetos = 0;
				reservas = new ArrayList<Reserva>();
				JSONObject meuJson3 = new JSONObject(json);
				JSONArray reservaArray = meuJson3.getJSONArray("reserva");
				
				//burlado com a gambiarra do servidor^^
				for (int j = 1; j < reservaArray.length(); j++) {
					JSONObject reservaObject = reservaArray.getJSONObject(j);
					
					
					JSONObject equipamentoJson = reservaObject.getJSONObject("equipamento");
					
					ativo = equipamentoJson.getString("ativo");
				    descricao = equipamentoJson.getString("descricao");
				    disponivelParaLocacao = equipamentoJson.getString("disponivelParaLocacao");
				    id = equipamentoJson.getString("id");
				    manutencao = equipamentoJson.getString("manutencao");
				    numeroDeSerie = equipamentoJson.getString("numeroDeSerie");
				    numeroTombamento = equipamentoJson.getString("numeroTombamento");
				    	
				    JSONObject tipoEquipamento = equipamentoJson.getJSONObject("tipoEquipamento");
				   	
				    idTipoEquipamento = tipoEquipamento.getString("id");
				    nomeTipoEquipamento = tipoEquipamento.getString("nome");
				    quantidadeTipoEquipamento = tipoEquipamento.getString("quantidade");
				    quantidadeAlocadaTipoEquipamento = tipoEquipamento.getString("quantidadeAlocada");
				    
				    idReserva = reservaObject.getString("id");
				    
				    reserva = new Reserva(Integer.parseInt(id), nomeTipoEquipamento, null, idCursoPesquisa, descricao, null, numeroDeSerie);
				    reservas.add(reserva);
				    contadorObjetos++;
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
