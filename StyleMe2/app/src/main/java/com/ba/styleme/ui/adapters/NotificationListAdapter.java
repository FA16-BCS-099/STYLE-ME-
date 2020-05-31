package com.ba.styleme.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ba.styleme.R;
import com.ba.styleme.data.network.model.NotificationListModel;

import java.util.ArrayList;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationViewHolder> {
    private ArrayList<NotificationListModel> notificationListModels;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;

        TextView tv_event, tv_date, tv_dress;

        public NotificationViewHolder(View v) {
            super(v);
            this.view = v;

            tv_event = v.findViewById(R.id.tv_event);
            tv_date = v.findViewById(R.id.tv_date);
            tv_dress = v.findViewById(R.id.tv_dress);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NotificationListAdapter(ArrayList<NotificationListModel> notificationListModels) {
        this.notificationListModels = notificationListModels;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotificationListAdapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent,
                                                                             int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_notification, parent, false);

        NotificationViewHolder vh = new NotificationViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tv_event.setText("EVENT NAME :   " + notificationListModels.get(position).getEvent());
        holder.tv_date.setText("DATE  :   " + notificationListModels.get(position).getDate());
        holder.tv_dress.setText("EVENT DESCRIPTION :   " + notificationListModels.get(position).getDress());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return notificationListModels.size();
    }
}