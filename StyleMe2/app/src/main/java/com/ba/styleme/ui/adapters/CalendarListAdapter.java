package com.ba.styleme.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ba.styleme.R;
import com.ba.styleme.data.network.model.EventDetailModel;
import com.ba.styleme.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CalendarListAdapter extends RecyclerView.Adapter<CalendarListAdapter.CalednarViewHolder> {
    private ArrayList<EventDetailModel> eventDetailModels;
    OnClickListener onClickListener;

    int currentDay=1;

    public void setCurrentDay(int currentDay){
        this.currentDay=currentDay;
    }

    private Context context;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class CalednarViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;

        ImageView img_product;
        TextView tv_title1,tv_title2,tv_event_name;
        androidx.cardview.widget.CardView cardView;


        public CalednarViewHolder(View v) {
            super(v);
            this.view = v;
            tv_title1 = v.findViewById(R.id.tv_title1);
            tv_title2 = v.findViewById(R.id.tv_title2);
            tv_event_name = v.findViewById(R.id.tv_event_name);
            img_product = v.findViewById(R.id.img_product);
            cardView = v.findViewById(R.id.cardView);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CalendarListAdapter(Context context,OnClickListener onClickListener, ArrayList<EventDetailModel> eventDetailModels) {
        this.context=context;
        this.eventDetailModels = eventDetailModels;
        this.onClickListener=onClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CalendarListAdapter.CalednarViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_calendar, parent, false);

        CalednarViewHolder vh = new CalednarViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final CalednarViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(eventDetailModels.get(position).getImageUrl().isEmpty()&& eventDetailModels.get(position).getDate().isEmpty()){
            holder.img_product.setVisibility(View.GONE);
            holder.img_product.setEnabled(false);
            holder.tv_title1.setVisibility(View.GONE);
            holder.tv_title1.setEnabled(false);
            holder.tv_title2.setVisibility(View.GONE);
            holder.tv_event_name.setVisibility(View.GONE);
            holder.tv_title2.setEnabled(false);
        }else {

            holder.tv_title1.setText(eventDetailModels.get(position).getDate());
            holder.tv_title2.setText(eventDetailModels.get(position).getDate());

            Log.e("currentDay",currentDay+"");
            if((!eventDetailModels.get(position).getImageUrl().isEmpty())){
                holder.tv_title1.setVisibility(View.VISIBLE);
                holder.tv_title2.setVisibility(View.GONE);
                holder.tv_event_name.setVisibility(View.VISIBLE);
                holder.tv_event_name.setText(eventDetailModels.get(position).getEvent_name());
               /* holder.tv_title.setBackgroundColor(context.getResources().getColor(R.color.colorThemeBlue));
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));*/
                Glide.with(context)
                        .load(eventDetailModels.get(position).getImageUrl())
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into(holder.img_product);


            }else{
                holder.tv_title1.setVisibility(View.GONE);
                holder.tv_title2.setVisibility(View.VISIBLE);
                holder.tv_event_name.setVisibility(View.GONE);

                Log.e("seconds",System.currentTimeMillis()+"\t"+eventDetailModels.get(position).getEvent_date().getSeconds());
                    if (System.currentTimeMillis() >=eventDetailModels.get(position).getEvent_date().getSeconds()*1000) {
                        holder.cardView.setEnabled(false);
                        holder.tv_title2.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                        holder.tv_title2.setAlpha((float) 0.7);
                    }

            }

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   onClickListener.onClick(eventDetailModels.get(position));

                }
            });


            if(eventDetailModels.get(position).getDate().trim().equals(currentDay+1+"")){
                holder.cardView.performClick();
                Log.e("date", eventDetailModels.get(position).getDate()+"\t"+currentDay);
            }

        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return eventDetailModels.size();
    }


    public interface OnClickListener{
        void onClick(EventDetailModel eventDetailModel);
    }
}