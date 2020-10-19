package com.cursoandroid.instagram.model;

import com.cursoandroid.instagram.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class PostagemCurtida {

    private int qtdCurtidas = 0;
    private Feed feed;
    private Usuario usuario;

    public PostagemCurtida() {
    }

    public int getQtdCurtidas() {
        return qtdCurtidas;
    }

    public void setQtdCurtidas(int qtdCurtidas) {
        this.qtdCurtidas = qtdCurtidas;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void salvar(){
        HashMap<String, Object> dadosUsuario = new HashMap<>();
        dadosUsuario.put("nomeUsuario", usuario.getNome());
        dadosUsuario.put("caminhoFoto", usuario.getCaminhoFoto());

        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pCurtidasRef = databaseReference
                .child("postagens-curtidas")
                .child(feed.getIdPostagem())
                .child(usuario.getId());

        pCurtidasRef.setValue(dadosUsuario);
        atualizarQtd(1);
    }

    public void atualizarQtd(int valor){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pCurtidasRef = databaseReference
                .child("postagens-curtidas")
                .child(feed.getIdPostagem())
                .child("qtdCurtidas");

        setQtdCurtidas(getQtdCurtidas() + valor);

        pCurtidasRef.setValue(getQtdCurtidas());

    }

    public void remover(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pCurtidasRef = databaseReference
                .child("postagens-curtidas")
                .child(feed.getIdPostagem())
                .child(usuario.getId());

        pCurtidasRef.removeValue();
        atualizarQtd(-1);
    }

}
