package ru.strcss.projects.moneycalc.moneycalcandroid.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.strcss.projects.moneycalc.dto.AjaxRs;
import ru.strcss.projects.moneycalc.dto.Credentials;
import ru.strcss.projects.moneycalc.enitities.Access;
import ru.strcss.projects.moneycalc.enitities.Person;

public interface MoneyCalcClient {
    @POST("/api/registration/register")
    Call<AjaxRs<Person>> registerPerson(@Body Credentials credentials);

    @POST("/login")
    Call<Void> login(@Body Access access);
}
