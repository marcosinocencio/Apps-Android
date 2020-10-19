package com.cursoandroid.cardview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.cardview.R;
import com.cursoandroid.cardview.model.Postagem;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Postagem> postagens;

    public Adapter(List<Postagem> p) {
        this.postagens = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postagem = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_postagem,
                parent,
                false
        );
        return new MyViewHolder(postagem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Postagem p = postagens.get(position);

        holder.nomeUsuario.setText(p.getNome());
        holder.imagem.setImageResource(p.getImagem());
        holder.postagem.setText(p.getPostagem());
    }

    @Override
    public int getItemCount() {
        return postagens.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nomeUsuario;
        private TextView postagem;
        private ImageView imagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeUsuario = itemView.findViewById(R.id.textViewUsuario);
            imagem = itemView.findViewById(R.id.imagem);
            postagem = itemView.findViewById(R.id.textViewPostagem);
        }
    }
}
