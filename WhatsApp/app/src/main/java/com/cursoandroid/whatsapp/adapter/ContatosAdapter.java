package com.cursoandroid.whatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cursoandroid.whatsapp.R;
import com.cursoandroid.whatsapp.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContatosAdapter extends RecyclerView.Adapter <ContatosAdapter.MyViewHolder> {

    private List<Usuario> contatos;
    private Context context;

    public ContatosAdapter(List<Usuario> listaContatos, Context c) {
        this.contatos = listaContatos;
        this.context = c;
    }

    public List<Usuario> getContatos(){
        return this.contatos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_contatos, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Usuario contato = contatos.get(position);
        boolean cabecalho = contato.getEmail().isEmpty();

        holder.nomeContato.setText(contato.getNome());
        holder.emailContato.setText(contato.getEmail());

        if(contato.getFoto() != null){
            Uri uri = Uri.parse(contato.getFoto());
            Glide.with(context).load(uri).into(holder.fotoContato);
        }else{
            if(cabecalho) {
                holder.fotoContato.setImageResource(R.drawable.icone_grupo);
                holder.emailContato.setVisibility(View.GONE);
            }else
                holder.fotoContato.setImageResource(R.drawable.padrao);
        }

    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView fotoContato;
        private TextView nomeContato, emailContato;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fotoContato = itemView.findViewById(R.id.circleImageViewContato);
            nomeContato = itemView.findViewById(R.id.textNomeContato);
            emailContato = itemView.findViewById(R.id.textEmailContato);

        }
    }

}
