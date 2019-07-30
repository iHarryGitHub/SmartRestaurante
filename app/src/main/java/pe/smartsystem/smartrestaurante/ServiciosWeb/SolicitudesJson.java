package pe.smartsystem.smartrestaurante.ServiciosWeb;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;

import pe.smartsystem.smartrestaurante.VolleyRP;

public abstract class SolicitudesJson {

    public abstract void solicitudCompletada(JSONObject j);
    public abstract void solicitudErronea();

    public SolicitudesJson(){

    }

    public void solicitudJsonGET(Context c, String URL){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                solicitudCompletada(datos);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                solicitudErronea();
            }
        });
        VolleyRP.addToQueue(solicitud,VolleyRP.getInstance(c).getRequestQueue(),c,VolleyRP.getInstance(c));
    }

    public void solicitudJsonPOST(Context c, String URL, HashMap h){
        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST,URL,new JSONObject(h), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                solicitudCompletada(datos);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                solicitudErronea();
            }
        });
        VolleyRP.addToQueue(solicitud,VolleyRP.getInstance(c).getRequestQueue(),c,VolleyRP.getInstance(c));
    }

}
