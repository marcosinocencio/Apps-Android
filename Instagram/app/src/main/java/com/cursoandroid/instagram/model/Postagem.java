package com.cursoandroid.instagram.model;

import android.provider.ContactsContract;

import com.cursoandroid.instagram.helper.ConfiguracaoFirebase;
import com.cursoandroid.instagram.helper.UsuarioFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Postagem implements Serializable {

    private String idPostagem, idUsuario, descricao, caminhoFoto;

    public Postagem() {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference postagemRef = databaseReference.child("postagens");

        String idPostagem = postagemRef.push().getKey();
        setIdPostagem(idPostagem);
    }

    public String getIdPostagem() {
        return idPostagem;
    }

    public void setIdPostagem(String idPostagem) {
        this.idPostagem = idPostagem;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public boolean salvar(DataSnapshot seguidoresSnapshot){

        Map objeto = new HashMap<>();
        Usuario usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();

        String combinacaoId = "/" + getIdUsuario() + "/" + getIdPostagem();
        objeto.put("/postagens" + combinacaoId, this );

        for(DataSnapshot seguidores : seguidoresSnapshot.getChildren()){
            String idSeguidor = seguidores.getKey();

            HashMap<String, Object> dadosSeguidor = new HashMap<>();
            dadosSeguidor.put("fotoPostagem",getCaminhoFoto());
            dadosSeguidor.put("descricao",getDescricao());
            dadosSeguidor.put("idPostagem",getIdPostagem());

            dadosSeguidor.put("nomeUsuario",usuarioLogado.getNome());
            dadosSeguidor.put("fotoUsuario",usuarioLogado.getCaminhoFoto());

            String idsAtualizacao = "/" + idSeguidor + "/" + getIdPostagem();
            objeto.put("/feed" + idsAtualizacao, dadosSeguidor );
        }

        databaseReference.updateChildren(objeto);
        return true;
    }

}
