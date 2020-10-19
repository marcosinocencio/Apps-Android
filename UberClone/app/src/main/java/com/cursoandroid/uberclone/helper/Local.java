package com.cursoandroid.uberclone.helper;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

public class Local {

    public static float calcularDistancia(LatLng latLngInicial, LatLng latLngFinal){

        Location localInicial = new Location("Local inicial");
        localInicial.setLatitude(latLngInicial.latitude);
        localInicial.setLongitude(latLngInicial.longitude);

        Location localFinal = new Location("Local final");
        localFinal.setLatitude(latLngFinal.latitude);
        localFinal.setLongitude(latLngFinal.longitude);


        //Resultado em metros divido por 1000 para converter para Km
        float distancia = localInicial.distanceTo(localFinal) / 1000;

        return distancia;
    }

    public static String formatarDistancia(float distancia){
        String distanciaFormatada;
        if(distancia < 1){
            distancia = distancia * 1000; // em metros
            distanciaFormatada = Math.round(distancia) + " m ";
        }else {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            distanciaFormatada = decimalFormat.format(distancia) + " Km ";
        }
        return distanciaFormatada;
    }
}
