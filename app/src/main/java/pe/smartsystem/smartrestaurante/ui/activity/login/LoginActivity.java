package pe.smartsystem.smartrestaurante.ui.activity.login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import pe.smartsystem.smartrestaurante.ConexionSQLITEhelper;
import pe.smartsystem.smartrestaurante.IP;
import pe.smartsystem.smartrestaurante.R;
import pe.smartsystem.smartrestaurante.ServiciosWeb.SolicitudesJson;
import pe.smartsystem.smartrestaurante.TableActivity;
import pe.smartsystem.smartrestaurante.URLs.Links;
import pe.smartsystem.smartrestaurante.Utilidades.SessionManager;
import pe.smartsystem.smartrestaurante.Utilidades.Utilidades;
import pe.smartsystem.smartrestaurante.VolleyRP;
import pe.smartsystem.smartrestaurante.network.ApiClient;
import pe.smartsystem.smartrestaurante.network.ApiInterface;
import pe.smartsystem.smartrestaurante.ui.activity.setting.SettingActivity;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    ImageView lblConfiguracion;

    public static String Personal;
    public static String Nombre;
    public static String Cargo;
    //public static String idEmpleado;
    String mac;
    @BindView(R.id.btnIngresar)
    Button btnIngresar;
    @BindView(R.id.progressBar)
    ProgressBar bar;
    @BindView(R.id.txtUsuario)
    EditText txtUsuario;
    @BindView(R.id.txtClave)
    EditText txtClave;

    private VolleyRP volley;
    private RequestQueue mRequest;


    private String IPparaConexion = "";
    public static String IPgeneral;


    private SessionManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        volley = VolleyRP.getInstance(LoginActivity.this);
        mRequest = volley.getRequestQueue();

        // Session manager
        manager = new SessionManager(this);
        // Check if user is already logged in or not
        if (manager.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, TableActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        consultarListaIPs();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private void validarIMEI(String MAC) {
        bar.setVisibility(View.VISIBLE);

        if (IPparaConexion.equalsIgnoreCase("")) {
            Toasty.error(LoginActivity.this, "No se a guardado la IP  y port", Toast.LENGTH_SHORT).show();
            bar.setVisibility(View.GONE);
            btnIngresar.setEnabled(true);
            //startActivity(new Intent(this,SettingActivity.class));
            return;
        }

        final String[] resultado = {""};
        String URLVALIDARMAC = "http://"+manager.getIp()+"/WS/ValidarIMEI.php?caja=" + MAC;



        SolicitudesJson s = new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
                try {
                    String TodoslasMesas = j.getString("imei");
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        resultado[0] = js.getString("caja");
                    }

                    VerificacionLogin(txtUsuario.getText().toString(), txtClave.getText().toString());
                } catch (JSONException e) {
                    bar.setVisibility(View.GONE);
                    btnIngresar.setEnabled(true);
                    Toasty.error(LoginActivity.this, "error al descomponer JSON", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void solicitudErronea(VolleyError error) {
                bar.setVisibility(View.GONE);
                btnIngresar.setEnabled(true);
                Toasty.error(LoginActivity.this, "EQUIPO NO REGISTRADO"+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        s.solicitudJsonGET(LoginActivity.this, URLVALIDARMAC);
    }


    private void VerificacionLogin(String usu, String clave) {
        String url = Links.URL_LOGIN_GET + "?user=" + usu + "&pass=" + clave;
        SolicitudesJson s = new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
                try {
                    String TodoslasMesas = j.getString("empleado");
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        //Personal = js.optString("Nombre") + " " + js.optString("Apellidos");
                        manager.setFullName(js.optString("Nombre") + " " + js.optString("Apellidos"));
                        Cargo = "Cargo: " + js.optString("Cargo");
                        //idEmpleado = js.optString("IdEmpleado");
                        manager.setId(Integer.parseInt(js.optString("IdEmpleado")));
                        Nombre = js.optString("Nombre");
                    }

                    Toasty.success(LoginActivity.this, "Credenciales Válidas", Toast.LENGTH_SHORT).show();

                    if (isToplas()){
                        launchingTable();
                        return;
                    }
                    getToplas();

                } catch (JSONException e) {
                    bar.setVisibility(View.GONE);
                    btnIngresar.setEnabled(true);
                    Toasty.error(LoginActivity.this, "Usuario y/o contraseña incorrecta", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void solicitudErronea(VolleyError error) {
                btnIngresar.setEnabled(true);
                bar.setVisibility(View.GONE);
                Toasty.error(LoginActivity.this, "Usuario y/o contraseña incorrecta "+error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        };
        s.solicitudJsonGET(LoginActivity.this, url);
    }


    private boolean isToplas(){
        SQLiteDatabase bd = cn.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT * FROM " + "platos", null);
        while (cursor.moveToNext()) {
            return true;
        }

        return false;

    }

    private void launchingTable(){
        // mosaico successfully logged in
        // Create login session
        manager.setLogin(true);
        Intent IngresoBn = new Intent(LoginActivity.this, TableActivity.class);
        startActivity(IngresoBn);
        finish();
    }



    private void consultarListaIPs() {
        SQLiteDatabase bd = cn.getReadableDatabase();
        IP ip = null;
        Cursor cursor = bd.rawQuery("SELECT * FROM " + Utilidades.TABLA, null);
        while (cursor.moveToNext()) {
            ip = new IP();
            ip.setIp(cursor.getString(0));
            IPparaConexion = ip.getIp();
        }
        IPgeneral = IPparaConexion;
        manager.setIp(IPparaConexion);

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
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {

        }

        return "02:00:00:00:00:00";
        //return "02:15:b2:00:00:00";
    }


    public void setOnSettingListener(View view) {
        startActivity(new Intent(LoginActivity.this, SettingActivity.class));
    }


    //DESCARGAR LOS PALTOS


    private void getToplas() {
        SolicitudesJson s = new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
                try {
                    String variable = "ProductosGeneral";

                    //LIMPIAR SQLITE
                    cn.deleteUser();


                    String TodoslasMesas = j.getString(variable);
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        try {
                            RegistrarIP(js.getString("idProducto") + ""
                                    , js.getString("NombreProducto") + ""
                                    , js.getString("PrecioUnidad") + ""
                                    , js.getString("Destino") + ""
                                    , js.getString("NombreCategoria")+"");
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    Toasty.success(LoginActivity.this, "Platos guardados", Toast.LENGTH_SHORT).show();
                    guardarCategorias();
//
                } catch (JSONException e) {
                    btnIngresar.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "Error al descomponer el Json", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void solicitudErronea(VolleyError error) {
                btnIngresar.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Error al consultar Top de Productos", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
            }
        };
        s.solicitudJsonGET(this, Links.URL_TOP_GET);

    }

    ConexionSQLITEhelper cn = new ConexionSQLITEhelper(this, "bdservidor", null, 1);

    private void guardarCategorias() {
        JsonObjectRequest solicitud = new JsonObjectRequest(Links.URL_LISTA_CATEGORIAS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    //LIMPIAR SQLITE
                    cn.deleteCategorias();
                    String TodoslasMesas = datos.getString("categorias");
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        //Log.e("CATEGORIA", js.getString("IdCategoria"));
                        cn.guardarCategoria(js.getString("IdCategoria"), js.getString("NombreCategoria"));

                    }
                    guardarTodoLosPlatos();
                    Toasty.success(LoginActivity.this, "Categorias guardadas", Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "Error al descomponer el Json", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnIngresar.setEnabled(true);
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud, mRequest, LoginActivity.this, volley);


    }

    private void guardarTodoLosPlatos() {
        ApiInterface apiService = ApiClient.getCliente().create(ApiInterface.class);

        Call<List<Plato>> call = apiService.callPlatos();

        call.enqueue(new Callback<List<Plato>>() {
            @Override
            public void onResponse(Call<List<Plato>> call, retrofit2.Response<List<Plato>> response) {
                cn.deletetodolosplatos();
                List<Plato> list = response.body();

                for (Plato plato : list) {
                    cn.guardarTodoLosToplas(plato);
                }

                Toasty.success(LoginActivity.this, "OK ", Toast.LENGTH_SHORT).show();


                launchingTable();
//                manager.setLogin(true);
//                Intent IngresoBn = new Intent(LoginActivity.this, TableActivity.class);
//                startActivity(IngresoBn);
//                finish();
            }

            @Override
            public void onFailure(Call<List<Plato>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    //GURDAREN SQLIT
    private static final String idproducto = "idproducto";
    private static final String nombreProducto = "nombreProducto";
    private static final String precioUnidad = "precioUnidad";
    private static final String destino = "destino";
    private static final String nombrecategoria = "NombreCategoria";

    private void guardartodolosplatos() {
        SQLiteDatabase database = cn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("1", 1);
        Long ipRegistrado = database.insert("todolosplatos", null, values);

    }


    private void RegistrarIP(String id, String nombre, String precio, String des, String nombCatego) {

        SQLiteDatabase bd = cn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(idproducto, id);
        values.put(nombreProducto, nombre);
        values.put(precioUnidad, precio);
        values.put(destino, des);
        values.put(nombrecategoria, nombCatego);


        Long ipRegistrado = bd.insert("platos", null, values);
        //   Toast.makeText(getApplicationContext(),"FILAINESRTADA : "+ipRegistrado,Toast.LENGTH_LONG).show();
        bd.close();
        //Intent volveralLogin = new Intent(SettingActivity.this, LoginActivity.class);
        // startActivity(volveralLogin);
    }

//

    @OnClick(R.id.btnIngresar)
    public void onViewClicked() {
        btnIngresar.setEnabled(false);
        mac = getMacAddr();
        validarIMEI(mac);//mac="02:15:b2:00:00:00";
    }
}




   // private void guardarCategorias(String idCategoria, String nombreCategoria) {
//        SQLiteDatabase bd = cn.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(ConexionSQLITEhelper.idCategoria, idCategoria);
//        values.put(ConexionSQLITEhelper.NombreCategoria, nombreCategoria);
//
//        Long ipRegistrado = bd.insert(ConexionSQLITEhelper.categoria_, null, values);
//
//
//        Toast.makeText(getApplicationContext(), "CATEGORIA_PLATO : " + ipRegistrado, Toast.LENGTH_LONG).show();
//
//        bd.close();
//    }