package com.cursoandroid.instagram.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cursoandroid.instagram.R;
import com.cursoandroid.instagram.model.Comentario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterComentario extends RecyclerView.Adapter<AdapterComentario.MyViewHolder> {

    private List<Comentario> listaComentario;
    private Context context;

    public AdapterComentario(List<Comentario> listaComentario, Context context) {
        this.listaComentario = listaComentario;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.adapter_comentario, parent,false);

        return new AdapterComentario.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Comentario comentario = listaComentario.get(position);

        holder.textNomeComentario.setText(comentario.getNomeUsuario());
        holder.textComentario.setText(comentario.getComentario());
        Glide.with(context).load(comentario.getCaminhoFoto()).into(holder.imageFotoComentario);

    }

    @Override
    public int getItemCount() {
        return listaComentario.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView imageFotoComentario;
        private TextView textNomeComentario, textComentario;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageFotoComentario = itemView.findViewById(R.id.imageFotoComentario);
            textNomeComentario = itemView.findViewById(R.id.textNomeComentario);
            textComentario = itemView.findViewById(R.id.textComentario);
        }
    }
}
