package pe.smartsystem.smartrestaurante.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import pe.smartsystem.smartrestaurante.ConexionSQLITEhelper;
import pe.smartsystem.smartrestaurante.IP;
import pe.smartsystem.smartrestaurante.ui.activity.login.LoginActivity;
import pe.smartsystem.smartrestaurante.R;
import pe.smartsystem.smartrestaurante.ServiciosWeb.SolicitudesJson;
import pe.smartsystem.smartrestaurante.Utilidades.Utilidades;

public class SettingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SettingAdapter adapter;
    private ArrayList<Setting> arrayList=new ArrayList<>();
    ArrayList<String> listaInformacion;
    ArrayList<IP> listaIP;

    private EditText mAdminEditText,mPassEditText,mIpEditText;
    ConexionSQLITEhelper cn=new ConexionSQLITEhelper(this,"bdservidor",null,1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("CONFIGURACION");

        mAdminEditText  = findViewById(R.id.label_setting_et_admin);
        mPassEditText   = findViewById(R.id.label_setting_et_passwrod);
        mIpEditText     = findViewById(R.id.label_setting_et_ip);

        recyclerView=findViewById(R.id.label_setting_rv_ip);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new SettingAdapter(arrayList);
        recyclerView.setAdapter(adapter);


        consultarListaIPs();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setOnClickAddListener(View view) {
        String ip = mIpEditText.getText().toString();

        if (TextUtils.isEmpty(ip)){
            Toasty.error(SettingActivity.this, "Ingrese Ip").show();
           return;
        }

        if(mAdminEditText.getText().toString().equals("admin")&&mPassEditText.getText().toString().equals("rtsSmartSystem+@")){
            RegistrarIP(mIpEditText.getText().toString());
        }else {
            Toast.makeText(getApplicationContext(),"Usuaio y/o Contraseña Incorrecto ",Toast.LENGTH_SHORT).show();
        }


        /*Setting ipSetting = new Setting(ip);

        arrayList.add(ipSetting);

        adapter.notifyDataSetChanged();
        mIpEditText.setText("");*/

    }

    private void consultarListaIPs(){
        SQLiteDatabase bd=cn.getReadableDatabase();
        String ipcapturada="";
        Cursor cursor=bd.rawQuery("SELECT * FROM "+Utilidades.TABLA,null);

        while (cursor.moveToNext()){
            ipcapturada=cursor.getString(0);
        }

        if(!ipcapturada.equalsIgnoreCase("")){
            Setting ipSetting = new Setting(ipcapturada);
            arrayList.add(ipSetting);
            adapter.notifyDataSetChanged();
        }

    }


    private void RegistrarIP(String ip){

        SQLiteDatabase bd=cn.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Utilidades.IP,ip);

        Long ipRegistrado=bd.insert(Utilidades.TABLA,Utilidades.IP,values);
        Toasty.info(getApplicationContext(),"IP registrado : "+ipRegistrado,Toast.LENGTH_SHORT).show();
        bd.close();
        startActivity(new Intent(SettingActivity.this,LoginActivity.class));
        finish();
    //  descargarToplasTop(ip);


    }

//    private void descargarToplasTop(String ip) {
//        Log.e("ip", "http://"+ip+"/WS/TopProductos.php");
//        Toasty.info(this, "Descargando...", Toast.LENGTH_SHORT).show();
//
//
//        SolicitudesJson json = new SolicitudesJson() {
//            @Override
//            public void solicitudCompletada(JSONObject j) {
//                try {
//                    String variable = "ProductosGeneral";
//                    cn.deleteUser();
//                    String TodoslasMesas= j.getString(variable);
//                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
//                    for(int i=0;i<jsonArray.length();i++){
//                        JSONObject js = jsonArray.getJSONObject(i);
//                        try{
//                            guardarToplas(js.getString("idProducto")+""
//                                    , js.getString("NombreProducto")+""
//                                    , js.getString("PrecioUnidad")+""
//                                    , js.getString("Destino")+""
//                                    , js.getString("NombreCategoria"));
//                        }catch (Exception e){
//                        }
//                    }
//
//                    Toasty.success(SettingActivity.this, "PLATOS GUARDADOS", Toast.LENGTH_SHORT).show();
//
//                }catch (JSONException e){
//                    Toast.makeText(SettingActivity.this,"Error al guardar los platos",Toast.LENGTH_SHORT).show();
//                    finish();
//                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
//                }
//            }
//
//            @Override
//            public void solicitudErronea(VolleyError error) {
//                Toasty.error(SettingActivity.this, "Error", Toasty.LENGTH_SHORT ,true).show();
//
//            }
//        };
//        final String url_top="http://"+ip+"/WS/TopProductos.php";
//        json.solicitudJsonGET(this,url_top);
//
//    }









    private static final String idproducto="idproducto";
    private static final String nombreProducto="nombreProducto";
    private static final String precioUnidad="precioUnidad";
    private static final String destino="destino";
    private static final String nombrecategoria="NombreCategoria";


    private void guardarToplas(String id,String nombre,String precio,String des,String nombCatego ){

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


}
