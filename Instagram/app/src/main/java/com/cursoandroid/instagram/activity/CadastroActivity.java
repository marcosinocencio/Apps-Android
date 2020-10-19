package com.cursoandroid.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cursoandroid.instagram.R;
import com.cursoandroid.instagram.helper.ConfiguracaoFirebase;
import com.cursoandroid.instagram.helper.UsuarioFirebase;
import com.cursoandroid.instagram.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText nome, email, senha;
    private Button botaoCadastrar;
    private ProgressBar progressBar;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nome = findViewById(R.id.editCadastroNome);
        email = findViewById(R.id.editCadastroEmail);
        senha = findViewById(R.id.editCadastroSenha);
        botaoCadastrar = findViewById(R.id.buttonCadastrar);
        progressBar = findViewById(R.id.progressCadastro);
        nome.requestFocus();

        progressBar.setVisibility(View.GONE);
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validarCadastroUsuario(view);
            }
        });
    }

    public void validarCadastroUsuario(View view){

        String nomeC = nome.getText().toString();
        String emailC = email.getText().toString();
        String senhaC = senha.getText().toString();

        if(!nomeC.isEmpty()){
            if(!emailC.isEmpty()){
                if(!senhaC.isEmpty()){
                    Usuario usuario = new Usuario();
                    usuario.setNome(nomeC);
                    usuario.setEmail(emailC);
                    usuario.setSenha(senhaC);
                    cadastrarUsuario(usuario);
                }else{
                    Toast.makeText(CadastroActivity.this,
                            "Preencha a senha!",
                            Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(CadastroActivity.this,
                        "Preencha o email!",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(CadastroActivity.this,
                    "Preencha o nome!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void cadastrarUsuario(final Usuario usuario){
        progressBar.setVisibility(View.VISIBLE);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this
                        , new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    try{
                                        progressBar.setVisibility(View.GONE);

                                        String idUsuario = task.getResult().getUser().getUid();
                                        usuario.setId(idUsuario);
                                        usuario.salvar();

                                        UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());

                                        Toast.makeText(CadastroActivity.this,
                                                "Sucesso ao cadastrar usuário",
                                                Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    String excecao = "";

                                    try{
                                        throw task.getException();
                                    }catch (FirebaseAuthWeakPasswordException e){
                                        excecao = "Digite uma senha mais forte";
                                    }catch (FirebaseAuthInvalidCredentialsException e){
                                        excecao = "Digite um email válido";
                                    }catch (FirebaseAuthUserCollisionException e){
                                        excecao = "Essa conta já foi cadastrada";
                                    }catch (Exception e){
                                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(CadastroActivity.this,
                                            excecao,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

    }
}