package com.cursoandroid.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.cursoandroid.whatsapp.adapter.MensagensAdapter;
import com.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import com.cursoandroid.whatsapp.helper.Base64Custom;
import com.cursoandroid.whatsapp.helper.UsuarioFirebase;
import com.cursoandroid.whatsapp.model.Conversa;
import com.cursoandroid.whatsapp.model.Grupo;
import com.cursoandroid.whatsapp.model.Mensagem;
import com.cursoandroid.whatsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.whatsapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private CircleImageView fotoContato;
    private RecyclerView recyclerViewMensagens;
    private TextView nomeContato;
    private EditText editMensagem;
    private String idUsuarioRemetente, idUsuarioDestinatario;
    private List<Mensagem> mensagens = new ArrayList<>();
    private DatabaseReference databaseReference;
    private DatabaseReference mensagensRef;
    private StorageReference storageReference;
    private ChildEventListener childEventListenerMensagens;
    private MensagensAdapter adapter;
    private ImageView imageCamera;
    private static final int SELECAO_CAMERA = 100;
    private Usuario usuarioDestinatario, usuarioRemetente;
    private Grupo grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fotoContato = findViewById(R.id.circleImageViewFotoContato);
        nomeContato = findViewById(R.id.nomeContato);
        editMensagem = findViewById(R.id.contatoMensagem);
        idUsuarioRemetente = UsuarioFirebase.getIdUsuario();
        recyclerViewMensagens = findViewById(R.id.recyclerViewMensagens);
        imageCamera = findViewById(R.id.imageCamera);

        usuarioRemetente = UsuarioFirebase.getDadosUsuarioLogado();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {

            if(bundle.containsKey("chatGrupo")){
                grupo = (Grupo) bundle.getSerializable("chatGrupo");
                idUsuarioDestinatario = grupo.getId();

                if (grupo.getFoto() != null) {
                    Uri uri = Uri.parse(grupo.getFoto());
                    Glide.with(this).load(uri).into(fotoContato);
                } else {
                    fotoContato.setImageResource(R.drawable.padrao);
                }

                nomeContato.setText(grupo.getNome());

            }else {
                usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");

                if (usuarioDestinatario.getFoto() != null) {
                    Uri uri = Uri.parse(usuarioDestinatario.getFoto());
                    Glide.with(this).load(uri).into(fotoContato);
                } else {
                    fotoContato.setImageResource(R.drawable.padrao);
                }

                nomeContato.setText(usuarioDestinatario.getNome());

                idUsuarioDestinatario = Base64Custom.codificarBase64(usuarioDestinatario.getEmail());
            }
        }

        adapter = new MensagensAdapter(mensagens, getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewMensagens.setLayoutManager(layoutManager);
        recyclerViewMensagens.setHasFixedSize(true);
        recyclerViewMensagens.setAdapter(adapter);

        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        mensagensRef = databaseReference.child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_CAMERA );
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener(childEventListenerMensagens);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap bitmap = null;

            try{
                switch (requestCode){
                    case SELECAO_CAMERA:
                        bitmap = (Bitmap) data.getExtras().get("data");
                        break;
                }

                if (bitmap != null) {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70 ,baos);
                    byte[] dadosImagem = baos.toByteArray();

                    String nomeImagem = UUID.randomUUID().toString();

                    final StorageReference imagemRef = storageReference.child("imagens")
                            .child("fotos")
                            .child(idUsuarioRemetente)
                            .child(nomeImagem);

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivity.this,
                                    "Erro ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ChatActivity.this,
                                    "Sucesso ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();

                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String url = task.getResult().toString();

                                    //Quando não é conversa em grupo
                                    if(usuarioDestinatario != null){
                                        Mensagem mensagem = new Mensagem();
                                        mensagem.setIdUsuario(idUsuarioRemetente);
                                        mensagem.setMensagem("imagem.jpeg");
                                        mensagem.setFoto(url);

                                        salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

                                        salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                                        salvarConversa(idUsuarioRemetente, idUsuarioDestinatario, usuarioDestinatario, mensagem, false);

                                        salvarConversa(idUsuarioDestinatario, idUsuarioRemetente, usuarioRemetente, mensagem, false);

                                    }else{//conversa em grupo
                                        for(Usuario membro : grupo.getMembros()){

                                            String idRemetenteGrupo = Base64Custom.codificarBase64(membro.getEmail());
                                            String idUsuarioLogadoGrupo = UsuarioFirebase.getIdUsuario();

                                            Mensagem mensagem = new Mensagem();
                                            mensagem.setIdUsuario(idUsuarioLogadoGrupo);
                                            mensagem.setMensagem("imagem.jpeg");
                                            mensagem.setNome(usuarioRemetente.getNome());
                                            mensagem.setFoto(url);

                                            salvarMensagem(idRemetenteGrupo, idUsuarioDestinatario, mensagem);

                                            salvarConversa(idRemetenteGrupo, idUsuarioDestinatario, usuarioDestinatario, mensagem, true);
                                        }
                                    }

                                    Toast.makeText(ChatActivity.this,
                                            "Sucesso ao enviar imagem",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void enviarMensagem(View view){
        String textoMensagem = editMensagem.getText().toString();

        if(!textoMensagem.isEmpty()){

            if( usuarioDestinatario != null){
                Mensagem mensagem = new Mensagem();
                mensagem.setIdUsuario(idUsuarioRemetente);
                mensagem.setMensagem(textoMensagem);

                //Salvar mensagem para o remetente
                salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

                //Salvar mensagem para o destinatário
                salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                //Salvar conversa para o remetente
                salvarConversa(idUsuarioRemetente, idUsuarioDestinatario, usuarioDestinatario, mensagem, false);

                //Salvar conversa para o destinatario
                salvarConversa(idUsuarioDestinatario, idUsuarioRemetente, usuarioRemetente, mensagem, false);

            }else{
                for(Usuario membro : grupo.getMembros()){

                    String idRemetenteGrupo = Base64Custom.codificarBase64(membro.getEmail());
                    String idUsuarioLogadoGrupo = UsuarioFirebase.getIdUsuario();

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioLogadoGrupo);
                    mensagem.setMensagem(textoMensagem);
                    mensagem.setNome(usuarioRemetente.getNome());

                    salvarMensagem(idRemetenteGrupo, idUsuarioDestinatario, mensagem);

                    salvarConversa(idRemetenteGrupo, idUsuarioDestinatario, usuarioDestinatario, mensagem, true);
                }
            }
        }else{
            Toast.makeText(this,
                    "Digite uma mensagem para enviar",
                    Toast.LENGTH_SHORT);
        }

    }

    private void salvarMensagem (String remetente, String destinatario, Mensagem msg){

        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        mensagensRef = databaseReference.child("mensagens");

        mensagensRef.child(remetente)
                .child(destinatario)
                .push()
                .setValue(msg);


        editMensagem.setText("");
    }

    private void recuperarMensagens(){

       mensagens.clear();

       childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);
               mensagens.add(mensagem);
               adapter.notifyDataSetChanged();
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

    private void salvarConversa(String remetente, String destinatario, Usuario usuarioExibicao, Mensagem msg, boolean isGroup){

        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente(remetente);
        conversaRemetente.setIdDestinatario(destinatario);
        conversaRemetente.setUltimaMensagem(msg.getMensagem());

        if(isGroup){
            conversaRemetente.setGrupo(grupo);
            conversaRemetente.setIsGroup("true");
        }else{
            conversaRemetente.setUsuarioExibicao(usuarioExibicao);
            conversaRemetente.setIsGroup("false");
        }
        conversaRemetente.salvar();

    }
}