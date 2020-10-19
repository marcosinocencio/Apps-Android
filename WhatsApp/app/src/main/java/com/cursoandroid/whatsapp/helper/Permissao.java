package com.cursoandroid.whatsapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validarPermissoes(String[] permissoes, Activity activity, int requestCode){

        if(Build.VERSION.SDK_INT >= 23){
            List<String> listaPermissoes = new ArrayList<>();

            /*Percorre as permissões passadas verificando uma a uma
            *se já tem permissão liberada*/
            for(String permissao : permissoes){
                Boolean temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!temPermissao){
                   listaPermissoes.add(permissao);
                }
            }
            /*Se a lista está vazia, não é necessário solicitaar permissão*/
            if (listaPermissoes.isEmpty())
                return true;

            String[] novasPermissões = new String [listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissões);

            //Solicitar permissão
            ActivityCompat.requestPermissions(activity, novasPermissões, requestCode);


        }
        return true;
    }
}
