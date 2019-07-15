package com.kcolis.android.kcolis.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kcolis.android.kcolis.R;
import com.kcolis.android.kcolis.model.Const;
import com.kcolis.android.kcolis.model.data.Annonce;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AnnoncesListAdapter extends RecyclerView.Adapter<AnnoncesListAdapter.MyViewHolder>  implements MenuItem.OnMenuItemClickListener {

    List<Annonce> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public AnnoncesListAdapter(Context context, List<Annonce> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public AnnoncesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.annonceslistadapter,parent,false);
        AnnoncesListAdapter.MyViewHolder holder = new AnnoncesListAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AnnoncesListAdapter.MyViewHolder holder, int position) {
        final Annonce current = data.get(position);
        holder.title.setText(current.getNOM_USER());
        holder.title1.setText(current.getDATE_ANNONCE());
        if(!current.getPHOTO_USER().equals("null")) {
            Uri uri = Uri.parse(Const.dns+"/colis/uploads/photo_de_profil/" + current.getPHOTO_USER());
            holder.picture.setImageURI(uri);
        }else
        {
            holder.picture.setImageResource(R.drawable.ic_profile_colorier);
        }
        holder.depart.setText(current.getVILLE_DEPART());
        holder.arrivee.setText(current.getVILLE_ARRIVEE());
        holder.Prix.setText(current.getPRIX()+" euros/Kg");
        holder.dateannonce.setText(current.getDATE_ANNONCE_VOYAGE());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView title1;
        TextView depart;
        TextView arrivee;
        TextView dateannonce;
        TextView Prix;
        SimpleDraweeView picture;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            title1 = itemView.findViewById(R.id.title1);
            depart =  itemView.findViewById(R.id.contenu);
            arrivee =  itemView.findViewById(R.id.contenu_ville_arrivee_block);
            dateannonce = itemView.findViewById(R.id.contenu_heure_depart);
            Prix = itemView.findViewById(R.id.contenu_heure_arrivee);
            picture = itemView.findViewById(R.id.my_image_view);
        }
    }


}