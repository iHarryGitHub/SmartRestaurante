package pe.smartsystem.smartrestaurante.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pe.smartsystem.smartrestaurante.TableActivity;
import pe.smartsystem.smartrestaurante.URLs.Links;
import pe.smartsystem.smartrestaurante.Utilidades.SessionManager;
import pe.smartsystem.smartrestaurante.ui.activity.login.LoginActivity;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //private static final String BASE_URL = "http://190.81.3.91/WS/";
    private SessionManager manager;
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
                    .baseUrl("http://"+ TableActivity.IP_GENERAL_GET_SHARED +"/WS/")
                  .addConverterFactory(GsonConverterFactory.create())
                    // .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;

    }
}
