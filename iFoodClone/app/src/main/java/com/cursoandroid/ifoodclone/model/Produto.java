package com.cursoandroid.ifoodclone.model;

import com.cursoandroid.ifoodclone.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class Produto {

    private String idUsuario, idProduto, nome, descricao;
    private Double preco;

    public Produto() {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = databaseReference
                .child("produtos");
        setIdProduto(produtoRef.push().getKey());
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public void salvar(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = databaseReference
                .child("produtos")
                .child(getIdUsuario())
                .child(getIdProduto());

        produtoRef.setValue(this);

    }

    public void remover(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = databaseReference
                .child("produtos")
                .child(getIdUsuario())
                .child(getIdProduto());
        produtoRef.removeValue();
    }
}
