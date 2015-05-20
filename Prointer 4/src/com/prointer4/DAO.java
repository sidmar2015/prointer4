package com.prointer4;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


public class DAO {
	
	SQLiteDatabase db;
	StringBuilder sqlCmd;
	int id;
	Date calendario = new Date();
	private String data;
	private String hora;
	private String posto;
	private Cursor cursor;
	
	public void setPosto(String posto) {
		this.posto = posto;
	}
				
	public String getPosto() {
		
		return posto;
	}


	public String getData() {
		data = calendario.getDate()+"/"+(calendario.getMonth()+1)+"/"+calendario.getYear();
		return data;
	}


	public String getHora() {
		hora = calendario.getHours()+":"+calendario.getMinutes();
		return hora;
	}


	
	
	
	
	//função que salva os dados
	public void salvar(Context context){
		try{
							
	
			//abre o banco	
			db = SQLiteDatabase.openOrCreateDatabase("db_acesso.db", null);
			
			
			sqlCmd = new StringBuilder(); 
			
			sqlCmd.append("CREATE TABLE IF NOT EXISTS tb_dados(");
			sqlCmd.append("_id INTEGER PRIMARY KEY, ");
			sqlCmd.append("posto TEXT, ");
			sqlCmd.append("data TEXT, ");
			sqlCmd.append("hora TEXT);");
			db.execSQL(sqlCmd.toString());
			
			//
			ContentValues ctv = new ContentValues();
			ctv.put("posto", getPosto());
			ctv.put("data",  getData());
			ctv.put("hora",  getHora());
			
			//insere os dados no banco
			db.insert("tb_dados", null, ctv);
			db.close();
			Toast.makeText(context, "DADOS SALVOS ", Toast.LENGTH_LONG).show();
			
			 }catch(Exception ex){
		    Toast.makeText(context, "FALHA AO SALVAR DADOS", Toast.LENGTH_LONG).show(); 
				
			 }
			
}
	
	public Cursor consultar(Context context){
		try{
			cursor = db.rawQuery("SELECT _id, posto, data, hora FROM tb_dados", null);
		}catch(Exception ex){
			Toast.makeText(context, "FALHA AO OBTER DADOS", Toast.LENGTH_LONG).show();
		}
		return cursor;
	}

}
