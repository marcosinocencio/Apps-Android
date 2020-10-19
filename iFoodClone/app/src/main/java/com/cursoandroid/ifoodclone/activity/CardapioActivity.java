package com.cursoandroid.ifoodclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cursoandroid.ifoodclone.R;
import com.cursoandroid.ifoodclone.adapter.AdapterProduto;
import com.cursoandroid.ifoodclone.helper.ConfiguracaoFirebase;
import com.cursoandroid.ifoodclone.helper.UsuarioFirebase;
import com.cursoandroid.ifoodclone.listener.RecyclerItemClickListener;
import com.cursoandroid.ifoodclone.model.Empresa;
import com.cursoandroid.ifoodclone.model.ItemPedido;
import com.cursoandroid.ifoodclone.model.Pedido;
import com.cursoandroid.ifoodclone.model.Produto;
import com.cursoandroid.ifoodclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class CardapioActivity extends AppCompatActivity {

    private RecyclerView recyclerProdutoCardapio;
    private ImageView imageEmpresaCardapio;
    private TextView textNomeEmpresaCardapio;
    private Empresa empresaSelecionada;
    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private List<ItemPedido> itensCarrinho = new ArrayList<>();
    private DatabaseReference databaseReference;
    private String idEmpresa;
    private AlertDialog dialog;
    private String idUsuarioLogado;
    private Usuario usuario;
    private Pedido pedidoRecuperado;
    private int qtdItensCarrinho;
    private Double totalCarrinho;
    private TextView textCarrinhoQtd, textCarrinhoTotal;
    private int metodoDePagamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        inicializarComponentes();

        databaseReference = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){

            empresaSelecionada = (Empresa) bundle.getSerializable("empresa");

            textNomeEmpresaCardapio.setText(empresaSelecionada.getNome());

            idEmpresa = empresaSelecionada.getIdUsuario();

            String url = empresaSelecionada.getUrlImagem();
            Picasso.get().load(url).into(imageEmpresaCardapio);

        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cardápio");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerProdutoCardapio.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutoCardapio.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos, this);
        recyclerProdutoCardapio.setAdapter(adapterProduto);

        recyclerProdutoCardapio.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerProdutoCardapio,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        confirmarQuantidade(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));

        recuperarProdutos();
        recuperarDadosUsuario();
    }

    private void confirmarQuantidade(final int position) {

        final EditText editQtd = new EditText(this);
        editQtd.setText("1");

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Quantidade")
              .setMessage("Digite a quantidade")
              .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                        String quantidade = editQtd.getText().toString();
                        Produto produtoSelecionado = produtos.get(position);

                        ItemPedido itemPedido = new ItemPedido();
                        itemPedido.setIdProduto(produtoSelecionado.getIdProduto());
                        itemPedido.setNomeProduto(produtoSelecionado.getNome());
                        itemPedido.setPreco(produtoSelecionado.getPreco());
                        itemPedido.setQuantidade(Integer.parseInt(quantidade));

                        itensCarrinho.add(itemPedido);

                        if(pedidoRecuperado == null){
                            pedidoRecuperado = new Pedido(idUsuarioLogado, idEmpresa);
                        }
                        pedidoRecuperado.setNome(usuario.getNome());
                        pedidoRecuperado.setEndereco(usuario.getEndereco());
                        pedidoRecuperado.setItens(itensCarrinho);
                        pedidoRecuperado.salvar();
                  }
              })
              .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {

                  }
              })
              .setView(editQtd)
              .create().show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cardapio, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuPedido:
                confirmarPedido();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmarPedido() {

        CharSequence[] tipoPagamento = {"Dinheiro", "Máquina cartão"};

        final EditText observacao = new EditText(this);
        observacao.setHint("Digite uma observação");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione um método de pagamento")
               .setSingleChoiceItems(tipoPagamento, 0, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                        metodoDePagamento = i;
                   }
               })
               .setView(observacao)
               .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                        String o = observacao.getText().toString();
                        pedidoRecuperado.setMetodoPagamento(metodoDePagamento);
                        pedidoRecuperado.setObservacao(o);
                        pedidoRecuperado.setStatus("confirmado");
                        pedidoRecuperado.confirmar();
                        pedidoRecuperado.remover();
                        pedidoRecuperado = null;
                   }
               })
               .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                   }
               })
               .create()
               .show();


    }

    private void recuperarDadosUsuario(){

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando dados")
                .setCancelable(false)
                .build();

        dialog.show();

        DatabaseReference usuariosRef = databaseReference
                .child("usuarios")
                .child(idUsuarioLogado);

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    usuario = dataSnapshot.getValue(Usuario.class);
                }
                recuperarPedido();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void recuperarPedido() {

        DatabaseReference pedidoRef = databaseReference
                .child("pedidos_usuario")
                .child(idEmpresa)
                .child(idUsuarioLogado);

        pedidoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                qtdItensCarrinho = 0;
                totalCarrinho = 0.0;
                itensCarrinho = new ArrayList<>();

                if(dataSnapshot.getValue() != null){
                    pedidoRecuperado = dataSnapshot.getValue(Pedido.class);
                    itensCarrinho = pedidoRecuperado.getItens();

                    for(ItemPedido itemPedido : itensCarrinho){

                        int qtd = itemPedido.getQuantidade();
                        Double preco = itemPedido.getPreco();

                        totalCarrinho += qtd * preco;
                        qtdItensCarrinho += qtd;

                    }
                }
                DecimalFormat df = new DecimalFormat("0.00");

                textCarrinhoQtd.setText("Qtd: " + String.valueOf(qtdItensCarrinho));
                textCarrinhoTotal.setText("R$ : " + df.format(totalCarrinho));

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void inicializarComponentes(){
        recyclerProdutoCardapio = findViewById(R.id.recyclerProdutoCardapio);
        imageEmpresaCardapio = findViewById(R.id.imageEmpresaCardapio);
        textNomeEmpresaCardapio = findViewById(R.id.textNomeEmpresaCardapio);
        textCarrinhoQtd = findViewById(R.id.textCarrinhoQtd);
        textCarrinhoTotal = findViewById(R.id.textCarrinhoTotal);
    }

    private void recuperarProdutos(){

        DatabaseReference produtosRef = databaseReference
                .child("produtos")
                .child(idEmpresa);

        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                produtos.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    produtos.add(ds.getValue(Produto.class));
                }
                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}