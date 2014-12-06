package com.lugia.webcad.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.lugia.webcad.R;
import com.lugia.webcad.adapters.ReservaAdapter;
import com.lugia.webcad.dao.ConnectionFactory;
import com.lugia.webcad.entidade.Reserva;

public class PrincipalActivity extends ListActivity {
	private ConnectionFactory conexao;
	private ArrayList<Reserva> reservas;//lista com todos os dados do array
	private String emailUsuario;//local para salvar email do usuario logado
	private int idReserva; // id da reserva obitido no metodo onListItemClick();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		conexao = new ConnectionFactory();
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


	private void listaDeReservas(String email) {
		try {
			reservas = conexao.listaDeReservas(email);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Nenhum equipamento reservado", Toast.LENGTH_LONG).show();
		}
		
	}


	
	private void desistirReserva(String idReserva) {
		if(conexao.desistirReserva(idReserva) == true){
			Toast.makeText(PrincipalActivity.this,
					"Reserva cancelada com sucesso!",
					Toast.LENGTH_LONG).show();
			Intent it = new Intent(this, PrincipalActivity.class);
			it.putExtra("email", emailUsuario);
			startActivity(it);
		}else{
			Toast.makeText(getApplicationContext(),
					"Sem conexão com a internet!", Toast.LENGTH_LONG).show();
		}
		
		
		
	}

}