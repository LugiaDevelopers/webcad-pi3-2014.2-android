package com.lugia.webcad.activity;

import com.lugia.webcad.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Login");
	}
	
	public void acao(View view){
		
		Intent it = new Intent(this, PrincipalActivity.class);
		startActivity(it);
		
	}
	
}
