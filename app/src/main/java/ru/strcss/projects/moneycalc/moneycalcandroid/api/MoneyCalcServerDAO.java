package ru.strcss.projects.moneycalc.moneycalcandroid.api;

import javax.inject.Singleton;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.strcss.projects.moneycalc.dto.AjaxRs;
import ru.strcss.projects.moneycalc.dto.Credentials;
import ru.strcss.projects.moneycalc.enitities.Access;
import ru.strcss.projects.moneycalc.enitities.Person;
import rx.Observable;

@Singleton
public class MoneyCalcServerDAO implements MoneyCalcClient {
    static final String BASE_URL = "http://192.168.1.100:8080";

    MoneyCalcClient client;

    public MoneyCalcServerDAO() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        client = retrofit.create(MoneyCalcClient.class);
    }

    @Override
    public Observable<AjaxRs<Person>> registerPerson(Credentials credentials) {
        return null;
    }

    @Override
    public Observable<String> login(Access access) {
        System.out.println("in MoneyCalcServerDAO: access = " + access);
        return client.login(access);
    }
}
