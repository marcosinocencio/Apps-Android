package com.cursoandroid.whatsapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.cursoandroid.whatsapp.R;
import com.cursoandroid.whatsapp.activity.ChatActivity;
import com.cursoandroid.whatsapp.adapter.ConversasAdapter;
import com.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import com.cursoandroid.whatsapp.helper.Base64Custom;
import com.cursoandroid.whatsapp.helper.RecyclerItemClickListener;
import com.cursoandroid.whatsapp.helper.UsuarioFirebase;
import com.cursoandroid.whatsapp.model.Conversa;
import com.cursoandroid.whatsapp.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConversasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversasFragment extends Fragment {

    private RecyclerView recyclerView;
    private ConversasAdapter conversasAdapter;
    private List<Conversa> listaConversas = new ArrayList<>();
    private DatabaseReference conversasRef;
    private ChildEventListener childEventListenerConversas;
    private FirebaseUser usuarioAtual;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConversasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConversasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConversasFragment newInstance(String param1, String param2) {
        ConversasFragment fragment = new ConversasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarConversas();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversasRef.removeEventListener(childEventListenerConversas);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        conversasAdapter = new ConversasAdapter(listaConversas, getActivity());

        recyclerView = view.findViewById(R.id.recyclerViewConversas);

        usuarioAtual = ConfiguracaoFirebase.getFirebaseAutenticacao().getCurrentUser();
        String idUsuarioAtual = UsuarioFirebase.getIdUsuario();

        conversasRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("conversas")
                .child(idUsuarioAtual);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(conversasAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getActivity(), ChatActivity.class);

                List<Conversa> listaConversaAtualiza = conversasAdapter.getConversas();
                Conversa conversaSelecionada = listaConversaAtualiza.get(position);

                if(conversaSelecionada.getIsGroup().equals("true")){
                    i.putExtra("chatGrupo", conversaSelecionada.getGrupo());
                    startActivity(i);
                }else{
                    Usuario usuarioSelecionado = conversaSelecionada.getUsuarioExibicao();
                    i.putExtra("chatContato", usuarioSelecionado);
                    startActivity(i);
                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }
        ));

        return view;
    }

    public void recuperarConversas(){

        listaConversas.clear();

        childEventListenerConversas = conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                Conversa conversa = dataSnapshot.getValue(Conversa.class);

                listaConversas.add(conversa);

                conversasAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void pesquisarConversas (String texto){
        List<Conversa> listaConversaBusca = new ArrayList<>();

        for(Conversa conversa : listaConversas){

            if(conversa.getUsuarioExibicao() != null){
                String nome = conversa.getUsuarioExibicao().getNome().toLowerCase();
                String ultimaMensagem = conversa.getUltimaMensagem().toLowerCase();

                if(nome.contains(texto) || ultimaMensagem.contains(texto)){
                    listaConversaBusca.add(conversa);
                }
            }else{
                String nome = conversa.getGrupo().getNome().toLowerCase();
                String ultimaMensagem = conversa.getUltimaMensagem().toLowerCase();

                if(nome.contains(texto) || ultimaMensagem.contains(texto)){
                    listaConversaBusca.add(conversa);
                }
            }


        }

        conversasAdapter = new ConversasAdapter(listaConversaBusca, getActivity());
        recyclerView.setAdapter(conversasAdapter);
        conversasAdapter.notifyDataSetChanged();

    }

    public void recarregarConversas(){
        conversasAdapter = new ConversasAdapter(listaConversas, getActivity());
        recyclerView.setAdapter(conversasAdapter);
        conversasAdapter.notifyDataSetChanged();
    }
}