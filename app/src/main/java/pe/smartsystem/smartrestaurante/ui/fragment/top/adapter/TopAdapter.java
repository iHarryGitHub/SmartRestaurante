package pe.smartsystem.smartrestaurante.ui.fragment.top.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.matrixxun.starry.badgetextview.MaterialBadgeTextView;


import java.util.ArrayList;

import pe.smartsystem.smartrestaurante.R;
import pe.smartsystem.smartrestaurante.ui.fragment.top.data.DataModel;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.MyViewHolder> {
    private ArrayList<DataModel> dataSet;
    private OnAddClickListener mListener;

    public TopAdapter(ArrayList<DataModel> dataSet) {
        this.dataSet = dataSet;
    }



    public interface OnAddClickListener{
        void onAddTempClick(int quantity, View view, String s);///cambio int x view
    }
    public void setOnItemClickAddListener(OnAddClickListener listener){
        mListener=listener;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.platos_mas_vendidos_layout, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);
        return new MyViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        TextView textViewName = holder.textViewName;


        //textViewName.setText(dataSet.get(position).getPlato());
        textViewName.setText(dataSet.get(position).getNombreProducto());

        holder.mQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mQuantity.setText("");
            }
        });




    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        ImageView view;
       // private ImageView viewOptios;
        private EditText mQuantity;
        private MaterialBadgeTextView textView;


        public MyViewHolder(@NonNull final View itemView,
                            final OnAddClickListener listener) {
            super(itemView);

            this.textViewName   = itemView.findViewById(R.id.label_nombre_plato);
            this.view           = itemView.findViewById(R.id.imageviewPlus);
            this.mQuantity      = itemView.findViewById(R.id.label_quantity);
            this.textView            = itemView.findViewById(R.id.materialBadgeTextView2);
            //this.viewOptios     = itemView.findViewById(R.id.label_item_optios);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = Integer.parseInt(mQuantity.getText().toString())+1;
                    mQuantity.setText(i+"");
                }
            });


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!= null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION  &&  !mQuantity.getText().toString().equals("") && !mQuantity.getText().toString().equals("0")){
                            int quantity = Integer.parseInt(mQuantity.getText().toString());

                            listener.onAddTempClick(quantity,itemView,textViewName.getText().toString());///*************AQUII VA EL ITEM VIRE
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(mQuantity.getText());
                        }
                    }
                }
            });


         /* viewOptios.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if (listener!= null){
                      int position=getAdapterPosition();
                      if (position!=RecyclerView.NO_POSITION){
                          listener.onOptionClick(position);
                      }
                  }
              }
          });*/

        }
    }
}
