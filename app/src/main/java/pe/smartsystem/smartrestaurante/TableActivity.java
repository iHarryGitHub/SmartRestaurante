package pe.smartsystem.smartrestaurante;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import pe.smartsystem.smartrestaurante.ServiciosWeb.SolicitudesJson;
import pe.smartsystem.smartrestaurante.URLs.Links;
import pe.smartsystem.smartrestaurante.Utilidades.SessionManager;
import pe.smartsystem.smartrestaurante.network.ApiClient;
import pe.smartsystem.smartrestaurante.network.ApiInterface;
import pe.smartsystem.smartrestaurante.ui.activity.MesaPojo;
import pe.smartsystem.smartrestaurante.ui.activity.login.LoginActivity;
import pe.smartsystem.smartrestaurante.ui.activity.login.Plato;
import pe.smartsystem.smartrestaurante.ui.activity.main.MainActivity;
import pe.smartsystem.smartrestaurante.ui.activity.mesas.adapter.AdapterListaMesas;
import retrofit2.Call;
import retrofit2.Callback;

public class TableActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static String IP_GENERAL_GET_SHARED;

    @BindView(R.id.progressBarTable)
    ProgressBar progressBarTable;
    @BindView(R.id.RefreshLayout)
    SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private AdapterListaMesas adapterCotizacion;
    private ArrayList<MesaPojo> mesaPojos;
    private static final String URL_GET_MESAS = Links.URL_MESAS_GET;
    private VolleyRP volley;
    private RequestQueue mRequest;

    private SessionManager manager;

    ProgressDialog progressDialog;
    private void update() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Actualizando Platos");
        progressDialog.setMessage("Descargando...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //progressBarTable.setVisibility(View.VISIBLE);
        getToplas();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        // getSupportActionBar().hide();
        ButterKnife.bind(this);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#014B46"));
        }

        // Session manager
        manager = new SessionManager(getApplicationContext());
        IP_GENERAL_GET_SHARED=manager.getIp();
        refreshLayout.setOnRefreshListener(this);

        volley = VolleyRP.getInstance(TableActivity.this);
        mRequest = volley.getRequestQueue();

//        setTitle(LoginActivity.Personal);
        setTitle(manager.getFullName());

        mesaPojos = new ArrayList<>();
        recyclerView = findViewById(R.id.label_profile_lista_mesas_rv);
        adapterCotizacion = new AdapterListaMesas(mesaPojos);

        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 150);
        recyclerView.setLayoutManager(layoutManager);

        adapterCotizacion.setOnClicListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TableActivity.this, MainActivity.class);
                MesaPojo mesaPojo = mesaPojos.get(recyclerView.getChildAdapterPosition(v));
                intent.putExtra("extra", (Serializable) mesaPojo);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapterCotizacion);

        showTables();


    }

    private void showTables() {

        SolicitudesJson s = new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
                MesaPojo consultita = null;
                try {
                    String TodoslasMesas = j.getString("mesas");
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        if (js.getString("Estado").equalsIgnoreCase("Ocupada")) {
                            consultita = new MesaPojo(js.getInt("NMesa"), "ocupada");
                            mesaPojos.add(consultita);
                            adapterCotizacion.notifyDataSetChanged();
                        } else if (js.getString("Estado").equalsIgnoreCase("Libre")) {
                            consultita = new MesaPojo(js.getInt("NMesa"), "libre");
                            mesaPojos.add(consultita);
                            adapterCotizacion.notifyDataSetChanged();
                        } else if (js.getString("Estado").equalsIgnoreCase("En Caja")) {
                            consultita = new MesaPojo(js.getInt("NMesa"), "espera");
                            mesaPojos.add(consultita);
                            adapterCotizacion.notifyDataSetChanged();
                        }

                    }
                    progressBarTable.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);

                } catch (JSONException e) {
                    refreshLayout.setRefreshing(false);
                    progressBarTable.setVisibility(View.GONE);
                    Toasty.info(TableActivity.this, "error al descomponer JSON", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void solicitudErronea(VolleyError error) {
                refreshLayout.setRefreshing(false);
                progressBarTable.setVisibility(View.GONE);
                Toasty.info(TableActivity.this, error.getMessage() + "", Toast.LENGTH_SHORT, true).show();
            }
        };
        //s.solicitudJsonGET(TableActivity.this, URL_GET_MESAS);
        s.solicitudJsonGET(TableActivity.this,  "http://"+manager.getIp()+"/WS/consultarMesa.php");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update_toplas) {
            update();
            return true;
        }

        if (id==R.id.action_exit){
            manager.setLogin(false);
            finish();
            startActivity(new Intent(this,LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //[SWIPE REFRESH
    @Override
    public void onRefresh() {
        mesaPojos.removeAll(mesaPojos);
        adapterCotizacion.notifyDataSetChanged();
        showTables();
    }
    //[SWIPE REFRESH

    ConexionSQLITEhelper cn = new ConexionSQLITEhelper(this, "bdservidor", null, 1);

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
                            saveTopToplas(js.getString("idProducto") + ""
                                    , js.getString("NombreProducto") + ""
                                    , js.getString("PrecioUnidad") + ""
                                    , js.getString("Destino") + ""
                                    , js.getString("NombreCategoria"));
                        } catch (Exception e) {
                            Toast.makeText(TableActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    Toasty.success(TableActivity.this, "Platos guardados", Toast.LENGTH_SHORT).show();
                    guardarCategorias();
//
                } catch (JSONException e) {

                    Toast.makeText(TableActivity.this, "Error al descomponer el Json", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(TableActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void solicitudErronea(VolleyError error) {

                Toast.makeText(TableActivity.this, "Error al consultar Top de Productos", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(TableActivity.this, LoginActivity.class));
            }
        };
        s.solicitudJsonGET(this, "http://"+manager.getIp()+"/WS/TopProductos.php");

    }




    private void guardarCategorias() {
        JsonObjectRequest solicitud = new JsonObjectRequest("http://"+manager.getIp()+"/WS/mostrarCategorias.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    //LIMPIAR SQLITE
                    cn.deleteCategorias();

                    String TodoslasMesas = datos.getString("categorias");
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        cn.guardarCategoria(js.getString("IdCategoria"), js.getString("NombreCategoria"));

                    }
                    guardarTodoLosPlatos();
                    Toasty.success(TableActivity.this, "Categorias guardadas", Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    Toast.makeText(TableActivity.this, "Error al descomponer el Json", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(TableActivity.this, "Categorias "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        VolleyRP.addToQueue(solicitud, mRequest, TableActivity.this, volley);


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

                Toasty.success(TableActivity.this, "OK ", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();

                //launchingTable();
//                manager.setLogin(true);
//                Intent IngresoBn = new Intent(LoginActivity.this, TableActivity.class);
//                startActivity(IngresoBn);
//                finish();
            }

            @Override
            public void onFailure(Call<List<Plato>> call, Throwable t) {
                progressDialog.cancel();
                Toast.makeText(TableActivity.this, "guardarTodoLosPlatos "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }







    //GURDAREN SQLIT
    private static final String idproducto = "idproducto";
    private static final String nombreProducto = "nombreProducto";
    private static final String precioUnidad = "precioUnidad";
    private static final String destino = "destino";
    private static final String nombrecategoria = "NombreCategoria";

    private void saveTopToplas(String id, String nombre, String precio, String des, String nombCatego) {

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
    }
}
