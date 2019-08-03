package pe.smartsystem.smartrestaurante.ui.fragment.cuenta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import pe.smartsystem.smartrestaurante.R;
import pe.smartsystem.smartrestaurante.ServiciosWeb.SolicitudesJson;
import pe.smartsystem.smartrestaurante.URLs.Links;
import pe.smartsystem.smartrestaurante.Utilidades.SessionManager;
import pe.smartsystem.smartrestaurante.VolleyRP;
import pe.smartsystem.smartrestaurante.ui.activity.MesaPojo;
import pe.smartsystem.smartrestaurante.ui.activity.message.MessageActivity;
import pe.smartsystem.smartrestaurante.ui.activity.main.MainActivity;
import pe.smartsystem.smartrestaurante.ui.fragment.cuenta.adapter.CartListAdapter;
import pe.smartsystem.smartrestaurante.ui.fragment.cuenta.pojo.DetalleMesaPojo;

public class CuentaFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_botton_platos)
    RecyclerView recyclerView;
    @BindView(R.id.refreshCuenta)
    SwipeRefreshLayout refreshCuenta;
   // private RecyclerView recyclerView;
    private CartListAdapter mAdapter;
    private MesaPojo mesaPojo;
    List<DetalleMesaPojo> detalleMesaPojos;
    private static final String URL_GET_MESAS = Links.URL_DETALLEMESA_GET;
    private static final String IP_ACTUALIZAR = Links.URL_ACTUALIZARMESA_POST;
    private VolleyRP volley;
    private RequestQueue mRequest;

    private SessionManager manager;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuenta, container, false);
        ButterKnife.bind(this,view);

        volley = VolleyRP.getInstance(getContext());
        mRequest = volley.getRequestQueue();

        manager = new SessionManager(getContext());

        refreshCuenta.setRefreshing(true);
        refreshCuenta.setOnRefreshListener(this);
        //recyclerView = view.findViewById(R.id.rv_botton_platos);
        detalleMesaPojos = new ArrayList<>();
        mAdapter = new CartListAdapter(detalleMesaPojos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

        mAdapter.setItemListener(new PlatosDetalleRecyclerItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                DetalleMesaPojo pojo = detalleMesaPojos.get(recyclerView.getChildAdapterPosition(view));

                Intent i = new Intent(getActivity(), MessageActivity.class);
                i.putExtra("extra", pojo);
                i.putExtra("quantity", pojo.getCantidad());
                startActivity(i);
            }


        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Actualizando Platos");
        progressDialog.setMessage("Un momento porfavor...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        showToplas();

        return view;
    }


    private void showToplas() {

        //validar si el json esta vacio!!!!!!!!!!

        SolicitudesJson s = new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {

//                Toast.makeText(getContext(), ""+j.toString(), Toast.LENGTH_SHORT).show();
                try {
                    String TodoslasMesas = j.getString("mesasDetalle");
                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        DetalleMesaPojo detalleMesaPojo = new DetalleMesaPojo();
                        detalleMesaPojo.setCantidad(js.getString("cantxprod"));
                        detalleMesaPojo.setNomproducto(js.getString("nomproducto"));
                        detalleMesaPojo.setPrecxprod(js.getString("precxprod"));
                        // detalleMesaPojo.setPu(js.getString("precxprod"));
                        detalleMesaPojo.setIdpro(js.getString("idproducto"));
                        detalleMesaPojos.add(detalleMesaPojo);
                        mAdapter.notifyDataSetChanged();
                    }
                    refreshCuenta.setRefreshing(false);
                    progressDialog.cancel();

                } catch (JSONException e) {
                    progressDialog.cancel();
                    Toasty.info(getContext(), "Mesa vacia", Toast.LENGTH_SHORT, true).show();
                    refreshCuenta.setRefreshing(false);
                    //Toasty.info(getContext(), "error al descomponer JSON", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void solicitudErronea(VolleyError error) {
                progressDialog.cancel();
                Toasty.info(getContext(), error.getMessage() + "", Toast.LENGTH_LONG, true).show();
                refreshCuenta.setRefreshing(false);
            }
        };
        //s.solicitudJsonGET(getContext(),URL_GET_MESAS+ MainActivity.numero_mesa);

        s.solicitudJsonGET(getContext(), "http://" + manager.getIp() + "/WS/consultarDetalleMesa.php?nromesa=" + MainActivity.numero_mesa);


    }


    public void ActualizamosCantidad(final String can, final String pro, final String pu) {

        HashMap<String, String> hashMapToken = new HashMap<>();
        hashMapToken.put("Cantidad", can);
        hashMapToken.put("NMesa", MainActivity.numero_mesa);
        hashMapToken.put("Producto", pro);
        hashMapToken.put("PrecU", pu);

        SolicitudesJson s = new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
            }

            @Override
            public void solicitudErronea(VolleyError error) {
            }
        };
        s.solicitudJsonPOST(getContext(), IP_ACTUALIZAR, hashMapToken);
    }


    @Override
    public void onRefresh() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Actualizando Platos");
        progressDialog.setMessage("Un momento porfavor...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        detalleMesaPojos.removeAll(detalleMesaPojos);
        mAdapter.notifyDataSetChanged();
        showToplas();
    }
}
