package com.prointer4;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ConsultarActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consultar);
		
		//cria a instancia do manipulador de banco de dados
		DAO banco_dados = new DAO();
		//cria a instancia do listview para exibir os dados
		ListView consulta = (ListView)findViewById(R.id.lvwConsultar);
		//cria a instancia de um cursor para efetuar as consultas
		Cursor cursor = banco_dados.consultar(this);
		 //define de onde vem os dados
		 String[] from = { "_id", "posto", "data", "hora"};
		 //define para onde vão os dados
	        int[] to = {R.id.txvSenha, R.id.txvPosto, R.id.txvData, R.id.txvHora};
		
		//cria um cursor adapter para passar os dados para a listview
		SimpleCursorAdapter ad = new SimpleCursorAdapter(getBaseContext(), R.layout.model_consultar, cursor, from, to);
		
		//adiciona os dados ao listview
		consulta.setAdapter(ad);
		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.consultar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
