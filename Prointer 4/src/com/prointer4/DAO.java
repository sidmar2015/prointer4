package com.prointer4;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


public class DAO {
	
	SQLiteDatabase db;
	StringBuilder sqlCmd;
	int id;
	Calendar calendario;
	String posto;
	String data;
	String hora;
	
	//função que salva os dados
	public void salvar(){
		try{
							
	
			//abre o banco	
			db = SQLiteDatabase.openOrCreateDatabase("db_acesso.db", null);// openOrCreateDatabase("db_acesso.db", Context.MODE_PRIVATE, null);
			
			
			sqlCmd = new StringBuilder(); 
			
			sqlCmd.append("CREATE TABLE IF NOT EXISTS tb_dados(");
			sqlCmd.append("_id INTEGER PRIMARY KEY, ");
			sqlCmd.append("posto TEXT, ");
			sqlCmd.append("data TEXT, ");
			sqlCmd.append("hora TEXT);");
			db.execSQL(sqlCmd.toString());
			
			//
			ContentValues ctv = new ContentValues();
			ctv.put("posto", posto);
			ctv.put("data",  data);
			ctv.put("hora",  hora);
			
			//insere os dados no banco
			db.insert("tb_dados", null, ctv);
			db.close();
			//Toast.makeText(getBaseContext(), "BANCO CRIADO "+ctv.toString(), Toast.LENGTH_LONG).show();
			
			 }catch(Exception ex){
				 //Toast.makeText(getBaseContext(), "FALHA AO CRIAR BANCO"+ex.getMessage(), Toast.LENGTH_LONG).show(); 
				
			 }
			
}
	
	public void consultar(){
		try{
			
		}catch(Exception ex){
			
		}
		
	}

}
