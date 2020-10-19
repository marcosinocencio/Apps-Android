package com.cursoandroid.threads;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private Button buttonIniciar;
    private int numero;
    private Handler handler = new Handler();
    private boolean pararExecucao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonIniciar = findViewById(R.id.buttonIniciar);
    }

    public void inicarThread(View view){
        /*MyThread myThread = new MyThread();
        myThread.start();*/
        pararExecucao = false;
        MyRunnable myRunnable = new MyRunnable();
        new Thread(myRunnable).start();
    }

    public void pararThread (View view){
        pararExecucao = true;
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();

            for(int i = 0; i <= 15; i++){
                Log.d("Thread", "contador: " + i);
                try {
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    class MyRunnable implements Runnable{

        @Override
        public void run() {

            for(int i = 0; i <= 15; i++){

                if(pararExecucao)
                    return;

                Log.d("Thread", "contador: " + i);
                numero = i;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonIniciar.setText("contador: " + numero);
                    }
                });

               /* handler.post(new Runnable() {
                    @Override
                    public void run() {
                        buttonIniciar.setText("contador: " + numero);
                    }
                });*/

                try {
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

}