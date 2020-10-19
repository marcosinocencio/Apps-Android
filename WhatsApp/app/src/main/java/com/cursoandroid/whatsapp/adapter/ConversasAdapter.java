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
import com.cursoandroid.whatsapp.model.Conversa;
import com.cursoandroid.whatsapp.model.Grupo;
import com.cursoandroid.whatsapp.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder> {

    private List<Conversa> conversas;
    private Context context;

    public ConversasAdapter(List<Conversa> conversas, Context context) {
        this.conversas = conversas;
        this.context = context;
    }

    public List<Conversa> getConversas(){
        return this.conversas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_conversas, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Conversa conversaContato = conversas.get(position);
        holder.conversa.setText(conversaContato.getUltimaMensagem());

        if(conversaContato.getIsGroup().equals("true")){
            Grupo grupo = conversaContato.getGrupo();
            holder.nomeConversa.setText(grupo.getNome());

            if(grupo.getFoto() != null){
                Uri uri = Uri.parse(conversaContato.getGrupo().getFoto());
                Glide.with(context).load(uri).into(holder.fotoConversa);
            }else{
                holder.fotoConversa.setImageResource(R.drawable.padrao);
            }

        }else{
            holder.nomeConversa.setText(conversaContato.getUsuarioExibicao().getNome());


            if(conversaContato.getUsuarioExibicao().getFoto() != null){
                Uri uri = Uri.parse(conversaContato.getUsuarioExibicao().getFoto());
                Glide.with(context).load(uri).into(holder.fotoConversa);
            }else{
                holder.fotoConversa.setImageResource(R.drawable.padrao);
            }
        }


    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView fotoConversa;
        private TextView nomeConversa, conversa;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fotoConversa = itemView.findViewById(R.id.circleImageViewConversa);
            nomeConversa = itemView.findViewById(R.id.textNomeConversa);
            conversa = itemView.findViewById(R.id.textConversa);

        }
    }
}


