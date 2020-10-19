package com.cursoandroid.frasesdodiaprojeto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gerarNovaFrase(View view){
        String[] frases = {
                "Tudo o que um sonho precisa para ser realizado é alguém que acredite que ele possa ser realizado.",
                "Imagine uma nova história para sua vida e acredite nela.",
                "A amizade desenvolve a felicidade e reduz o sofrimento, duplicando a nossa alegria e dividindo a nossa dor.",
                "Ser feliz sem motivo é a mais autêntica forma de felicidade.",
                "Não existe um caminho para a felicidade. A felicidade é o caminho.",
                "Nunca deixe ninguém te dizer que não pode fazer alguma coisa. Se você tem um sonho tem que correr atrás dele. As pessoas não conseguem vencer e dizem que você também não vai vencer. Se você quer uma coisa corre atrás.",
                "Não espere por uma crise para descobrir o que é importante em sua vida.",
                "Acredite em si próprio e chegará um dia em que os outros não terão outra escolha senão acreditar com você.",
                "Saber encontrar a alegria na alegria dos outros, é o segredo da felicidade."
        };

        TextView frase = findViewById(R.id.textView);
        frase.setText(frases[new Random().nextInt(9)]);
    }
}