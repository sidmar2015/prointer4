package com.prointer4;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AcessoAdapter extends BaseAdapter {
	
	private Context context;
	private List<Acesso> list;
	
	public AcessoAdapter(Context context, List<Acesso> list){
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return Long.getLong(list.get(position).getId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
						
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.model_consultar, null);
						
		TextView txvId = (TextView) layout.findViewById(R.id.txvId);
		txvId.setText(list.get(position).getId());
		
		TextView txvPosto = (TextView) layout.findViewById(R.id.txvPosto);
		txvPosto.setText(list.get(position).getLocal());
		
		TextView txvData = (TextView) layout.findViewById(R.id.txvData);
		txvData.setText(list.get(position).getData());
		
		TextView txvHora = (TextView) layout.findViewById(R.id.txvHora);
		txvHora.setText(list.get(position).getHora());
		
		return layout;
	}

}
