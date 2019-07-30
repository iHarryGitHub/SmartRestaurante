package pe.smartsystem.smartrestaurante.ui.activity.mesas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import pe.smartsystem.smartrestaurante.AutoFitGridLayoutManager;
import pe.smartsystem.smartrestaurante.LoginActivity;
import pe.smartsystem.smartrestaurante.R;
import pe.smartsystem.smartrestaurante.ServiciosWeb.SolicitudesJson;
import pe.smartsystem.smartrestaurante.URLs.Links;
import pe.smartsystem.smartrestaurante.VolleyRP;
import pe.smartsystem.smartrestaurante.ui.activity.MesaPojo;
import pe.smartsystem.smartrestaurante.ui.activity.main.MainActivity;
import pe.smartsystem.smartrestaurante.ui.activity.mesas.adapter.AdapterListaMesas;

public class TableActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private AdapterListaMesas adapterCotizacion;
    private ArrayList<MesaPojo> mesaPojos;
    private static final String URL_GET_MESAS = Links.URL_MESAS_GET;
    private VolleyRP volley;
    private RequestQueue mRequest;

    private void refresh() {

        mesaPojos.removeAll(mesaPojos);
        adapterCotizacion.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showTables();
            }
        }, 1000) ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        volley = VolleyRP.getInstance(TableActivity.this);
        mRequest = volley.getRequestQueue();

        setTitle(LoginActivity.Personal);

        mesaPojos = new ArrayList<>();
        recyclerView    = findViewById(R.id.label_profile_lista_mesas_rv);
        adapterCotizacion   = new AdapterListaMesas(mesaPojos);

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

        SolicitudesJson s=new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
                MesaPojo consultita=null;
                try {
                    String TodoslasMesas= j.getString("mesas");
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);
                        if(js.getString("Estado").equalsIgnoreCase("Ocupada")){
                            consultita= new MesaPojo(js.getInt("NMesa"),"ocupada");
                            mesaPojos.add(consultita);
                            adapterCotizacion.notifyDataSetChanged();
                        }else if(js.getString("Estado").equalsIgnoreCase("Libre")){
                            consultita= new MesaPojo(js.getInt("NMesa"),"libre");
                            mesaPojos.add(consultita);
                            adapterCotizacion.notifyDataSetChanged();
                        }else if(js.getString("Estado").equalsIgnoreCase("En Caja")){
                            consultita= new MesaPojo(js.getInt("NMesa"),"espera");
                            mesaPojos.add(consultita);
                            adapterCotizacion.notifyDataSetChanged();
                        }

                    }

                }catch (JSONException e){
                    Toasty.info(TableActivity.this, "error al descomponer JSON", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void solicitudErronea() {
                Toasty.info(TableActivity.this,"No ay conexion",Toast.LENGTH_SHORT,true).show();
            }
        };
        s.solicitudJsonGET(TableActivity.this,URL_GET_MESAS);


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
        if (id == R.id.action_settings) {
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
