package pe.smartsystem.smartrestaurante.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import pe.smartsystem.smartrestaurante.ConexionSQLITEhelper;
import pe.smartsystem.smartrestaurante.IP;
import pe.smartsystem.smartrestaurante.LoginActivity;
import pe.smartsystem.smartrestaurante.R;
import pe.smartsystem.smartrestaurante.ServiciosWeb.SolicitudesJson;
import pe.smartsystem.smartrestaurante.URLs.Links;
import pe.smartsystem.smartrestaurante.Utilidades.Utilidades;
import pe.smartsystem.smartrestaurante.VolleyRP;
import pe.smartsystem.smartrestaurante.setting.Setting;
import pe.smartsystem.smartrestaurante.setting.SettingActivity;
import pe.smartsystem.smartrestaurante.sqlite.SQLiteHandler;
import pe.smartsystem.smartrestaurante.ui.fragment.category.CategoriaPlato;
import pe.smartsystem.smartrestaurante.ui.fragment.top.data.DataModel;

public class SplashScreenActivity extends AppCompatActivity {


    String viene="";
    private ArrayList<DataModel> data;
    String nomCatego="";

    private VolleyRP volley;
    private RequestQueue mRequest;

   // private static final String URL_GET_TOP = Links.URL_TOP_GET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();


        cn=new ConexionSQLITEhelper(this,"bdservidor",null,1);

        verificarIP();



     /*   List<DataModel> allTags = cn.getAllTags();
        for (DataModel tag : allTags) {
            Toast.makeText(this, tag.getNombreCategoria(), Toast.LENGTH_LONG).show();
        }
*/



    }



    private void getToplas(){
        SolicitudesJson s=new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
                try {
                    String variable = "ProductosGeneral";
                    cn.deleteUser();
                    String TodoslasMesas= j.getString(variable);
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);
                        try{
                            RegistrarIP(js.getString("idProducto")+""
                                    , js.getString("NombreProducto")+""
                                    , js.getString("PrecioUnidad")+""
                                    , js.getString("Destino")+""
                                    , js.getString("NombreCategoria"));
                        }catch (Exception e){
                        }
                    }

                    Toast.makeText(SplashScreenActivity.this, "PLATOS GUARDADOS", Toast.LENGTH_SHORT).show();
                    guardarCategorias();
//                    startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
//                    finish();
                }catch (JSONException e){
                    Toast.makeText(SplashScreenActivity.this,"Error al descomponer el Json",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                }
            }
            @Override
            public void solicitudErronea() {
                Toast.makeText(SplashScreenActivity.this,"Error al consultar Top de Productos",Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            }
        };


     /*   if(viene.equalsIgnoreCase("categoria")){
            urlViene= Links.URL_LISTA_PRODUCTOS+nomCatego;
        }else {
            urlViene = URL_GET_TOP;
        }
*/

        //s.solicitudJsonGET(this,URL_TOP_GET);
        s.solicitudJsonGET(this,"http://190.81.3.91:82/WS/TopProductos.php");
    }

    private void guardarCategorias() {



        JsonObjectRequest solicitud = new JsonObjectRequest("http://190.81.3.91:82/WS/TopProductos.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String TodoslasMesas= datos.getString("categorias");
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);
                        //AgregarCategorias(js.getString("NombreCategoria"),js.getString("IdCategoria"));
                        CategoriaPlato b =new CategoriaPlato(js.getString("NombreCategoria"),js.getInt("IdCategoria"));




                    }
                }catch (JSONException e){
                    Toast.makeText(SplashScreenActivity.this,"Error al descomponer el Json",Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SplashScreenActivity.this,"Error al consultar Categorias",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,SplashScreenActivity.this,volley);



    }




    String ipcapturada="";
    private void consultarListaIPs(){
        SQLiteDatabase bd=cn.getReadableDatabase();

        Cursor cursor=bd.rawQuery("SELECT * FROM "+SQLiteHandler.TABLE_TOPLAS ,null);

        while (cursor.moveToNext()){
            ipcapturada=cursor.getString(0);
            Toast.makeText(this, ipcapturada+"", Toast.LENGTH_SHORT).show();
        }

        if(!ipcapturada.equalsIgnoreCase("")){
            Setting ipSetting = new Setting(ipcapturada);
            //arrayList.add(ipSetting);
            //adapter.notifyDataSetChanged();
        }

    }


    private static final String idproducto="idproducto";
    private static final String nombreProducto="nombreProducto";
    private static final String precioUnidad="precioUnidad";
    private static final String destino="destino";
    private static final String nombrecategoria="NombreCategoria";

    ConexionSQLITEhelper cn;
    private void RegistrarIP(String id,String nombre,String precio,String des,String nombCatego ){

        SQLiteDatabase bd=cn.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(idproducto, id);
        values.put(nombreProducto, nombre);
        values.put(precioUnidad, precio);
        values.put(destino, des);
        values.put(nombrecategoria, nombCatego);


        Long ipRegistrado=bd.insert("platos",null,values);
     //   Toast.makeText(getApplicationContext(),"FILAINESRTADA : "+ipRegistrado,Toast.LENGTH_LONG).show();
        bd.close();
        //Intent volveralLogin = new Intent(SettingActivity.this, LoginActivity.class);
       // startActivity(volveralLogin);
    }



    String URL_TOP_GET;
    private String IPparaConexion = "";
    public static String IPgeneral="";
    private int i = 0;
    private void verificarIP(){
        SQLiteDatabase bd=cn.getReadableDatabase();

        Cursor cursor=bd.rawQuery("SELECT * FROM "+ Utilidades.TABLA,null);
        while (cursor.moveToNext()){
            i++;
        }
        if (i>0){
             URL_TOP_GET="http://"+ipcapturada+"/WS/TopProductos.php";
            getToplas();
        }else {
            startActivity(new Intent(this,SettingActivity.class));
            Toasty.warning(this, "Configurar la Direccion IP", Toasty.LENGTH_LONG,true).show();
        }
        //IPgeneral=IPparaConexion;
        IPgeneral="190.81.3.91:82";

    }



}
