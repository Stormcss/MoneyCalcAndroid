package ru.strcss.projects.moneycalc.moneycalcandroid.api;

import javax.inject.Singleton;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.strcss.projects.moneycalc.dto.AjaxRs;
import ru.strcss.projects.moneycalc.dto.Credentials;
import ru.strcss.projects.moneycalc.enitities.Access;
import ru.strcss.projects.moneycalc.enitities.Settings;
import rx.Observable;

@Singleton
public class MoneyCalcServerDAO implements MoneyCalcClient {
    static final String BASE_URL = "http://192.168.1.102:8080";

    private String token;
    private MoneyCalcClient client;

    public MoneyCalcServerDAO() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        client = retrofit.create(MoneyCalcClient.class);
    }

    @Override
    public Observable<AjaxRs<Void>> registerPerson(Credentials credentials) {
        return client.registerPerson(credentials);
    }

    @Override
    public Observable<Response<Void>> login(Access access) {
        return client.login(access);
    }

    @Override
    public Observable<AjaxRs<Settings>> getSettings(String token) {
        return client.getSettings(token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
