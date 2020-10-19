package marcosinocencio.cursoandroidyoutubeapp.youtube.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import marcosinocencio.cursoandroidyoutubeapp.youtube.R;
import marcosinocencio.cursoandroidyoutubeapp.youtube.adapter.AdapterVideo;
import marcosinocencio.cursoandroidyoutubeapp.youtube.api.YoutubeService;
import marcosinocencio.cursoandroidyoutubeapp.youtube.helper.RetrofitConfig;
import marcosinocencio.cursoandroidyoutubeapp.youtube.helper.YoutubeConfig;
import marcosinocencio.cursoandroidyoutubeapp.youtube.listener.RecyclerItemClickListener;
import marcosinocencio.cursoandroidyoutubeapp.youtube.model.Item;
import marcosinocencio.cursoandroidyoutubeapp.youtube.model.Resultado;
import marcosinocencio.cursoandroidyoutubeapp.youtube.model.Video;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerVideos;
    private List<Item> videos = new ArrayList<>();
    private Resultado resultado;
    private AdapterVideo adapterVideo;
    private MaterialSearchView searchView;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("YouTube");
        setSupportActionBar(toolbar);

        recyclerVideos = findViewById(R.id.recylerVideos);

        searchView = findViewById(R.id.searchView);
        retrofit = RetrofitConfig.getRetrofit();

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recuperarVideos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                recuperarVideos("");
            }
        });

        recuperarVideos("");

    }

    private void recuperarVideos(String pesquisa){

        String q = pesquisa.replaceAll(" ","+");

        YoutubeService youtubeService = retrofit.create(YoutubeService.class);

        youtubeService.recuperarVideos(
                "snippet",
                "date",
                "20",
                YoutubeConfig.CHAVE_YOUTUBE_API,
                YoutubeConfig.CANAL_ID,
                q
        ).enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {

                if(response.isSuccessful()){
                    resultado = response.body();
                    videos = resultado.items;
                    configurarRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {

            }
        });

    }

    public void configurarRecyclerView(){
        adapterVideo = new AdapterVideo(videos, this);
        recyclerVideos.setHasFixedSize(true);
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        recyclerVideos.setAdapter(adapterVideo);
        recyclerVideos.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerVideos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Item video = videos.get(position);
                        String idVideo = video.id.videoId;

                        Intent i = new Intent(MainActivity.this, PlayerActivity.class);
                        i.putExtra("idVideo",idVideo);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        searchView.setMenuItem(item);

        return true;
    }
}