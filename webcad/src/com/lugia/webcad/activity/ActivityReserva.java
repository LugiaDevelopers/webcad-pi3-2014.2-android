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
import android.widget.Spinner;
import android.widget.Toast;

import com.lugia.webcad.R;
import com.lugia.webcad.entidade.Curso;
import com.lugia.webcad.entidade.Equipamento;
import com.lugia.webcad.entidade.Reserva;

public class ActivityReserva extends Activity {

	private Spinner spn1;
	private Spinner spn2;
	
	private List<String> nomesCursos = new ArrayList<String>();
	private List<String> nomesEquipamentos = new ArrayList<String>();
	
	private String nomeCurso;
	private String nomeEquipamento;
	
	private List<Curso> cursos;
	private List<Equipamento> equipamentos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserva);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Realizar Reserva");
		actionBar.setDisplayHomeAsUpEnabled(true);


//		acessaWebService("http://192.168.25.7:8080/WebcadeService/curso/listarTodos", 1);
//		
//		
//		// Identifica o Spinner no layout
//		spn1 = (Spinner) findViewById(R.id.id_spinner_curso);
//		// Cria um ArrayAdapter usando um padrão de layout da classe R do
//		// android, passando o ArrayList nomes
//		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, nomesCursos);
//		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
//		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//		spn1.setAdapter(spinnerArrayAdapter);
//
//		//Método do Spinner para capturar o item selecionado
//		spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
//				//pega nome pela posição
//				nomeCurso = parent.getItemAtPosition(posicao).toString();
//				//imprime um Toast na tela com o nome que foi selecionado
//				
//				for (int i = 0; i < cursos.size(); i++) {
//					if(cursos.get(i).getNome().equalsIgnoreCase(nomeCurso)== true){
//						Toast.makeText(ActivityReserva.this, " Curso: " + nomeCurso+"\nId: "+cursos.get(i).getId(), Toast.LENGTH_LONG).show();
//					}
//				}
//				
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//
//			}
//		});
		
		acessaWebService("http://192.168.25.7:8080/WebcadeService/equipamento/listarTodos", 2);
		
		// Identifica o Spinner no layout
				spn2 = (Spinner) findViewById(R.id.id_spinner_equipamento);
				// Cria um ArrayAdapter usando um padrão de layout da classe R do
				// android, passando o ArrayList nomes
				ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, nomesEquipamentos);
				ArrayAdapter<String> spinnerArrayAdapter2 = arrayAdapter2;
				spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
				spn2.setAdapter(spinnerArrayAdapter2);

				//Método do Spinner para capturar o item selecionado
				spn2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
						//pega nome pela posição
						nomeEquipamento = parent.getItemAtPosition(posicao).toString();
						//imprime um Toast na tela com o nome que foi selecionado
						
						for (int i = 0; i < equipamentos.size(); i++) {
							if(equipamentos.get(i).getNome().equalsIgnoreCase(nomeEquipamento)== true){
								Toast.makeText(ActivityReserva.this, " Curso: " + nomeEquipamento+"\nId: "+equipamentos.get(i).getId(), Toast.LENGTH_LONG).show();
							}
						}
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
	}

		
		

//		// Foi definido um array de Strings para popular o spinner
//		Spinner spinnerEquipamentos = (Spinner) findViewById(R.id.id_spinner_equipamento);
//		ArrayAdapter<CharSequence> adapterEquipamentos = ArrayAdapter
//				.createFromResource(this, R.array.array_itens_equipamentos,
//						R.layout.item_spinner);
//		adapterEquipamentos
//				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinnerEquipamentos.setAdapter(adapterEquipamentos);
//
//	}

	public void acao(View view) {

		AlertDialog.Builder alertaConfirmarReserva = new AlertDialog.Builder(
				ActivityReserva.this);
		alertaConfirmarReserva.setIcon(R.drawable.ic_action_warning);
		alertaConfirmarReserva.setTitle("Confirmar Reserva?");
		alertaConfirmarReserva
				.setMessage("Existem 02 equipamentos disponíveis para reserva na data e o curso escolhido."
						+ "\n\n" + "Deseja confirmar a reserva do equipamento?");

		// Método executado se escolher Sim
		alertaConfirmarReserva.setPositiveButton("Sim",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Toast.makeText(ActivityReserva.this,
								"Reserva Realizada com Sucesso!",
								Toast.LENGTH_SHORT).show();
						Intent itPrincipal = new Intent(ActivityReserva.this,
								PrincipalActivity.class);
						startActivity(itPrincipal);
					}
				});
		// Método executado se escolher Não
		alertaConfirmarReserva.setNegativeButton("Não",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});

		alertaConfirmarReserva.show();

	}

	
	private void acessaWebService(String url,int tipo) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try {
			String json = acessaWeb(url);
			Toast.makeText(getApplicationContext(),
					json, Toast.LENGTH_LONG).show();
			parseJSON(json,tipo);
			
			
			
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Sem conexão com a internet!", Toast.LENGTH_LONG).show();
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
		cursos = new ArrayList<Curso>();
		
//#####################
		
		String idEquipamento = null;
		String nomeEquipamento = null;
		String quantidade = null;
		String quantidadeAloc = null;
		
		Equipamento equipamento = null;
		equipamentos = new ArrayList<Equipamento>();
		try {
			
			if(tipo == 1){
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
			
			if(tipo == 2){
				JSONObject meuJson2 = new JSONObject(json);
				JSONArray equipamentoArray = meuJson2.getJSONArray("tipoEquipamento");
				Toast.makeText(getApplicationContext(),
						equipamentoArray.length(), Toast.LENGTH_LONG).show();
//				for (int j = 0; j < equipamentoArray.length(); j++) {
//					JSONObject equipamentoObject = equipamentoArray.getJSONObject(j);
//					
//					idCurso = equipamentoObject.getString("id");
//					nomeCurso = equipamentoObject.getString("nome");
//					quantidade = equipamentoObject.getString("quantidade");
//					quantidadeAloc = equipamentoObject.getString("quantidadeAlocada");
//					
//					equipamento = new Equipamento(idEquipamento, nomeEquipamento);
//					equipamentos.add(equipamento);
//					nomesEquipamentos.add(equipamento.getNome());
//				}
			}
			
			

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
