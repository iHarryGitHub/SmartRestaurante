package pe.smartsystem.smartrestaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;
import pe.smartsystem.smartrestaurante.ServiciosWeb.SolicitudesJson;
import pe.smartsystem.smartrestaurante.URLs.Links;
import pe.smartsystem.smartrestaurante.Utilidades.Utilidades;
import pe.smartsystem.smartrestaurante.setting.SettingActivity;
import pe.smartsystem.smartrestaurante.splash.SplashScreenActivity;
import pe.smartsystem.smartrestaurante.ui.activity.MessageActivity;
import pe.smartsystem.smartrestaurante.ui.activity.mesas.TableActivity;
import pe.smartsystem.smartrestaurante.ui.fragment.category.CategoriaPlato;

public class LoginActivity extends AppCompatActivity {

    EditText txtUsuario;
    EditText txtClave;
    ImageView lblConfiguracion;

    public static String Personal;
    public static String Nombre;
    public static String Cargo;
    public static String idEmpleado;
    String mac;

    private VolleyRP volley;
    private RequestQueue mRequest;

    ConexionSQLITEhelper cn = new ConexionSQLITEhelper(this, "bdservidor", null, 1);
    private String IPparaConexion = "";
    public static String IPgeneral;


    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        volley = VolleyRP.getInstance(LoginActivity.this);
        mRequest = volley.getRequestQueue();

        bar=findViewById(R.id.progressBar);
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtClave = (EditText) findViewById(R.id.txtClave);
        //lblConfiguracion = (ImageView) findViewById(R.id.lblConfiguracion);

        consultarListaIPs();
      //  attemptLogin


        /*lblConfiguracion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent configuracionIP = new Intent(LoginActivity.this,SettingActivity.class);
                startActivity(configuracionIP);
            }
        });*/

        //getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
    }

    public void attemptLogin(View view) {
        mac = getMacAddr();
        validarIMEI(mac);
    }

    private void validarIMEI(String MAC){
        bar.setVisibility(View.VISIBLE);

        if(IPparaConexion.equalsIgnoreCase("")){
            Toasty.error(LoginActivity.this, "No se a guardado la IP  y port", Toast.LENGTH_SHORT).show();
            bar.setVisibility(View.GONE);
            return;
        }

        final String[] resultado = {""};
        String URLVALIDARMAC = Links.URL_VALIDAR_MAC+MAC;

        SolicitudesJson s=new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
                try {
                    String TodoslasMesas= j.getString("imei");
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);
                        resultado[0] = js.getString("caja");
                    }

                    VerificacionLogin(txtUsuario.getText().toString(),txtClave.getText().toString());
                }catch (JSONException e){
                    bar.setVisibility(View.GONE);
                    Toasty.error(LoginActivity.this, "error al descomponer JSON", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void solicitudErronea() {
                Toasty.error(LoginActivity.this,"Este equipo no esta registrado",Toast.LENGTH_SHORT).show();
            }
        };
        s.solicitudJsonGET(LoginActivity.this,URLVALIDARMAC);
    }


    private void VerificacionLogin(String usu, String clave){
        String url = Links.URL_LOGIN_GET+"?user="+usu+"&pass="+clave;
        SolicitudesJson s=new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
                try {
                    String TodoslasMesas= j.getString("empleado");
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);
                        Personal=js.optString("Nombre") + " "+js.optString("Apellidos");
                        Cargo = "Cargo: " + js.optString("Cargo");
                        idEmpleado =  js.optString("IdEmpleado");
                        Nombre =js.optString("Nombre");
                    }

                    Toasty.success(LoginActivity.this, "Credenciales Válidas.", Toast.LENGTH_SHORT).show();
                    getToplas();

                    //agregar al final de las categorias
//
                }catch (JSONException e){
                    Toasty.error(LoginActivity.this, "Usuario y/o contraseña incorrecta", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void solicitudErronea() {
                bar.setVisibility(View.GONE);
                Toasty.error(LoginActivity.this,"Usuario y/o contraseña incorrecta",Toast.LENGTH_SHORT,true).show();
            }
        };
        s.solicitudJsonGET(LoginActivity.this,url);
    }


    private void consultarListaIPs(){
        SQLiteDatabase bd=cn.getReadableDatabase();
        IP ip=null;
        Cursor cursor=bd.rawQuery("SELECT * FROM "+ Utilidades.TABLA,null);
        while (cursor.moveToNext()){
            ip =new IP();
            ip.setIp(cursor.getString(0));
            IPparaConexion=ip.getIp();
        }
        IPgeneral=IPparaConexion;

    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }








    public void setOnSettingListener(View view) {
        startActivity(new Intent(LoginActivity.this,SettingActivity.class));
    }








    //DESCARGAR LOS PALTOS


    private void getToplas(){
        SolicitudesJson s=new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
                try {
                    String variable = "ProductosGeneral";

                    //LIMPIAR SQLITE
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
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    Toasty.success(LoginActivity.this, "PLATOS GUARDADOS", Toast.LENGTH_SHORT).show();
                    guardarCategorias();
//
                }catch (JSONException e){
                    Toast.makeText(LoginActivity.this,"Error al descomponer el Json",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                }
            }
            @Override
            public void solicitudErronea() {
                Toast.makeText(LoginActivity.this,"Error al consultar Top de Productos",Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
            }
        };



        s.solicitudJsonGET(this,Links.URL_TOP_GET);

    }
    ConexionSQLITEhelper sqlitEhelper = new ConexionSQLITEhelper(LoginActivity.this,"bdservidor",null,1);
    private void guardarCategorias() {
        JsonObjectRequest solicitud = new JsonObjectRequest(Links.URL_LISTA_CATEGORIAS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    //LIMPIAR SQLITE
                    cn.deleteCategorias();
                    String TodoslasMesas= datos.getString("categorias");
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);

                        //Toast.makeText(LoginActivity.this, js.getString("NombreCategoria"), Toast.LENGTH_LONG).show();
                        //AgregarCategorias(js.getString("NombreCategoria"),js.getString("IdCategoria"));
                        //CategoriaPlato b =new CategoriaPlato(js.getString("NombreCategoria"),js.getInt("IdCategoria"));

                        //guardarCategorias(js.getString("IdCategoria"),js.getString("NombreCategoria"));


                        sqlitEhelper.guardarCategoria(js.getString("IdCategoria"),js.getString("NombreCategoria"));

                    }
                    Toasty.success(LoginActivity.this, "Categorias Guardadas", Toast.LENGTH_SHORT).show();

                    Intent IngresoBn = new Intent(LoginActivity.this,TableActivity.class);
                   startActivity(IngresoBn);
                    finish();
                }catch (JSONException e){
                    Toast.makeText(LoginActivity.this,"Error al descomponer el Json",Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"Error al consultar Categorias",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,LoginActivity.this,volley);



    }







    //GURDAREN SQLIT

    private static final String idproducto="idproducto";
    private static final String nombreProducto="nombreProducto";
    private static final String precioUnidad="precioUnidad";
    private static final String destino="destino";
    private static final String nombrecategoria="NombreCategoria";


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

    private void guardarCategorias(String idCategoria,String nombreCategoria){
        SQLiteDatabase bd=cn.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ConexionSQLITEhelper.idCategoria, idCategoria);
        values.put(ConexionSQLITEhelper.NombreCategoria, nombreCategoria);

        Long ipRegistrado= bd.insert(ConexionSQLITEhelper.categoria_, null, values);


        Toast.makeText(getApplicationContext(),"CATEGORIA_PLATO : "+ipRegistrado,Toast.LENGTH_LONG).show();

        bd.close();
    }

}
