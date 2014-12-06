package com.lugia.webcad.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.lugia.webcad.R;
import com.lugia.webcad.dao.ConnectionFactory;
import com.lugia.webcad.entidade.Curso;
import com.lugia.webcad.entidade.Equipamento;
import com.lugia.webcad.entidade.Reserva;

public class ActivityReserva extends Activity {
	private ConnectionFactory conexao;

	private Spinner spn1;
	private Spinner spn2;

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
		conexao = new ConnectionFactory();
		diaHoje();
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
		cursos = new ArrayList<Curso>();
		cursos.addAll(conexao.preencheCurso());

		ArrayList<String> nomesCursos2 = new ArrayList<String>();
		for (int i = 0; i < cursos.size(); i++) {
			nomesCursos2.add(cursos.get(i).getNome());
		}

		spn1 = (Spinner) findViewById(R.id.id_spinner_curso);
		// Cria um ArrayAdapter usando um padrão de layout da classe R do
		// android, passando o ArrayList nomes
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, nomesCursos2);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spn1.setAdapter(spinnerArrayAdapter);

		// Método do Spinner para capturar o item selecionado
		spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int posicao, long id) {
				// pega nome pela posição
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
		equipamentos = new ArrayList<Equipamento>();
		equipamentos.addAll(conexao.preencheEquipamento());

		ArrayList<String> nomesEquipamentos2 = new ArrayList<String>();
		for (int i = 0; i < equipamentos.size(); i++) {
			nomesEquipamentos2.add(equipamentos.get(i).getNome());
		}

		spn2 = (Spinner) findViewById(R.id.id_spinner_equipamento);
		ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item,
				nomesEquipamentos2);
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

		// quando selecionado alguma data diferente da padrão
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {

			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {

				dia = "" + dayOfMonth;
				month = month + 1;
				mes = "" + month;
				ano = "" + year;

				data = dia + "/" + mes + "/" + ano;
			}
		});
	}

	public void acao(View view) {
		reservas = new ArrayList<Reserva>();
		reservas.addAll(conexao.realisaBusca(nomeEquipPesquisa,
				idCursoPesquisa, dia, mes, ano));

		String nomeDoCurso = "";

		for (int i = 0; i < cursos.size(); i++) {
			if (cursos.get(i).getId().equalsIgnoreCase(idCursoPesquisa)) {
				nomeDoCurso = cursos.get(i).getNome();
			}
		}

		AlertDialog.Builder alertaConfirmarReserva = new AlertDialog.Builder(
				ActivityReserva.this);

		if (reservas.size() == 0 || verificaDataReserva() == true) {
			alertaConfirmarReserva.setIcon(R.drawable.ic_action_warning);

			alertaConfirmarReserva.setTitle("Equipamento Indisponivel");
			alertaConfirmarReserva
					.setMessage("Não existe equipamentos disponíveis para reserva na data e o curso escolhido.");
			alertaConfirmarReserva.setPositiveButton("Ok", null);
			alertaConfirmarReserva.show();

		} else {

			alertaConfirmarReserva.setIcon(R.drawable.ic_action_warning);

			alertaConfirmarReserva.setTitle("Confirmar Reserva?");
			alertaConfirmarReserva.setMessage("Disponibilisamos de "
					+ reservas.size() + " equipamento(s) para " + nomeDoCurso
					+ "." + "\n\n"
					+ "Deseja confirmar a reserva do equipamento?");

			// Método executado se escolher Sim
			alertaConfirmarReserva.setPositiveButton("Sim",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							realisaReserva();

						}
					});
			// Método executado se escolher Não
			alertaConfirmarReserva.setNegativeButton("Não",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// realisaReserva();
						}
					});

			alertaConfirmarReserva.show();
		}

	}

	private void realisaReserva() {
		int idEquipamento = 0;
		String tipoEquipamento = "";
		for (int i = 0; i < reservas.size();) {
			idEquipamento = reservas.get(i).getId();
			tipoEquipamento = reservas.get(i).getTitulo();

			break;
		}

		conexao.realisaReserva(idEquipamento, tipoEquipamento, dia, mes, ano,
				email);

		Toast.makeText(ActivityReserva.this,
		"Reserva Realizada com Sucesso!",
		Toast.LENGTH_SHORT).show();
		
		Intent itPrincipal = new Intent(ActivityReserva.this,
				PrincipalActivity.class);
		itPrincipal.putExtra("email", email);
		startActivity(itPrincipal);

	}

	private boolean verificaDataReserva() {
		boolean retorno = false;

		int diaHoje;
		int mesHoje;
		int anoHoje;

		String token[] = getDate().split("/");

		diaHoje = Integer.parseInt(token[0]);
		mesHoje = Integer.parseInt(token[1]);
		anoHoje = Integer.parseInt(token[2]);

		if (anoHoje > Integer.parseInt(ano)) {
			retorno = true;
		}
		if (mesHoje > Integer.parseInt(mes)) {
			retorno = true;
			if (anoHoje < Integer.parseInt(ano)) {
				retorno = false;
			}
		}

		if (diaHoje > Integer.parseInt(dia)) {
			retorno = true;
			if (mesHoje < Integer.parseInt(mes)) {
				retorno = false;

			}

			if (anoHoje < Integer.parseInt(ano)) {
				retorno = false;
			}
		}

		return retorno;
	}

	private String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		
		
		
		return dateFormat.format(date);
	}

	private void diaHoje(){
		String token[] = getDate().split("/");
		dia = token[0];
		mes = token[1];
		ano = token[2];
		
	}
}
