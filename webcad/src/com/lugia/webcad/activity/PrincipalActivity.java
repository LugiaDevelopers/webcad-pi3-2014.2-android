package com.lugia.webcad.activity;

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

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.lugia.webcad.R;
import com.lugia.webcad.adapters.ReservaAdapter;
import com.lugia.webcad.entidade.Reserva;

public class PrincipalActivity extends ListActivity {
	private ArrayList<Reserva> reservas;//lista com todos os dados do array
	private String emailUsuario;//local para salvar email do usuario logado
	private int idReserva; // id da reserva obitido no metodo onListItemClick();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_principal);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Reservas realizadas");
		// pegando email da tela de login
		Intent it = getIntent();
		String email = it.getStringExtra("email");
		// metodo para ler o json com base no email passado e colocar seu
		// resultado no ArrayList - reservas
		listaDeReservas(email);
		//aqui salvamos o email do usuario na pagina para atualizações da mesma 
		emailUsuario = email;
		
		ReservaAdapter reservaAdapeter = new ReservaAdapter(reservas, this);
		ListView lista = (ListView) findViewById(android.R.id.list);
		lista.setAdapter(reservaAdapeter);

	}

	/* trecho de código que tá chamando o ActionBar */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_tela_principal, menu);
		return super.onCreateOptionsMenu(menu);

	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.id_adicionar_reserva:
			Intent it = new Intent(this, ActivityReserva.class);
			it.putExtra("email", emailUsuario);
			startActivity(it);

			return true;

		case R.id.id_about:

			Intent telaAbout = new Intent();
			telaAbout.setClass(this, ActivityAbout.class);
			startActivity(telaAbout);

			return true;

		case R.id.id_sair:

			AlertDialog.Builder alertaSair = new AlertDialog.Builder(
					PrincipalActivity.this);
			alertaSair.setIcon(R.drawable.ic_action_warning);
			alertaSair.setTitle("Warning!");
			alertaSair.setMessage("Você tem certeza que deseja sair?");

			// Método executado se escolher Sim
			alertaSair.setPositiveButton("Sim",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Intent itLogin = new Intent(PrincipalActivity.this,
									LoginActivity.class);
							startActivity(itLogin);
						}
					});
			// Método executado se escolher Não
			alertaSair.setNegativeButton("Não",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

						}
					});

			alertaSair.show();
			return true;

		case android.R.id.home:

			Toast.makeText(PrincipalActivity.this, "Você já está na home",
					Toast.LENGTH_SHORT).show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	/* Selecionando um item de uma lista e executando uma ação apartir dele */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		idReserva = reservas.get(position).getId();//salva o id da reserva na variavel idReserva referente a posição selecionada
		
//		Toast.makeText(PrincipalActivity.this,
//				""+idReserva,
//				Toast.LENGTH_LONG).show();
		
		AlertDialog.Builder alerta = new AlertDialog.Builder(this)
				.setTitle("Informações da Reserva")
				.setMessage(
						"Equipamento : "
								+ reservas.get(position).getEquipamento()
								+ "\n" + "Numero de Serie : "
								+ reservas.get(position).getNumeroDeSerie()
								+ "\n" + "Data da reserva : "
								+ reservas.get(position).getData())
				.setIcon(R.drawable.ic_action_about_preto)
				.setPositiveButton("Cancelar Reserva", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						AlertDialog.Builder alertCancelarReserva = new AlertDialog.Builder(
								PrincipalActivity.this);
						alertCancelarReserva.setIcon(R.drawable.ic_action_warning);
						alertCancelarReserva.setTitle("Cancelar Reserva?");
						alertCancelarReserva
								.setMessage("Você tem certeza que deseja cancelar a reserva selecionada?");

						// Método executado se escolher Sim
						alertCancelarReserva.setPositiveButton("Sim",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										// conecta com o webservice e apaga o item selecionado
										desistirReserva(""+idReserva);
										

									}
								});
						// Método executado se escolher Não
						alertCancelarReserva.setNegativeButton("Não",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {

									}
								});

						alertCancelarReserva.show();
					}
				});

		alerta.show();

	}

	
//	public void cancelarReserva(View view) {
//		
//		AlertDialog.Builder alertCancelarReserva = new AlertDialog.Builder(
//				PrincipalActivity.this);
//		alertCancelarReserva.setIcon(R.drawable.ic_action_warning);
//		alertCancelarReserva.setTitle("Cancelar Reserva?");
//		alertCancelarReserva
//				.setMessage("Você tem certeza que deseja cancelar a reserva selecionada?");
//
//		// Método executado se escolher Sim
//		alertCancelarReserva.setPositiveButton("Sim",
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						// conecta com o webservice e apaga o item selecionado
//						desistirReserva(""+idReserva);
//						
//
//					}
//				});
//		// Método executado se escolher Não
//		alertCancelarReserva.setNegativeButton("Não",
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//
//					}
//				});
//
//		alertCancelarReserva.show();
//
//	}

	private void listaDeReservas(String email) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try {
			String json = acessaWeb("http://192.168.25.7:8080/WebcadeService/reserva/listarTodas/"
					+ email);
			parseJSON(json);
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

	private void parseJSON(String json) {
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
				numeroTombamento = equipamento.getString("numeroTombamento");

				JSONObject tipoEquipamento = equipamento
						.getJSONObject("tipoEquipamento");
				idDeNovo = tipoEquipamento.getString("id");
				nome = tipoEquipamento.getString("nome");
				qtd = tipoEquipamento.getString("quantidade");
				qtdAlocada = tipoEquipamento.getString("quantidadeAlocada");

				idReserva = reservaObject.getString("id");

				reserva = new Reserva(Integer.parseInt(idReserva),"Reserva Realizada", "Mais informações",
						"-", descricao, data, numeroDeSerie);
				reservas.add(reserva);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// desistir da reserva
	private void desistirReserva(String idReserva) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try {
			acessaWeb("http://192.168.25.7:8080/WebcadeService/reserva/desistirReserva/"
					+ idReserva);
			
			Toast.makeText(PrincipalActivity.this,
					"Reserva cancelada com sucesso!",
					Toast.LENGTH_LONG).show();
			Intent it = new Intent(this, PrincipalActivity.class);
			it.putExtra("email", emailUsuario);
			startActivity(it);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Sem conexão com a internet!", Toast.LENGTH_LONG).show();
		}
	}

}