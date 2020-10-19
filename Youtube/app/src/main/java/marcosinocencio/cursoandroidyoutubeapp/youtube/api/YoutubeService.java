package marcosinocencio.cursoandroidyoutubeapp.youtube.api;

import marcosinocencio.cursoandroidyoutubeapp.youtube.model.Resultado;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeService {
    /*
    https://www.googleapis.com/youtube/v3/
    search
    ?part=snippet
    &order=date
    &maxResults=20
    &key=AIzaSyDqsKJdo9MCQVuVzvR4bO_I3x1x_yff4z4
    &channelId=UC8pYaQzbBBXg9GIOHRvTmDQ
    &q=muscle+car

    */

    @GET("search")
    Call<Resultado> recuperarVideos(
            @Query("part") String part,
            @Query("order") String order,
            @Query("maxResults") String maxResults,
            @Query("key") String key,
            @Query("channelId") String channelId,
            @Query("q") String q
    );

}
