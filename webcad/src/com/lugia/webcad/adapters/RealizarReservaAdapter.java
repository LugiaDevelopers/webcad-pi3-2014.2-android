package com.lugia.webcad.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lugia.webcad.R;
import com.lugia.webcad.entidade.RealizarReserva;

public class RealizarReservaAdapter extends BaseAdapter {

	private List<RealizarReserva> realizarReservas;
	private Context context;

	public RealizarReservaAdapter(List<RealizarReserva> realizarReservas,
			Context context) {
		this.realizarReservas = realizarReservas;
		this.context = context;
	}

	@Override
	public int getCount() {
		return this.realizarReservas.size();
	}

	@Override
	public Object getItem(int position) {
		return this.realizarReservas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater li = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View linhaView = li.inflate(R.layout.item_lista_realizar_reserva, null);

		TextView txtCurso = (TextView) linhaView
				.findViewById(R.id.id_item_curso);
		TextView txtEquipamento = (TextView) linhaView
				.findViewById(R.id.id_item_equipamento);
		TextView txtData = (TextView) linhaView.findViewById(R.id.id_item_data);

		RealizarReserva realizarReserva = this.realizarReservas.get(position);
		txtCurso.setText(realizarReserva.getCurso());
		txtEquipamento.setText(realizarReserva.getEquipamento());
		txtData.setText(realizarReserva.getData());
			
		return linhaView;
	}

}
