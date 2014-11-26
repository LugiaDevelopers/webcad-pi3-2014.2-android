package com.lugia.webcad.adapters;

import java.util.List;

import com.lugia.webcad.R;
import com.lugia.webcad.R.drawable;
import com.lugia.webcad.R.id;
import com.lugia.webcad.R.layout;
import com.lugia.webcad.entidade.Reserva;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReservaAdapter extends BaseAdapter {

	private List<Reserva> reservas;
	private Context context;

	public ReservaAdapter(List<Reserva> reservas, Context context) {
		this.reservas = reservas;
		this.context = context;
	}

	// Quantidad de elementos que o adapter possui
	@Override
	public int getCount() {
		return this.reservas.size();
	}

	@Override
	public Object getItem(int position) {
		return this.reservas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Converter um xml e jogar no java
		LayoutInflater inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View linhaView = inflater.inflate(R.layout.item_lista_reserva, null);
		TextView txtTitulo = (TextView) linhaView
				.findViewById(R.id.id_txt_titulo);
		TextView txtSubTitulo = (TextView) linhaView
				.findViewById(R.id.id_txt_subtitulo);
		ImageView img = (ImageView) linhaView
				.findViewById(R.id.id_img_principal);

		ImageView imgReserva = (ImageView) linhaView
				.findViewById(R.id.id_img_reserva);

		// setando as informações na tela
		Reserva reserva = this.reservas.get(position);
		txtTitulo.setText(reserva.getTitulo());
		txtSubTitulo.setText(reserva.getSubtitulo());

		imgReserva.setImageResource(R.drawable.ic_datashow);

		img.setImageResource(R.drawable.ic_action_cancel);

		return linhaView;
	}

}
