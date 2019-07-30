package pe.smartsystem.smartrestaurante.ui.fragment.cuenta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import pe.smartsystem.smartrestaurante.R;
import pe.smartsystem.smartrestaurante.ServiciosWeb.SolicitudesJson;
import pe.smartsystem.smartrestaurante.VolleyRP;
import pe.smartsystem.smartrestaurante.ui.activity.main.MainActivity;
import pe.smartsystem.smartrestaurante.ui.fragment.cuenta.CuentaFragment;
import pe.smartsystem.smartrestaurante.ui.fragment.cuenta.pojo.DetalleMesaPojo;
import pe.smartsystem.smartrestaurante.ui.fragment.cuenta.PlatosDetalleRecyclerItemClickListener;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder>  {

    private List<DetalleMesaPojo> detalleMesaPojos;
    private Context c;
    private VolleyRP volley;
    private RequestQueue mRequest;

    private PlatosDetalleRecyclerItemClickListener recyclerItemClickListener;


    public CartListAdapter(List<DetalleMesaPojo> detalleMesaPojos) {
        this.detalleMesaPojos = detalleMesaPojos;
        this.c=c;
    }


    public void setItemListener(PlatosDetalleRecyclerItemClickListener itemListener){
        recyclerItemClickListener=itemListener;
    }

     MyViewHolder viewHolder;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_platos, parent, false);

          viewHolder = new MyViewHolder(itemView);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
        holder.name.setText(detalleMesaPojos.get(i).getNomproducto()+"");
        holder.mCantidad.setText(detalleMesaPojos.get(i).getCantidad()+"");
        //double pu=Double.parseDouble(detalleMesaPojos.get(i).getPu());

       double pu =Double.parseDouble(detalleMesaPojos.get(i).getPrecxprod());
        int cantidad=Integer.parseInt(detalleMesaPojos.get(i).getCantidad());
       // holder.mPU.setText(""+detalleMesaPojos.get(i).getPu());
        holder.mPU.setText(""+detalleMesaPojos.get(i).getPrecxprod());
        holder.mtotal.setText(""+pu*cantidad+"0");
    }

//    public void removeItem(int position){
//        detalleMesaPojos.remove(position);
//        notifyItemRemoved(position);
//    }


    @Override
    public int getItemCount() {
        return detalleMesaPojos.size();
    }

   /* @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }

    public void setOnClickListener(View.OnClickListener listener1){
        this.listener=listener1;
    }

    public void setOnLongClickListener(View.OnLongClickListener longClickListener){
        this.longClickListener=longClickListener;
    }

    @Override
    public boolean onLongClick(View view) {
        if (longClickListener!=null){
            longClickListener.onLongClick(view);
        }
        return true;
    }*/


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /*public TextView name, description, price;
        public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;*/

       private TextView name;
       private TextView mCantidad;
       private TextView mtotal;
       private TextView mPU;

       public RelativeLayout viewBackground;
       public ConstraintLayout viewForeground;


       private ImageView mAdd;
       private ImageView mLess;
        public MyViewHolder(@NonNull View view) {
            super(view);

          //  itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

          /* name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            price = view.findViewById(R.id.price);
            thumbnail = view.findViewById(R.id.thumbnail);
            ;
            ;
*/
          //viewBackground    = view.findViewById(R.id.view_background);
          viewForeground    = view.findViewById(R.id.view_foreground);

          mAdd              = view.findViewById(R.id.label_item_add);
          mLess             = view.findViewById(R.id.label_item_minus);
            volley = VolleyRP.getInstance(c);
            mRequest = volley.getRequestQueue();

          //boton mas
          mAdd.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  int i = Integer.parseInt(mCantidad.getText().toString())+1;
                  String can=""+i;
                  String pro=(String) name.getText().toString();
                  String pu=(String)mPU.getText().toString();

                  CuentaFragment cc= new CuentaFragment();
                  cc.ActualizamosCantidad(can,pro,pu);

                  double total=Double.parseDouble(pu)*Double.parseDouble(can);

                  mCantidad.setText(i+"");
                  mtotal.setText(""+total);

              }
          });
            //boton mas




          //boton menos
          mLess.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  int i = Integer.parseInt(mCantidad.getText().toString())-1;
                  String can=""+i;
                  String pro=(String) name.getText().toString();
                  String pu=(String)mPU.getText().toString();

                  CuentaFragment cc= new CuentaFragment();
                  cc.ActualizamosCantidad(can,pro,pu);

                  double total=Double.parseDouble(pu)*Double.parseDouble(can);

                  mCantidad.setText(String.valueOf(Integer.parseInt(mCantidad.getText().toString())-1));
                  mtotal.setText(""+total);

              }
          });
            //boton menos

          mCantidad         = view.findViewById(R.id.label_item_cantidad);
          mtotal            = view.findViewById(R.id.labe_item_plato_total);
          name              = view.findViewById(R.id.item_nombre_plato);
          mPU               = view.findViewById(R.id.label_item_pu);



        }

     /*   @Override
        public boolean onLongClick(View view) {
            int adapterPos = getAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION) {
                if (recyclerItemClickListener != null) {
                    //recyclerItemClickListener.onItemLongClick(adapterPos, viewHolder.itemView);
                }
            }
            return true ;
        }*/

        @Override
        public void onClick(View view) {
            int adapterPos = getAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION) {
                if (recyclerItemClickListener != null) {
                    recyclerItemClickListener.onItemClick(adapterPos, view );
                }
            }

        }
    }
}
