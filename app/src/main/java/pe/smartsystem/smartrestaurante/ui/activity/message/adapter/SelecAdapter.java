package pe.smartsystem.smartrestaurante.ui.activity.message.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pe.smartsystem.smartrestaurante.R;
import pe.smartsystem.smartrestaurante.ui.activity.message.pojo.SelectedPojo;

public class SelecAdapter  extends RecyclerView.Adapter<SelecAdapter.MyHolder>{

    private ArrayList<SelectedPojo> arrayList;
    private OnSelectedListener listener;
    private int row_index=0;

    public SelecAdapter(ArrayList<SelectedPojo> arrayList, OnSelectedListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }


    public void addDetailItem(int position,SelectedPojo pojo,String name){

        arrayList.remove(position);
        notifyItemRemoved(position);

        String pedido =pojo.getName()+","+name;
        pojo.setName(pedido);

        SelectedPojo pojo1 =new SelectedPojo();
        pojo1.setName(pedido);
        pojo1.setOrder(pojo.getOrder());


        arrayList.add(position, pojo1);
        notifyDataSetChanged();
    }

    public void cleanItem(int i,String plato){
        arrayList.remove(i);
        notifyItemRemoved(i);

        arrayList.add(i,new SelectedPojo(plato));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_selected,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder,final int position) {

        SelectedPojo pojo=arrayList.get(position);
        holder.setName(pojo.getName());
        holder.setOrder(pojo.getOrder());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelecteClick(position);
                row_index=position;
                notifyDataSetChanged();
            }
        });


        if(row_index==position)
            holder.mLayout.setBackgroundColor(Color.parseColor("#008577"));


        else
            holder.mLayout.setBackgroundColor(Color.parseColor("#ffffff"));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mName;
        private TextView mOrder;
        private LinearLayout mLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mLayout = itemView.findViewById(R.id.liner_con);
            mName=itemView.findViewById(R.id.item_name);
            mOrder=itemView.findViewById(R.id.item_order);

            itemView.setOnClickListener(this);


        }

        public void setName(String name) {
            this.mName.setText(name);
        }


        @Override
        public void onClick(View view) {
            int adapterPos = getAdapterPosition();
            listener.onSelecteClick(adapterPos);
        }

        public void setOrder(String order) {
            this.mOrder.setText(order);
        }
    }


}
