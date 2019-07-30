package pe.smartsystem.smartrestaurante.ui.fragment.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pe.smartsystem.smartrestaurante.R;


public class CategoraPlatosAdapter extends RecyclerView.Adapter<CategoraPlatosAdapter.Myholder> {

   private ArrayList<CategoriaPlato> arrayList;
    private OnCategoryPlatosListener listener;


    public CategoraPlatosAdapter(ArrayList<CategoriaPlato> arrayList, OnCategoryPlatosListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_category_platos,parent,false);

       return new Myholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        CategoriaPlato plato=arrayList.get(position);
        holder.mNameTextView.setText(arrayList.get(position).getName());
        holder.bind(plato);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Myholder extends RecyclerView.ViewHolder{
        private TextView mNameTextView;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            mNameTextView=itemView.findViewById(R.id.label_item_categori_aplatos_tv);


        }


        public void bind(final CategoriaPlato plato) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onClickCategotyPlatosListener(plato,"categoria");
                }
            });
        }
    }
}
