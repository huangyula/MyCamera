package com.hiscene.flytech.api;

import com.hiscene.flytech.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("data/{gank}/10/{page}")
    Observable<HttpResult<List<String>>> getGankData( @Path("gank") String gank, @Path("page") int page );
}
