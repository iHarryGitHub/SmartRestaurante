package pe.smartsystem.smartrestaurante.ui.fragment.category;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import java.util.List;

import pe.smartsystem.smartrestaurante.ConexionSQLITEhelper;
import pe.smartsystem.smartrestaurante.R;
import pe.smartsystem.smartrestaurante.URLs.Links;
import pe.smartsystem.smartrestaurante.VolleyRP;
import pe.smartsystem.smartrestaurante.ui.fragment.top.TopFragment;
import pe.smartsystem.smartrestaurante.ui.fragment.top.data.DataModel;

public class CategoryFragment extends Fragment  implements OnCategoryPlatosListener{
    //ID CATEGORIA
    //CHAPA ID MOSTRAR LOS PRODUCTO DE ESA CATEGORIA

    private RecyclerView recyclerView;
    private CategoraPlatosAdapter adapter;
    private ArrayList<CategoriaPlato> arrayList=new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_categorias,container,false);

        cn=new ConexionSQLITEhelper(getContext(),"bdservidor",null,1);


        adapter=new CategoraPlatosAdapter(arrayList,this);
        recyclerView=view.findViewById(R.id.categoria_plastos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


//        JsonObjectRequest solicitud = new JsonObjectRequest(Links.URL_LISTA_CATEGORIAS, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject datos) {
//                try {
//                    String TodoslasMesas= datos.getString("categorias");
//                    JSONArray jsonArray = new JSONArray(TodoslasMesas);
//                    for(int i=0;i<jsonArray.length();i++){
//                        JSONObject js = jsonArray.getJSONObject(i);
//                        //AgregarCategorias(js.getString("NombreCategoria"),js.getString("IdCategoria"));
//                        CategoriaPlato b =new CategoriaPlato(js.getString("NombreCategoria"),js.getInt("IdCategoria"));
//                        arrayList.add(b);
//                        adapter.notifyDataSetChanged();
//                    }
//                }catch (JSONException e){
//                    Toast.makeText(getContext(),"Error al descomponer el Json",Toast.LENGTH_SHORT).show();
//                }
//            }
//        },new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext(),"Error al consultar Categorias",Toast.LENGTH_SHORT).show();
//            }
//        });
//        VolleyRP.addToQueue(solicitud,mRequest,getContext(),volley);



        consultarlabd();

        return view;
    }

    ConexionSQLITEhelper cn;
    private void consultarlabd() {
        ArrayList<CategoriaPlato> allTags = cn.getCategorias();
        for (CategoriaPlato tag : allTags) {
            Toast.makeText(getContext(), ""+tag.getName(), Toast.LENGTH_SHORT).show();
            arrayList.add(tag);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClickCategotyPlatosListener(CategoriaPlato plato, String viene) {

        Bundle bundle = new Bundle();
        bundle.putString("key_name", plato.getName());
        bundle.putInt("key",plato.getId()); // Put anything what you
        bundle.putString("viene",viene);

        TopFragment fragment2 = new TopFragment();
        fragment2.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment2);
        //fragmentTransaction.addToBackStack(null);
        // fragmentTransaction.remove(this);
        fragmentTransaction.commit();

    }

    /*@Override
    public void onClickCategotyPlatosListener(CategoriaPlato plato) {

        Bundle bundle = new Bundle();
        bundle.putString("key_name", plato.getName());
        bundle.putInt("key",plato.getId()); // Put anything what you

        TopFragment fragment2 = new TopFragment();
        fragment2.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment2);
        //fragmentTransaction.addToBackStack(null);
        // fragmentTransaction.remove(this);
        fragmentTransaction.commit();

    }*/
}
