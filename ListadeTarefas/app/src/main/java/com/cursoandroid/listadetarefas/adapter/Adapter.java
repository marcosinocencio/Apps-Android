package com.cursoandroid.listadetarefas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.listadetarefas.R;
import com.cursoandroid.listadetarefas.model.Tarefa;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Tarefa> listaTarefas;

    public Adapter(List<Tarefa> listaTarefas) {
        this.listaTarefas = listaTarefas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemTarefa = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_tarefa, parent,false);
        return new MyViewHolder(itemTarefa);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tarefa.setText(listaTarefas.get(position).getNomeTarefa());
    }

    @Override
    public int getItemCount() {
        return listaTarefas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tarefa;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tarefa = itemView.findViewById(R.id.textTarefa);
        }
    }
}
