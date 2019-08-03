package pe.smartsystem.smartrestaurante.ui.activity.message;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import pe.smartsystem.smartrestaurante.R;
import pe.smartsystem.smartrestaurante.ServiciosWeb.SolicitudesJson;
import pe.smartsystem.smartrestaurante.URLs.Links;
import pe.smartsystem.smartrestaurante.Utilidades.SessionManager;
import pe.smartsystem.smartrestaurante.VolleyRP;
import pe.smartsystem.smartrestaurante.ui.activity.main.MainActivity;
import pe.smartsystem.smartrestaurante.ui.activity.message.adapter.CatAdapter;
import pe.smartsystem.smartrestaurante.ui.activity.message.adapter.OnSelectedListener;
import pe.smartsystem.smartrestaurante.ui.activity.message.adapter.SelecAdapter;
import pe.smartsystem.smartrestaurante.ui.activity.message.pojo.MensajeDetailPojo;
import pe.smartsystem.smartrestaurante.ui.activity.message.pojo.SelectedPojo;
import pe.smartsystem.smartrestaurante.ui.fragment.cuenta.pojo.DetalleMesaPojo;

public class MessageActivity extends AppCompatActivity implements OnSelectedListener {

    DetalleMesaPojo detalleMesaPojo;
    private int PosicionGlobal=0;

    private TextView mNameTv;
    public static String MensajeObtenido;

    private SelectedPojo selectedPojoAux=new SelectedPojo();

    private RecyclerView mCategoriaRv;
    private RecyclerView mDetailRv;
    private RecyclerView mNumberProductRv;

    private CatAdapter catAdapter;
    private DetailAdapter detailAdapter;
    static SelecAdapter selecAdapter;

    private ArrayList<MessageCatPojo> messageCatPojos = new ArrayList<>();
    private ArrayList<MensajeDetailPojo> mensajeDetailPojos = new ArrayList<>();
    private ArrayList<SelectedPojo> selectedPojos=new ArrayList<>();


    private VolleyRP volley;
    private RequestQueue mRequest;
    private static final String URL_GET_CABEMENSAJE = Links.URL_CABEMENSAJE_GET;
    private static final String URL_GET_MENSAJE = Links.URL_MENSAJE_GET;
    private static final String URL_GET_ACTUALIZARMENSAJE = Links.URL_INSERTMENSAJE_POST;


    private SessionManager manager;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        volley = VolleyRP.getInstance(MessageActivity.this);
        mRequest = volley.getRequestQueue();

        detalleMesaPojo=(DetalleMesaPojo) getIntent().getExtras().get("extra");

        manager=new SessionManager(this);

        assert detalleMesaPojo!=null;
        mNameTv = findViewById(R.id.label_message_name_tv);

        mNameTv.setText(detalleMesaPojo.getNomproducto()+"");


        buildRv();

        addButtons();

