package com.lugia.webcad.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_principal);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Reservas realizadas");

		List<Reserva> reservas = new ArrayList<Reserva>();
		reservas.add(new Reserva("Reserva realizada", "Mais informações",
				"Curso", "Equipamento", "dd/MM/yyyy", "01:23:12"));
		reservas.add(new Reserva("Reserva realizada", "Mais informações",
				"Curso", "Equipamento", "dd/MM/yyyy", "01:23:12"));
		reservas.add(new Reserva("Reserva realizada", "Mais informações",
				"Curso", "Equipamento", "dd/MM/yyyy", "01:23:12"));
		reservas.add(new Reserva("Reserva realizada", "Mais informações",
				"Curso", "Equipamento", "dd/MM/yyyy", "01:23:12"));
		reservas.add(new Reserva("Reserva realizada", "Mais informações",
				"Curso", "Equipamento", "dd/MM/yyyy", "01:23:12"));
		reservas.add(new Reserva("Reserva realizada", "Mais informações",
				"Curso", "Equipamento", "dd/MM/yyyy", "01:23:12"));

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
			startActivity(it);

			return true;

		case R.id.id_about:

			Intent telaAbout = new Intent();
			telaAbout.setClass(this,  ActivityAbout.class);
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

		LayoutInflater alertInfo = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View textEntryView = alertInfo.inflate(
				R.layout.activity_informacao, null);

		AlertDialog.Builder alerta = new AlertDialog.Builder(this)
				.setTitle("Informações da Reserva")
				.setIcon(R.drawable.ic_action_about_preto).setView(textEntryView)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});

		alerta.show();

	}

	public void cancelarReserva(View view){
		
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
						Toast.makeText(PrincipalActivity.this,
								"Reserva cancelada com sucesso!",
								Toast.LENGTH_LONG).show();

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
	
	
}
