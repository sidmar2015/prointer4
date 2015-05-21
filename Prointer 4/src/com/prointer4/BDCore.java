package com.prointer4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDCore extends SQLiteOpenHelper {
	
	private final static String NOME_BD = "acessos";
	private final static int VERSAO_BD  = 1;

	public BDCore(Context context) {
		super(context, NOME_BD, null, VERSAO_BD);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table dados_acesso(_id integer primary key autoincrement, local text not null, data text not null, hora text not null);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table dados_acesso");
		onCreate(db);
		
	}

}
