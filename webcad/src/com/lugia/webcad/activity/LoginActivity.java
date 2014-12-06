package com.lugia.webcad.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lugia.webcad.R;
import com.lugia.webcad.dao.ConnectionFactory;

public class LoginActivity extends Activity {
	private ConnectionFactory conexao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		conexao = new ConnectionFactory();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Login");
	}

	public void acao(View view) {
		String useEmail = "";
		String useSenha = "";

		EditText email = (EditText) findViewById(R.id.id_campo_email);
		EditText senha = (EditText) findViewById(R.id.id_campo_senha);

		useEmail = email.getText().toString();
		useSenha = senha.getText().toString();
		
		if (useEmail != "" && useSenha != "") {
			if (conexao.fazerLogin("WebcadeService/login/fazerLogin/"+ useEmail + "&" + useSenha) == true) {
				Intent it = new Intent(this, PrincipalActivity.class);
				it.putExtra("email", useEmail);
				startActivity(it);
			} else {
				Toast.makeText(getApplicationContext(), "Usuario invalido",
						Toast.LENGTH_SHORT).show();
			}
		}

	}


}
