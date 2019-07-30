package pe.smartsystem.smartrestaurante.ui.activity.mesas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

import pe.smartsystem.smartrestaurante.R;
import pe.smartsystem.smartrestaurante.ui.activity.MesaPojo;


public class AdapterListaMesas extends RecyclerView.Adapter<AdapterListaMesas.ViewHolderCotizacion> implements View.OnClickListener {

    private ArrayList<MesaPojo> mesaPojos;
    private View.OnClickListener listener;

    public AdapterListaMesas(ArrayList<MesaPojo> list){
        this.mesaPojos=list;
    }

    @Override
    public ViewHolderCotizacion onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mesa,viewGroup,false);
        view.setOnClickListener(this);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new ViewHolderCotizacion(view);
    }

    @Override
    public void onBindViewHolder( ViewHolderCotizacion holder, int i) {

        holder.nMesa.setText(mesaPojos.get(i).getnMesa()+"");

        if (mesaPojos.get(i).getStadomesa().equals("ocupada")){
            holder.imageView.setImageResource(R.drawable.rojo);
          //  holder.constraintLayout.setBackgroundColor(Color.parseColor("#E4BFBB"));
        }else if(mesaPojos.get(i).getStadomesa().equals("libre")){
            holder.imageView.setImageResource(R.drawable.verde);
        }else if (mesaPojos.get(i).getStadomesa().equals("espera")){
            holder.imageView.setImageResource(R.drawable.azul);
       //     holder.constraintLayout.setBackgroundColor(Color.parseColor("#ABE0F8"));
        }

    }

    @Override
    public int getItemCount() {
        return mesaPojos.size();
    }

    public void setOnClicListener(View.OnClickListener listener1){
        this.listener=listener1;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public class ViewHolderCotizacion extends RecyclerView.ViewHolder{

        ImageView imageView;
       // TextView state;
        TextView nMesa;
        ConstraintLayout constraintLayout;


        public ViewHolderCotizacion( View itemView) {
            super(itemView);
            this.constraintLayout=itemView.findViewById(R.id.label_mesa_cl);
            imageView=itemView.findViewById(R.id.imageView3);
           // state           = itemView.findViewById(R.id.label_item_state);
            nMesa = itemView.findViewById(R.id.label_item_nmesa);

        }
    }
}
