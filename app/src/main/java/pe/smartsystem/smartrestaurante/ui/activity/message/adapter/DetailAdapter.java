package pe.smartsystem.smartrestaurante.ui.activity.message.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pe.smartsystem.smartrestaurante.R;
import pe.smartsystem.smartrestaurante.ui.activity.message.pojo.MensajeDetailPojo;

public class DetailAdapter extends  RecyclerView.Adapter<DetailAdapter.MyHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    private ArrayList<MensajeDetailPojo> mensajeDetailPojos;


    public DetailAdapter(ArrayList<MensajeDetailPojo> mensajeDetailPojos) {
        this.mensajeDetailPojos = mensajeDetailPojos;
    }

    public void setOnClickListener(View.OnClickListener listener1){
        this.listener=listener1;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_button,viewGroup,false);
        view.setOnClickListener(this);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
        holder.mDetailButton.setText(mensajeDetailPojos.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return mensajeDetailPojos.size();
    }

    @Override
    public void onClick(View view) {
        if (listener!= null){
            listener.onClick(view);
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private TextView mDetailButton;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            this.mDetailButton  = itemView.findViewById(R.id.label_item_button_cat);
        }
    }
}
