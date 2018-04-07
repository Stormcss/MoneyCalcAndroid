package ru.strcss.projects.moneycalc.moneycalcandroid.handlers;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.strcss.projects.moneycalc.moneycalcandroid.AppExecutors;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcClient;

@Singleton
public class ServerHandler {
    static final String BASE_URL = "http://192.168.1.101:8080";
    private MoneyCalcClient client;
    @Inject
    AppExecutors appExecutors;

//    @Inject
//    public ServerHandler(AppExecutors appExecutors) {
//        this.appExecutors = appExecutors;
//    }

    @Inject
    public ServerHandler() {
    }

    public MoneyCalcClient getClient() {
        if (client == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
//                    .callbackExecutor(appExecutors.networkIO())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            client = retrofit.create(MoneyCalcClient.class);
        }
        return client;
    }

}

