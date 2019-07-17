package com.kcolis.android.kcolis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.kcolis.android.kcolis.R;
import com.kcolis.android.kcolis.model.Database.SessionManager;
import com.kcolis.android.kcolis.model.dao.DatabaseHandler;
import com.kcolis.android.kcolis.model.data.profil_adapter_item;
import java.util.Collections;
import java.util.List;

public class ProfilAdapter extends RecyclerView.Adapter<ProfilAdapter.MyViewHolder> {

    List<profil_adapter_item> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private DatabaseHandler database;
    private SessionManager session;

    public ProfilAdapter(Context context,List<profil_adapter_item> data)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ProfilAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.profil_adapter,parent,false);
        ProfilAdapter.MyViewHolder holder = new ProfilAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProfilAdapter.MyViewHolder holder, int position) {
        profil_adapter_item current = data.get(position);
        /*holder.title.setText(current.getLibelle());
        holder.icon.setImageResource(current.getID());*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        /*TextView title;
        ImageView icon;*/

        public MyViewHolder(View itemView)
        {
            super(itemView);
            /*title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);*/
        }
    }

    public void delete(int position)
    {
        data.remove(position);
        notifyItemRemoved(position);
    }
}
