package pe.smartsystem.smartrestaurante.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://190.81.3.91/WS/";
    private static Retrofit retrofit = null;

    /**
     * Este metodo retorna la intancia
     *
     * @return Retrofit object
     */


    public static Retrofit getCliente(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();



        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                  .addConverterFactory(GsonConverterFactory.create())
                    // .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;

    }
}
