package com.lugia.webcad.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.lugia.webcad.R;

public class ActivityReserva extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserva);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Realizar Reserva");
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Foi definido um array de Strings para popular o spinner
		Spinner spinnerCursos = (Spinner) findViewById(R.id.id_spinner_curso);
		ArrayAdapter<CharSequence> adapterCursos = ArrayAdapter
				.createFromResource(this, R.array.array_itens_cursos,
						R.layout.item_spinner);
		
		adapterCursos
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCursos.setAdapter(adapterCursos);

		// Foi definido um array de Strings para popular o spinner
		Spinner spinnerEquipamentos = (Spinner) findViewById(R.id.id_spinner_equipamento);
		ArrayAdapter<CharSequence> adapterEquipamentos = ArrayAdapter
				.createFromResource(this, R.array.array_itens_equipamentos,
						R.layout.item_spinner);
		adapterEquipamentos
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerEquipamentos.setAdapter(adapterEquipamentos);

	}

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

}
