package pe.smartsystem.smartrestaurante.ui.fragment.top;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import pe.smartsystem.smartrestaurante.ConexionSQLITEhelper;
import pe.smartsystem.smartrestaurante.LoginActivity;
import pe.smartsystem.smartrestaurante.R;
import pe.smartsystem.smartrestaurante.ServiciosWeb.SolicitudesJson;
import pe.smartsystem.smartrestaurante.URLs.Links;
import pe.smartsystem.smartrestaurante.VolleyRP;
import pe.smartsystem.smartrestaurante.setting.Setting;
import pe.smartsystem.smartrestaurante.sqlite.SQLiteHandler;
import pe.smartsystem.smartrestaurante.ui.activity.main.MainActivity;
import pe.smartsystem.smartrestaurante.ui.fragment.top.adapter.TopAdapter;
import pe.smartsystem.smartrestaurante.ui.fragment.top.data.DataModel;

public class TopFragment extends Fragment {

    private ArrayList<DataModel> data;
    private TopAdapter adapter;
    private RecyclerView recyclerView;

    private TextView mCategoryName;

    private VolleyRP volley;
    private RequestQueue mRequest;

    private static final String URL_GET_TOP = Links.URL_TOP_GET;
    private static final String IP_REGISTRAR2 = Links.URL_INSERT_MESA_POST;
    String viene="";
    int id_categoria=0;
    String nomCatego="";

    private SQLiteHandler db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_top,container,false);

        // SQLite database handler
        db = new SQLiteHandler(getContext());


        cn=new ConexionSQLITEhelper(getContext(),"bdservidor",null,1);
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        data= new ArrayList<>();
        adapter= new TopAdapter(data);
        getActivity().setTitle("Platos mas vendidos");

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        Bundle bundle = this.getArguments();

        if(bundle != null){

            // mCategoryName.setVisibility(View.VISIBLE);
            id_categoria=bundle.getInt("key");
            viene = bundle.getString("viene");
            nomCatego=bundle.getString("key_name");
        }
        adapter.setOnItemClickAddListener(new TopAdapter.OnAddClickListener() {
            @Override
            public void onAddTempClick(int quantity, View view, String s) {
                DataModel model = data.get(recyclerView.getChildAdapterPosition(view));
                //Toast.makeText(getActivity(), model.getPlato(), Toast.LENGTH_SHORT).show();
                String CantxProd=""+quantity;
//PrecXProd se guarda
                //se leer precxprod
                String PrecXProd=model.getPrecioUnidad();

                String NomProducto=model.getNombreProducto();
                String NMesa= MainActivity.numero_mesa;
                String Mozo= LoginActivity.Nombre;
                String Cajero= LoginActivity.Nombre;
                String Destino=model.getDestino();
                String idproducto=model.getIdproducto();
                String idcategoria=model.getIdcategoria();
                String nombrecategoria=model.getNombreCategoria();
                registrarPlato(CantxProd,PrecXProd,NomProducto,NMesa,Mozo,Cajero,Destino,idproducto,idcategoria,nombrecategoria);
                Toasty.success(getActivity(), quantity+" "+s+" agregado", Toast.LENGTH_SHORT, true).show();
            }
        });

        SolicitudesJson s=new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
                try {
                    String variable="ProductosGeneral";
                    if(viene.equalsIgnoreCase("categoria")){
                        variable= "productos";
                    }
                    String TodoslasMesas= j.getString(variable);
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);
                        //AgregarTop(js.getString("NombreProducto"),js.getString("idProducto"));
                        DataModel f=new DataModel();
                        if(viene.equalsIgnoreCase("categoria")){
                            f.setIdproducto(js.getString("idproducto"));
                            f.setPlato(js.getString("nombreProducto"));
                            f.setPrecXProd(js.getString("precioUnidad"));
                            f.setDestino(js.getString("destino"));
                            f.setNombreCategoria(js.getString("nombrecategoria"));
                            data.add(f);
                            adapter.notifyDataSetChanged();
                        }
                    }

                }catch (JSONException e){
                    Toast.makeText(getContext(),"Error al descomponer el Json",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void solicitudErronea() {
                Toast.makeText(getContext(),"Error al consultar Top de Productos",Toast.LENGTH_SHORT).show();
            }
        };
        String urlViene="";

        if(viene.equalsIgnoreCase("categoria")){
            urlViene= Links.URL_LISTA_PRODUCTOS+nomCatego;
            s.solicitudJsonGET(getContext(),urlViene);
        }else {
            consultarlabd();
        }
        return view;
    }

    private void consultarlabd() {
        List<DataModel> allTags = cn.getAllTags();
        for (DataModel tag : allTags) {
            Log.e("Tag p", tag.getPrecioUnidad());
            data.add(tag);
        }
        adapter.notifyDataSetChanged();
    }
    private void registrarPlato(String CantxProd,
                                String PrecXProd,
                                String NomProducto,
                                String NMesa,
                                String Mozo,
                                String Cajero,
                                String Destino,
                                String idproducto,
                                String idcategoria,
                                String nombrecategoria){

        HashMap<String, String> hashMapToken = new HashMap<>();
        hashMapToken.put("CantxProd", CantxProd);
      hashMapToken.put("PrecXProd", PrecXProd);
        //hashMapToken.put("precxprod", PrecXProd);
        hashMapToken.put("NomProducto", NomProducto);
        //hashMapToken.put("nomproducto", NomProducto);
        hashMapToken.put("NMesa", NMesa);
        hashMapToken.put("Mozo", Mozo);
        hashMapToken.put("Cajero", Cajero);
        hashMapToken.put("Destino", Destino);
        hashMapToken.put("idproducto", idproducto);
        hashMapToken.put("idcategoria", idcategoria);
        hashMapToken.put("nombrecategoria", nombrecategoria);
        hashMapToken.put("codigoprod", idproducto);
        hashMapToken.put("NMesa2", NMesa);

        SolicitudesJson s=new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {

            }

            @Override
            public void solicitudErronea() {
                return;
            }
        };
        s.solicitudJsonPOST(getContext(),IP_REGISTRAR2,hashMapToken);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    ConexionSQLITEhelper cn;
    private void consultarListaIPs(){
        SQLiteDatabase bd=cn.getReadableDatabase();
        String ipcapturada="";
        Cursor cursor=bd.rawQuery("SELECT * FROM "+SQLiteHandler.TABLE_TOPLAS ,null);

        while (cursor.moveToNext()){
            ipcapturada=cursor.getString(0);





        }

        /*if(!ipcapturada.equalsIgnoreCase("")){
            Setting ipSetting = new Setting(ipcapturada);
            //arrayList.add(ipSetting);
            //adapter.notifyDataSetChanged();
        }*/

    }
}
