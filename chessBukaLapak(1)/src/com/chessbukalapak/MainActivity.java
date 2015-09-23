package com.chessbukalapak;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import com.chessbukalapak.ServerThread.OnReadListener;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity {

	ServerThread st = null;
	private TextView text;
	private ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text2);
    }

    @Override
    public void onResume() {
        startMonitoring();

        super.onResume();
    }

    @Override
    public void onPause() {
        stopMonitoring();

        super.onPause();
    }

    private void startMonitoring() {
        //stopMonitoring();

        st = new ServerThread("xinuc.org", 7387);
        st.setListener(new OnReadListener() {
			
			@Override
			public void onRead(ServerThread serverThread, final String response) {
				runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // update UI or do something with response
                    	resetImage();
                    	text.setText("");
                    	String[] posisi = response.split("\\s+");
                    	for(int i=0;i<posisi.length;i++){
                    		char bidak = posisi[i].charAt(0);
                    		char horizontal = posisi[i].charAt(1);
                    		char vertikal = posisi[i].charAt(2);
                    		String cell = posisi[i].substring(1);
                    		int nilai = ((int)horizontal)-96;
                    		int keterangan;
                    		if(Character.getNumericValue(vertikal)%2==0){
                    			if(nilai%2==1)
                    				keterangan=1;
                    			else
                    				keterangan=0;
                    		}
                    		else{
                    			if(nilai%2==1)
                    				keterangan=0;
                    			else
                    				keterangan=1;
                    		}
                    		String namaFile = getNamaFile(keterangan, bidak);
                    		int resID = getResources().getIdentifier(cell, "id", "com.chessbukalapak");
                    		image = (ImageView) findViewById(resID);
                    		int imageID = getResources().getIdentifier(namaFile, "drawable",  getPackageName());
                    		image.setImageResource(imageID);
                    	}
                    }
                });
			}
		});
        st.start();
    }
    
    private void resetImage(){
    	for(int i=1;i<=8;i++){
    		for(int j=0;j<8;j++){
    			String posisi = Character.toString((char)(j+97))+String.valueOf(i);
    			if(i%2==0){
    				if(j%2==0){
    					int resID = getResources().getIdentifier(posisi, "id", "com.chessbukalapak");
                		image = (ImageView) findViewById(resID);
                		image.setImageResource(R.drawable.white_square);
    				}
    				else{
    					int resID = getResources().getIdentifier(posisi, "id", "com.chessbukalapak");
                		image = (ImageView) findViewById(resID);
                		image.setImageResource(R.drawable.black_square);
    				}
    			}
    			else{
    				if(j%2==0){
    					int resID = getResources().getIdentifier(posisi, "id", "com.chessbukalapak");
                		image = (ImageView) findViewById(resID);
                		image.setImageResource(R.drawable.black_square);
    				}
    				else{
    					int resID = getResources().getIdentifier(posisi, "id", "com.chessbukalapak");
                		image = (ImageView) findViewById(resID);
                		image.setImageResource(R.drawable.white_square);
    				}
    			}
    		}
    	}
    }
    
    private String getNamaFile(int state, char bidak){
		String hasil = null;
    	if(state==0){
			if (bidak=='K')
				hasil = "kingwhite_black";
			else if(bidak=='k')
				hasil = "kingblack_black";
			else if(bidak=='Q')
				hasil = "queenwhite_black";
			else if(bidak=='q')
				hasil = "queenblack_black";
			else if(bidak=='N')
				hasil = "knightwhite_black";
			else if(bidak=='n')
				hasil = "knightblack_black";
			else if(bidak=='B')
				hasil = "bishopwhite_black";
			else if(bidak=='b')
				hasil = "bishopblack_black";
			else if(bidak=='R')
				hasil = "rookwhite_black";
			else
				hasil = "rookblack_black";
		}
		else{
			if (bidak=='K')
				hasil = "kingwhite_white";
			else if(bidak=='k')
				hasil = "kingblack_white";
			else if(bidak=='Q')
				hasil = "queenwhite_white";
			else if(bidak=='q')
				hasil = "queenblack_white";
			else if(bidak=='N')
				hasil = "knightwhite_white";
			else if(bidak=='n')
				hasil = "knightblack_white";
			else if(bidak=='B')
				hasil = "bishopwhite_white";
			else if(bidak=='b')
				hasil = "bishopblack_white";
			else if(bidak=='R')
				hasil = "rookwhite_white";
			else
				hasil = "rookblack_white";
		}
		return hasil;
    }
    private void stopMonitoring() {
        if (st != null) {
            try {
				st.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}
