package com.luneraremoterecieve;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luneraremoterecieve.atbeaconview.AltBeacon;

import java.util.ArrayList;

/**
 * Created by shvet on 03/08/2017,LuneraRemoteRecieve
 */

public class ReceiverAdapter extends RecyclerView.Adapter<ReceiverAdapter.ViewHolder> {

    Context context;
    ArrayList<Model> arrayList;

    public ReceiverAdapter(Context context, ArrayList<Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_child, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title1.setText(arrayList.get(position).getTitle());
        holder.subtitle.setText(arrayList.get(position).getSubtitle());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title1, subtitle;

        public ViewHolder(View itemView) {
            super(itemView);
            title1 = (TextView) itemView.findViewById(R.id.title1);
            subtitle = (TextView) itemView.findViewById(R.id.sub_title);
        }
    }
}
