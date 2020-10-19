package com.cursoandroid.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    //private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    //private FirebaseAuth usuario = FirebaseAuth.getInstance();

    private ImageView foto;
    private Button buttonUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foto = findViewById(R.id.imageFoto);
        buttonUpload = findViewById(R.id.buttonUpload);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Configura para a imagem ser salva em memória
                foto.setDrawingCacheEnabled(true);
                foto.buildDrawingCache();

                //Recupera bitmap da imagem a ser carregada
                Bitmap bitmap = foto.getDrawingCache();

                //Comprime bitmap para um formato png/jpeg
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);

                //Converte o baos para pixels brutos em uma matriz de bytes (dados da imagem)
                byte[] dadosImagem = baos.toByteArray();

                //Define nós para o Storage
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference imagens = storageReference.child("imagens");
                StorageReference imageRef = imagens.child("punisher.jpeg");

                Glide.with(MainActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(imageRef)
                        .into(foto);

                /*imageRef.delete().addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,
                                "Erro ao deletar" + e.getMessage().toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,
                                "Sucesso ao deletar",
                                Toast.LENGTH_LONG).show();
                    }
                });*/

               /* //Retorna objeto que irá controlar o upload
                UploadTask uploadTask = imageRef.putBytes(dadosImagem);

                uploadTask.addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,
                                "Upload da imagem falhou" + e.getMessage().toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri url = taskSnapshot.getDownloadUrl();
                        Toast.makeText(MainActivity.this,
                                "Sucesso ao fazer upload" + url.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });*/



            }
        });

        /*//Cadastrar usuário
        usuario.createUserWithEmailAndPassword("teste2@gmail.com","123456")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.i("CreateUser","Sucesso ao cadastrar usuário!");
                        }else{
                            Log.i("CreateUser","Erro ao cadastrar usuário!");
                        }
                    }
                });*/
        //Deslogar usuário
       // usuario.signOut();

       /* //logar usuário
        usuario.signInWithEmailAndPassword("teste@gmail.com","123456")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.i("SignIn","Sucesso ao logar usuário!");
                        }else{
                            Log.i("SignIn","Erro ao logar usuário!");
                        }
                    }
                });*/

    /*  //Verifica usuário logado
        if (usuario.getCurrentUser() != null){
            Log.i("CreateUser","Usuário logado!");
        }else{
            Log.i("CreateUser","Usuário não logado!");
        }*/



      /*  Usuario usuario = new Usuario();
        usuario.setNome("Marcos Vinicius");
        usuario.setSobrenome("Inocencio");
        usuario.setIdade(32);


        reference.child("usuarios").child("001").setValue(usuario);
       DatabaseReference usuarios = reference.child("usuarios");

       usuarios.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               Log.i("FIREBASE", dataSnapshot.getValue().toString());
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });*/

      //DatabaseReference usuarios = reference.child("usuarios");

       /* Usuario usuario = new Usuario();
        usuario.setNome("Ana");
        usuario.setSobrenome("Ribeiro");
        usuario.setIdade(22);
        //gerar identifador único
        usuarios.push().setValue(usuario);*/

      // DatabaseReference usuarioPesquisa = usuarios.child("-MDV9qHdCZc2RWAz1Y09");
      // Query usuarioPesquisa = usuarios.orderByChild("nome").equalTo("Rodrigo");
      // Query usuarioPesquisa = usuarios.orderByKey().limitToFirst(2);
     /*   Query usuarioPesquisa = usuarios.orderByChild("idade").startAt(22).endAt(40);
       usuarioPesquisa.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               //Usuario dadosUsuario = dataSnapshot.getValue(Usuario.class);
               //Log.i("DadosUsuario", "nome: " + dadosUsuario.getNome());
               Log.i("DadosUsuario", dataSnapshot.getValue().toString());
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });*/


    }
}