package com.cursoandroid.prefernciasdousurio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private Button salvar;
    private TextInputEditText nome;
    private TextView resultado;
    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        salvar = findViewById(R.id.buttonSalvar);
        nome = findViewById(R.id.editNome);
        resultado = findViewById(R.id.textView2);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
                SharedPreferences.Editor editor = preferences.edit();

                if(nome.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Preencha o nome", Toast.LENGTH_LONG).show();
                }else{
                    String nomeResultado = nome.getText().toString();
                    editor.putString("nome", nomeResultado);
                    editor.commit();
                    resultado.setText("Olá, "+ nomeResultado);
                }
            }
        });

        //Recuperar os dados
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        if(preferences.contains("nome")){
            String nomeResultado = preferences.getString("nome", "usuário não definido");
            resultado.setText("Olá, "+ nomeResultado);

        }else{
            resultado.setText("Olá, usuário não definido");
        }
    }
}