        ((Button)findViewById(R.id.label_message_button_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String gusano="";
                for(int i=0;i<selectedPojos.size();i++){
                    gusano += selectedPojos.get(i).getName()+"/";
                }

                int contador=gusano.length();

                gusano=gusano.substring(0,contador-1);
                gusano=gusano.replaceAll(":,",":");
                gusano=gusano.replaceAll(" ,"," ");

                final String gusano2=gusano;

                SolicitudesJson s= new SolicitudesJson() {
                    @Override
                    public void solicitudCompletada(JSONObject j) {
                        Toasty.success(MessageActivity.this, "Detalles agregados!", Toast.LENGTH_SHORT, true).show();
                        finish();
                    }

                    @Override
                    public void solicitudErronea(VolleyError error) {
                        Toast.makeText(MessageActivity.this,error.getMessage()+"No se Agrego el mensaje correctamente",Toast.LENGTH_SHORT).show();
                    }
                };
                String msj=gusano;
                String nmesa= MainActivity.numero_mesa;
                String idpro=detalleMesaPojo.getIdpro();

                HashMap<String, String> hashMapToken = new HashMap<>();

                hashMapToken.put("NMesa", nmesa);
                hashMapToken.put("Mensaje", msj);
                hashMapToken.put("idProducto", idpro);

                s.solicitudJsonPOST(MessageActivity.this,"http://"+manager.getIp()+"/WS/Mensaje_UPDATE.php",hashMapToken);



                //finish();
                //Toasty.success(MessageActivity.this, "Detalles agregados!", Toast.LENGTH_SHORT, true).show();

            }
        });

        ((Button)findViewById(R.id.label_message_button_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PosicionGlobal=PosicionGlobal+1;
                String mensaje=""+PosicionGlobal+": ";
                selecAdapter.cleanItem(iSelectedPosition,mensaje);

                selectedPojos.remove(iSelectedPosition);
                selectedPojos.add(iSelectedPosition,new SelectedPojo(detalleMesaPojo.getNomproducto()));
                selectedPojoAux.setName(mensaje);

                //Toasty.error(MessageActivity.this, "Plato Eliminado!", Toast.LENGTH_SHORT, true).show();
                //finish();

            }
        });


    }

    private void LlenamosMsj(){

        String nmesa= MainActivity.numero_mesa;
        String idpro=detalleMesaPojo.getIdpro();
        String url="http://"+manager.getIp()+"/WS/consultarMensajedeProducto.php"+"?nroMesa="+nmesa+"&idProducto="+idpro;
        Log.e("URL", url);
        SolicitudesJson s= new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
                Log.e("MJS", j.toString());
                try {
                    String TodoslasMesas= j.getString("mesajesPro");
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);
                        MensajeObtenido=js.getString("Mensaje");
                    }
                    String msj=MensajeObtenido;

                    if(msj==null||msj.equalsIgnoreCase("null")||msj.equalsIgnoreCase("")){
                        int quantity=Integer.parseInt(getIntent().getStringExtra("quantity"));

                        for (int i =0;i<quantity;i++){
                            SelectedPojo pojo=new SelectedPojo();
                            pojo.setName((i+1)+":");
                            pojo.setOrder((i+1)+"");
                            selectedPojos.add(pojo);
                        }
                    }else {
                        String[] datos = msj.split("/");

                        int quantity = Integer.parseInt(getIntent().getStringExtra("quantity"));

                        for (int i = 0; i < quantity; i++) {
                            SelectedPojo pojo = new SelectedPojo();
                            int posicionG = i + 1;
                            for (int j2 = 0; j2 < datos.length; j2++) {
                                String filas[] = datos[j2].split(":");
                                // String filas[] = datos[j2].split(",");
                                String id = filas[0];
                                String mens = filas[1];
                                int posicion = i + 1;
                                if (posicion == Integer.parseInt(id)) {
                                    pojo.setName(id + ":" + mens);
                                    //pojo.setName(id + "," + mens);
                                    pojo.setOrder((i + 1) + "");
                                    selectedPojos.add(pojo);
                                }
                            }
                            if (posicionG > datos.length) {
                                pojo.setName("" + posicionG + ":");
                                pojo.setOrder((i + 1) + "");
                                selectedPojos.add(pojo);
                            }
                        }
                    }

                    selecAdapter.notifyDataSetChanged();
                    selectedPojoAux.setName(detalleMesaPojo.getNomproducto());
                }catch (JSONException e){
                    Toast.makeText(MessageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void solicitudErronea(VolleyError error) {
                Toasty.error(MessageActivity.this, "LlenamosMsj "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        s.solicitudJsonGET(MessageActivity.this,url);
    }

    private void addButtons() {

        mensajesCabe();
        LlenamosMsj();

    }


    private void buildRv() {
        mCategoriaRv    = findViewById(R.id.label_mensaje_cat_rv);
        mDetailRv       = findViewById(R.id.label_mensaje_detail_rv);
        mNumberProductRv    = findViewById(R.id.label_message_rv_number_product);

        catAdapter= new CatAdapter(messageCatPojos);
        detailAdapter= new DetailAdapter(mensajeDetailPojos);
        selecAdapter    =new SelecAdapter(selectedPojos,MessageActivity.this);

        mCategoriaRv.setLayoutManager(new LinearLayoutManager(MessageActivity.this));//vertical
        mDetailRv.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        mNumberProductRv.setLayoutManager(new LinearLayoutManager(MessageActivity.this));

        mCategoriaRv.setAdapter(catAdapter);
        mDetailRv.setAdapter(detailAdapter);
        mNumberProductRv.setAdapter(selecAdapter);

        catAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MessageCatPojo pojo =messageCatPojos.get(mCategoriaRv.getChildAdapterPosition(view));
                String categoria=pojo.getName();
                int idmsj=pojo.getId();
                SolicitudMensajes(""+idmsj);

            }
        });


        detailAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final MensajeDetailPojo pojo =mensajeDetailPojos.get(mCategoriaRv.getChildAdapterPosition(view));
                String mensaje=pojo.getName();
                try{
                    mensaje=mensaje.replaceAll(":,",": ");
                    mensaje=mensaje.replaceAll(" ,"," ");
                    selecAdapter.addDetailItem(iSelectedPosition,selectedPojoAux,mensaje);
                    Toasty.info(MessageActivity.this, mensaje+" agregado...", Toast.LENGTH_SHORT, true).show();

                }catch (Exception e){
                    Toasty.error(MessageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void mensajesCabe(){
        JsonObjectRequest solicitud = new JsonObjectRequest("http://"+manager.getIp()+"/WS/ConsultarMensajes.php?idmensajes=1", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String TodoslasMesas= datos.getString("mensajes");
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);
                        MessageCatPojo xm = new MessageCatPojo(js.getInt("idmensajes"),js.getString("descripcion"));
                        messageCatPojos.add(xm);
                        catAdapter.notifyDataSetChanged();
                    }
                }catch (JSONException e){
                    Toast.makeText(MessageActivity.this,"Error al descomponer el Json",Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(MessageActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                //Toast.makeText(MessageActivity.this,"Error al consultar Mensajes",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,MessageActivity.this,volley);
    }

    public void SolicitudMensajes(String idMensaje){
        mensajeDetailPojos.removeAll(mensajeDetailPojos);
        JsonObjectRequest solicitud = new JsonObjectRequest("http://"+manager.getIp()+"/WS/ConsultarMensajesDetalle.php?idmensajes="+idMensaje, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String TodoslasMesas= datos.getString("mesajesDetallado");
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);
                        MensajeDetailPojo xm = new MensajeDetailPojo(js.getString("descripcion"));
                        mensajeDetailPojos.add(xm);
                        detailAdapter.notifyDataSetChanged();
                    }
                }catch (JSONException e){
                    Toast.makeText(MessageActivity.this,"Error al descomponer el Json",Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MessageActivity.this,"SolicitudMensajes "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,MessageActivity.this,volley);
    }

    private int iSelectedPosition=0;

    @Override
    public void onSelecteClick(int position) {
        iSelectedPosition=position;
        PosicionGlobal=iSelectedPosition;
        selectedPojoAux.setName(selectedPojos.get(position).getName());
    }

}
