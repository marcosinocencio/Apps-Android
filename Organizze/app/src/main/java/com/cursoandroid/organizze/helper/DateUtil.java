package com.cursoandroid.organizze.helper;

import java.text.SimpleDateFormat;

public class DateUtil {

    public static String dataAtual(){
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return new String( simpleDateFormat.format(date));
    }

    public static String mesAnoDataEscolhida(String data){
        String retornoData[] = data.split("/");
        String mes = retornoData[1];
        String ano = retornoData[2];

        return new String (mes+ano);
    }
}
