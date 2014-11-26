package com.lugia.webcad.activity;

import com.lugia.webcad.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class InicialActivity extends Activity {

	private static int TIME = 3000;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicial);
		ActionBar actionBar = getActionBar();
		actionBar.hide();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(InicialActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}, TIME);
		{

		}

	}
}
