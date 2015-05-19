package com.prointer4;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import android.widget.TextView;
import android.widget.Toast;
  
@SuppressLint("NewApi")
public class MainActivity extends Activity {
    
  
  
  Handler bluetoothIn;

  final int handlerState = 0;        				 //usado para identificar a handler message
  private BluetoothAdapter btAdapter = null;
  private BluetoothSocket btSocket = null;
  private StringBuilder recDataString = new StringBuilder();
  private Button btnConsultar;
  private Button btnConectar;
  private Button btnAcessar;
  private EditText edtSenha; 
  private boolean acesso = false;
     
  private ConnectedThread mConnectedThread;
    
  // SPP UUID service - this should work for most devices
  private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
  
  // String for MAC address
  private static String address;

@Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
    setContentView(R.layout.activity_main);
    
    btnAcessar   = (Button)findViewById(R.id.btnAcessar);
    btnConsultar = (Button)findViewById(R.id.btnConsultar);
    btnConectar  = (Button)findViewById(R.id.btnConectar);
    edtSenha     = (EditText)findViewById(R.id.edtSenha);
    
    btnAcessar.setActivated(false);//desativa o click do botão de acesso
    btnAcessar.setBackgroundResource(R.drawable.cadeado_fechado);
    
    //acessar
    btnAcessar.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			
		}
	});
    
    //acessar a lista de dispositivos
    btnConectar.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		
			
		}
	});
    
    //consultar os acessos
    btnConsultar.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent Consulta = new Intent();
			Consulta.setAction(".ConsultarActivity");
			startActivity(Consulta);
		}
	});

    bluetoothIn = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == handlerState) {										//if message is what we want
            	String readMessage = (String) msg.obj;                          // msg.arg1 = bytes from connect thread
                recDataString.append(readMessage);      							//keep appending to string until ~
                int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                if (endOfLineIndex > 0) {                                           // make sure there data before ~
                    String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                    //txtString.setText("Data Received = " + dataInPrint);           		
                    int dataLength = dataInPrint.length();							//get length of data received
                    //txtStringLength.setText("String Length = " + String.valueOf(dataLength));
                    
                    if (recDataString.charAt(0) == '#')								//if it starts with # we know it is what we are looking for
                    {
                    	//string recebida
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
      
      return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
      //cria uma conexão de sída segura com BT usando UUID
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
     
    try {
        btSocket = createBluetoothSocket(device);
    } catch (IOException e) {
    	Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
    }  
    // Estabelece a conezão com o socket Bluetooth 
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
        private String senha;
        private String IMEI;
        
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
    
