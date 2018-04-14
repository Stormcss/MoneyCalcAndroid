package ru.strcss.projects.moneycalc.moneycalcandroid.api;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.strcss.projects.moneycalc.dto.AjaxRs;
import ru.strcss.projects.moneycalc.dto.Credentials;
import ru.strcss.projects.moneycalc.enitities.Access;
import ru.strcss.projects.moneycalc.enitities.Settings;
import rx.Observable;

public interface MoneyCalcClient {
    @POST("/api/registration/register")
    Observable<AjaxRs<Void>> registerPerson(@Body Credentials credentials);

    @POST("/login")
    Observable<Response<Void>> login(@Body Access access);

    @GET("/api/settings/getSettings")
    Observable<AjaxRs<Settings>> getSettings(@Header("Authorization") String token);
}
