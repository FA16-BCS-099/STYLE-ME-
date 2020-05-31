package com.ba.styleme.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ba.styleme.R;
import com.ba.styleme.data.network.model.EventDetailModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class WardrobeListAdapter extends RecyclerView.Adapter<WardrobeListAdapter.WardrobeViewHolder> {
    private List<EventDetailModel> eventDetailModels;

    private Context context;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class WardrobeViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;

        ImageView img_product,img_weather;
        TextView tv_event,tv_outfit,tv_weather;




        public WardrobeViewHolder(View v) {
            super(v);
            this.view = v;
            tv_event = v.findViewById(R.id.tv_event);
            tv_outfit = v.findViewById(R.id.tv_outfit);
            tv_weather = v.findViewById(R.id.tv_weather);
            img_product = v.findViewById(R.id.img_product);
            img_weather = v.findViewById(R.id.img_weather);
            img_weather.setVisibility(View.GONE);
            tv_weather.setVisibility(View.GONE);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public WardrobeListAdapter(Context context,List<EventDetailModel> eventDetailModels) {
        this.context=context;
        this.eventDetailModels = eventDetailModels;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WardrobeListAdapter.WardrobeViewHolder onCreateViewHolder(ViewGroup parent,
                                                                            int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_recomendations, parent, false);

        WardrobeViewHolder vh = new WardrobeViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(WardrobeViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Glide.with(context)
                .load(eventDetailModels.get(position).getImageUrl())
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(holder.img_product);
        holder.tv_event.setText(eventDetailModels.get(position).getEvent_name());
        holder.tv_outfit.setText(eventDetailModels.get(position).getEvent_out_fit_type());
        holder.img_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)context).finish();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return eventDetailModels.size();
    }
}