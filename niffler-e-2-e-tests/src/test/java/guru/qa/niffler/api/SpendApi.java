package guru.qa.niffler.api;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpendApi {

    @POST("internal/spends/add")
    Call<SpendJson> createSpend(@Body SpendJson spending);

    @PATCH("internal/spends/edit")
    Call<SpendJson> editSpend(@Body SpendJson spending);

    @GET("internal/spends/{id}")
    Call<SpendJson> getSpend(@Path("id") String id);

    @GET("internal/spends/all")
    Call<List<SpendJson>> getAllSpends(
            @Query("filterCurrency") CurrencyValues filterCurrency);

    @DELETE("internal/spends/remove")
    Call<SpendJson> removeSpend(@Query("ids") List<String> ids);
}
