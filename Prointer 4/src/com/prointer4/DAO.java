package com.prointer4;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


public class DAO {
	
	private SQLiteDatabase db;
	StringBuilder sqlCmd;
	
		public DAO(Context context){
			BDCore auxBd = new BDCore(context);
			db = auxBd.getWritableDatabase();
		}
		
		
		public void inserir(Acesso acesso){
			ContentValues ctv = new ContentValues();
			ctv.put("local", acesso.getLocal());
			ctv.put("data",  acesso.getData());
			ctv.put("hora",  acesso.getHora());
			
			db.insert("dados_acesso", null, ctv);
			
		}
		
		public void atualizar(Acesso acesso){
			ContentValues ctv = new ContentValues();
			ctv.put("local", acesso.getLocal());
			ctv.put("data",  acesso.getData());
			ctv.put("hora",  acesso.getHora());
			
			db.insert("dados_acesso", null, ctv);
		}
		
		public void deletar(Acesso acesso){
			
		}
		
		public List<Acesso> consultar(){
			List<Acesso> list = new ArrayList<Acesso>();
			String[] colunas  = new String[]{"_id","local","data","hora"};
			
			Cursor cursor = db.query("dados_acesso", colunas, null, null, null, null, "_id");
			
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				
				do{
					
					Acesso a = new Acesso();
					a.setId(cursor.getString(0));
					a.setLocal(cursor.getString(1));
					a.setData(cursor.getString(2));
					a.setHora(cursor.getString(3));
					list.add(a);
					
				}while(cursor.moveToNext());
			}
			return(list);
		}
		
			

}

