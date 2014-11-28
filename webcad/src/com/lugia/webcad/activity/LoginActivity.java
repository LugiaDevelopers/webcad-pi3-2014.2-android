package com.lugia.webcad.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.lugia.webcad.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
			if (fazerLogin("http://192.168.25.7:8080/WebcadeService/login/fazerLogin/"+ useEmail + "&" + useSenha) == true) {
				Intent it = new Intent(this, PrincipalActivity.class);
				startActivity(it);
			} else {
				Toast.makeText(getApplicationContext(), "Usuario invalido",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	private boolean fazerLogin(String url) {
		boolean retorna = false;
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try {
			String json = acessaWeb(url);
			
			if (json.equalsIgnoreCase("true")) {
				retorna = true;
			}
		} catch (Exception e) {
			
		}

		return retorna;
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
}
