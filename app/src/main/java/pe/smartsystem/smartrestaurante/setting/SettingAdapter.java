package pe.smartsystem.smartrestaurante.setting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pe.smartsystem.smartrestaurante.R;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.Myholder> {

    private ArrayList<Setting> arrayList;

    public SettingAdapter(ArrayList<Setting> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_ip,parent,false);


        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        holder.mIpTextView.setText(arrayList.get(position).getIp());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Myholder extends RecyclerView.ViewHolder{

        private TextView mIpTextView;

        public Myholder(@NonNull View itemView) {
            super(itemView);

            mIpTextView =itemView.findViewById(R.id.label_item_ip_tv);
        }
    }
}
