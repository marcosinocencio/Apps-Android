package com.cursoandroid.uberclone.model;

import com.cursoandroid.uberclone.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class Requisicao {

    private String id, status;
    private Usuario passageiro, motorista;
    private Destino destino;

    public static final String STATUS_AGUARDANDO = "aguardando";
    public static final String STATUS_A_CAMINHO = "acaminho";
    public static final String STATUS_VIAGEM = "viagem";
    public static final String STATUS_FINALIZADA = "finalizada";
    public static final String STATUS_ENCERRADA = "encerrada";
    public static final String STATUS_CANCELADA = "cancelada";

    public Requisicao() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Usuario getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Usuario passageiro) {
        this.passageiro = passageiro;
    }

    public Usuario getMotorista() {
        return motorista;
    }

    public void setMotorista(Usuario motorista) {
        this.motorista = motorista;
    }

    public Destino getDestino() {
        return destino;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }

    public void salvar(){

        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        DatabaseReference requisicoes = databaseReference.child("requisicoes");

        String idRequisicao = requisicoes.push().getKey();
        setId(idRequisicao);

        requisicoes.child(getId()).setValue(this);

    }

    public void atualizar(){

        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        DatabaseReference requisicoes = databaseReference.child("requisicoes");

        DatabaseReference requisicao = requisicoes.child(getId());

        Map objeto = new HashMap();
        objeto.put("motorista", getMotorista());
        objeto.put("status", getStatus());

        requisicao.updateChildren(objeto);


    }

    public void atualizarLocalizacaoMotorista(){

        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        DatabaseReference requisicoes = databaseReference.child("requisicoes");

        DatabaseReference requisicao = requisicoes
                .child(getId())
                .child("motorista");

        Map objeto = new HashMap();
        objeto.put("latitude", getMotorista().getLatitude());
        objeto.put("longitude", getMotorista().getLongitude());

        requisicao.updateChildren(objeto);


    }

    public void atualizarStatus(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        DatabaseReference requisicoes = databaseReference.child("requisicoes");

        DatabaseReference requisicao = requisicoes.child(getId());

        Map objeto = new HashMap();
        objeto.put("status", getStatus());

        requisicao.updateChildren(objeto);
    }
}
