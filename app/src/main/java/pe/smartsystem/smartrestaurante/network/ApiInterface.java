package pe.smartsystem.smartrestaurante.network;

import java.util.List;


import pe.smartsystem.smartrestaurante.ui.activity.login.Plato;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("todolosplatos.php")
    Call<List<Plato>> callPlatos();



//    @GET("TopProductos.php")
//    Call
//
//
//
//
//    @GET("login.php")
//    Call<LoginResponse> signInWithEmailAndPassword(@Query("email") String user, @Query("password") String password);
//
//    @GET("getMedicalAppointments.php")
//    Call<List<DoctorList>> getMedicalAppointments(@Query("idEmpleado") int idEmpleado);
//
//    //metodo get
//    @GET("guardarObservacion.php")
//    Call<LoginResponse> saveObservation(@Query("observacion") String observation, @Query("idCitasMedicas") int id);
//


   // @GET("index.php")
   // Call<List<LocationVago>> getLocation();

//    @POST("registerwithFacebook.php")
//    Call<MyResponse> setData(@Query("email") String email,
//                             @Query("first_name") String first_name,
//                             @Query("last_name") String last_name);
//
//
//    @GET("storeAtention.php")
//    Call<MyResponse> storeAtention(@Query("svcliente_id") int svcliente_id,
//                                   @Query("svcliente_id_local") int svcliente_id_local,
//                                   @Query("svatencion_mesa") String svatencion_mesa,
//                                   @Query("svatencion_moso") int svatencion_moso,
//                                   @Query("svatencion_tll_ta") String svatencion_tll_ta,
//                                   @Query("svatencion_numerocomprobante") String svatencion_numerocomprobante);
//
//
//
//    @GET("getWaiter.php")
//    Call<List<Waiter>> getWaiter();
//
//    @GET("signInWithEmailAndPassword.php")
//    Call<MyResponse> signInWithEmailAndPassword(@Query("email") String user, @Query("password") String password);
}
