package com.prointer4;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
  
@SuppressLint("NewApi")
public class MainActivity extends Activity {
     
  Handler bluetoothIn;

  final int handlerState = 0;        				 //usado para identificar a handler message
  private BluetoothAdapter  btAdapter = null;
  private BluetoothSocket   btSocket = null;
  private StringBuilder 	recDataString = new StringBuilder();
  private ImageButton 		btnAcessar;
  private EditText 			edtSenha; 
  private String 			senha;
  private String 			imei;
  private DAO bd;
  private Acesso acessos = new Acesso();
  private Date data = new Date();
       
  private ConnectedThread mConnectedThread;
    
  // SPP UUID service - this should work for most devices
  private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
  
  // String for MAC address
  private static String address;

@Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
    setContentView(R.layout.activity_main);
    
    bd = new DAO(this);
    btnAcessar   = (ImageButton)findViewById(R.id.btnAcessar);
    edtSenha     = (EditText)findViewById(R.id.edtSenha);
             
    //acessar
    btnAcessar.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			ObterIMEI oImei = new ObterIMEI();
			imei = oImei.getIMEI(getApplicationContext());
			senha = edtSenha.getText().toString();
			
			//obtem os dados de acesso para salvar no banco
			//acessos.setLocal(btAdapter.getName());
			acessos.setData(data.getDate()+"/"+(data.getMonth()+1)+"/"+(data.getYear()+1900));
			acessos.setHora(data.getHours()+":"+data.getMinutes()+":"+data.getSeconds());
			
			//se o tamanho da senha estiver incorreto exibe uma mensagem
			if(senha.length() != 4){
				Toast.makeText(getApplicationContext(), "O TAMANHO DA SENHA ESTÁ INCORRETO", Toast.LENGTH_LONG).show();
			//senão envia para o hardware
			}else{
				mConnectedThread.write(imei+senha);
				Toast.makeText(getApplicationContext(), "SOLICITAÇÃO ENVIADA", Toast.LENGTH_LONG).show();
				bd.inserir(acessos);
				Toast.makeText(getApplicationContext(), "DADOS DE ACESSO SALVOS", Toast.LENGTH_LONG).show();
			}
		}
	});
    
  
    bluetoothIn = new Handler() {
    	//obtem a mensagem recebida pelo sistema
        public void handleMessage(android.os.Message msg) {
            if (msg.what == handlerState) {										//se a mensagem for o que procuramos
            	String readMessage = (String) msg.obj;                          // msg.arg1 = bytes da connect thread
                recDataString.append(readMessage);      							//keep appending to string until ~
                int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                if (endOfLineIndex > 0) {                                           // make sure there data before ~
                    String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                              		
                    int dataLength = dataInPrint.length();							//get length of data received
                    
                    
                    if (recDataString.charAt(0) == '#')								//if it starts with # we know it is what we are looking for
                    {
                    	String mensagem = recDataString.toString();
                    	//string recebida
                    	if(mensagem.contains("autorizado")){
                    		
                    		Toast.makeText(getApplicationContext(), "ACESSO LIBERADO !!!", Toast.LENGTH_LONG).show();
                    	}
                    	if(mensagem.contains("negado")){
                    		Toast.makeText(getApplicationContext(), "ACESSO NEGADO !!!", Toast.LENGTH_LONG).show();
                    	}
                    }
                    recDataString.delete(0, recDataString.length()); 					//limpa todas as strings do buffer 
                   
                    dataInPrint = " ";
                }            
            }
        }
    };
      
    btAdapter = BluetoothAdapter.getDefaultAdapter();       // Obtem Bluetooth adapter
    checkBTState();	
    
  }

   
  private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
	  //cria uma conexão de saída segura com BT usando UUID
      return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
      
  }
  
    
  @Override
  public void onResume() {
    super.onResume();
    
    
    //Obtem o MAC address da DeviceListActivity via intent
    Intent intent = getIntent();
    
    //Obtem o MAC address da DeviceListActivty via EXTRA
    address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

    //cria o dispositivo e seta o MAC address
    BluetoothDevice device = btAdapter.getRemoteDevice(address);
    //obtem o nome do dispositivo para salvar no banco
    //deviceName = device.getName();
    acessos.setLocal(device.getName());
     
    try {
        btSocket = createBluetoothSocket(device);
    } catch (IOException e) {
    	Toast.makeText(getBaseContext(), "FALHA AO CRIAR SOCKET", Toast.LENGTH_LONG).show();
    }  
    // Estabelece a conexão com o socket Bluetooth 
    try 
    {
      btSocket.connect();
    } catch (IOException e) {
      try 
      {
        btSocket.close();
      } catch (IOException e2) 
      {
    	//insert code to deal with this 
      }
    } 
    //inicia a thread
    mConnectedThread = new ConnectedThread(btSocket);
    mConnectedThread.start();
    
      }
  
  @Override
  public void onPause() 
  {
    super.onPause();
    try
    {
    //Não mantém o socket do Bluetooth aberto quando saímos da Activity
      btSocket.close();
    } catch (IOException e2) {
    	
    }
  }

 //Checa se o Bluetooth do dispositivo está ligado, senão solicita que seja ligado
  private void checkBTState() {
 
    if(btAdapter == null) { 
    	Toast.makeText(getBaseContext(), "Esse diaspositivo não suporta Bluetooth", Toast.LENGTH_LONG).show();
    } else {
      if (btAdapter.isEnabled()) {
      } else {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, 1);
      }
    }
  }
  
  //cria uma nova classe thread
  public class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
                
        //Cria a thread de conexão
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
            	//Cria I/O streams para conexão
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
      
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        
      
        public void run() {
            byte[] buffer = new byte[256];  
            int bytes; 
 
            // Continua escutando as mensagens recebidas
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);        	//lê os bytes do input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Envia os bytes obtidos para a UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget(); 
                } catch (IOException e) {
                    break;
                }
            }
        }
        //método de escrita
        public void write(String input) {
            byte[] msgBuffer = input.getBytes(); //converte a String em bytes
            try {
                mmOutStream.write(msgBuffer); //escreve os bytes na conexão BT via outstream
            } catch (IOException e) {  
            	//se não escrever, fecha a aplicação
            	Toast.makeText(getBaseContext(), "Falha na conexão", Toast.LENGTH_LONG).show();
            	finish();
            	
              }
        	}
        }
  
  		
}
    
