package pe.smartsystem.smartrestaurante.ui.activity.main;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import pe.smartsystem.smartrestaurante.LoginActivity;
import pe.smartsystem.smartrestaurante.R;
import pe.smartsystem.smartrestaurante.ServiciosWeb.SolicitudesJson;
import pe.smartsystem.smartrestaurante.URLs.Links;
import pe.smartsystem.smartrestaurante.VolleyRP;
import pe.smartsystem.smartrestaurante.ui.activity.MesaPojo;
import pe.smartsystem.smartrestaurante.ui.fragment.category.CategoryFragment;
import pe.smartsystem.smartrestaurante.ui.fragment.cuenta.CuentaFragment;
import pe.smartsystem.smartrestaurante.ui.fragment.top.TopFragment;

public class MainActivity extends AppCompatActivity {
    MesaPojo mesaPojo;

    public static String numero_mesa;
    private VolleyRP volley;
    private RequestQueue mRequest;
    private static final String URL_SUMAVENTAS_GET = Links.URL_RECORDMOZOS_GET;
    TextView txtSumaMax;
    TextView txtSumaActual;
    TextView label_id_mesa;
    TextView label_main_tv_moso;
    boolean b=false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;
            switch (item.getItemId()) {

                case R.id.navigation_print:
                    print();
                    return false;

                case R.id.navigation_top:
                    if (b) {
                        selectedFragment=new CategoryFragment();
                        b = false;
                    }else {
                        selectedFragment    = new TopFragment();//*******************//*******************//*******************
                        b = true;
                    }
                    break;
                case R.id.navigation_table:
                    b=(!b);//*******************//*******************//*******************//*******************//*******************
                    selectedFragment = new CuentaFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };

    private void print() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Enviar Comandas?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toasty.info(MainActivity.this,"Imprimiendo",Toasty.LENGTH_SHORT,true).show();
                        EnviarComanda(numero_mesa,LoginActivity.Personal.replace(" MOZO",""));
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void EnviarComanda(final String nmesa, String mozo){

        String url=Links.URL_ENV_COMANDAS+"Nmesa="+nmesa+"&mozo="+mozo;

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject datos) {
                Toast.makeText(MainActivity.this, "Se envio las comandas correctamente", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "No ay pendiente de envio de comandas", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud, mRequest, MainActivity.this, volley);
    }


    //private ProgressBar progreso;
    //private ProgressBar progresoSemanal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navView.setSelectedItemId(R.id.navigation_top);

        volley = VolleyRP.getInstance(MainActivity.this);
        mRequest = volley.getRequestQueue();

        //txtSumaActual=(TextView)findViewById(R.id.txtSumaActual);
        //txtSumaMax=(TextView)findViewById(R.id.txtSumaMax);
        label_id_mesa = (TextView)findViewById(R.id.label_id_mesa);
        label_main_tv_moso = (TextView)findViewById(R.id.label_main_tv_moso);


        //
        mesaPojo= (MesaPojo) getIntent().getExtras().get("extra");

        numero_mesa=mesaPojo.getnMesa()+"";

        label_main_tv_moso.setText(LoginActivity.Nombre);
        label_id_mesa.setText("Mesa : "+numero_mesa);

        //progreso =findViewById(R.id.progressBar4);
        //progresoSemanal=findViewById(R.id.progressBar3);


        //ValidamosVentas();

        /*progreso.getProgressDrawable().setColorFilter(
                Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);



        progresoSemanal.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);*/

        if (mesaPojo.getStadomesa().equals("ocupada")){
            navView.setSelectedItemId(R.id.navigation_table);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CuentaFragment()).commit();

        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TopFragment()).commit();
        }





    }

    private void ValidamosVentas(){
        SolicitudesJson s=new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
                try {
                    String TodoslasMesas= j.getString("preferencias");
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    String SM="";
                    String smenor="";
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);
                        SM=js.getString("SumaMayor");
                        smenor=js.getString("SumaActual");
                    }

                    double calculando=0;
                    calculando=Double.parseDouble(SM)-Double.parseDouble(smenor);
                    calculando=calculando/Double.parseDouble(SM);
                    calculando=calculando*100;

                    int entero = (int) calculando;
                    String to=""+entero;
                    if(to.equalsIgnoreCase("0.0")||to.equalsIgnoreCase("0")){
                        to="100";
                    }else{
                        double nf=100-entero;
                        entero=(int)nf;
                        to=""+entero;
                    }

                    /*ProgressBarAnimation mProgressAnimation = new ProgressBarAnimation(progreso, 2000);
                    mProgressAnimation.setProgress(100);
                    txtSumaMax.setText("100%");
                    ProgressBarAnimation mProgressAnimatio2n = new ProgressBarAnimation(progresoSemanal, 2000);
                    mProgressAnimatio2n.setProgress(Integer.parseInt(to));
                    txtSumaActual.setText(to+"%");*/
                }catch (JSONException e){
                    Toasty.info(MainActivity.this, "error al descomponer JSON", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void solicitudErronea() {
                Toasty.info(MainActivity.this,"Error al consultar progreso",Toast.LENGTH_SHORT,true).show();
            }
        };
        s.solicitudJsonGET(MainActivity.this,URL_SUMAVENTAS_GET+LoginActivity.Nombre);
    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

}
