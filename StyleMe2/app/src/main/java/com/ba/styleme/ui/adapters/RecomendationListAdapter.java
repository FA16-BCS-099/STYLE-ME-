package com.ba.styleme.ui.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ba.styleme.R;
import com.ba.styleme.data.network.ApiConstant;
import com.ba.styleme.data.network.model.EventDetailModel;
import com.ba.styleme.data.network.model.RecomendationModel;
import com.ba.styleme.data.prefs.AppPreference;
import com.ba.styleme.services.broadcast_recevier.AlarmReceiver;
import com.ba.styleme.ui.activities.home.calendar.admin.RecomendationFormAdminActivity;
import com.ba.styleme.ui.activities.home.calendar.user.add_look.AddLookActivity;
import com.ba.styleme.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecomendationListAdapter extends RecyclerView.Adapter<RecomendationListAdapter.DressSuggestionViewHolder> {
    private ArrayList<RecomendationModel> recomendationModels;

    EventDetailModel eventDetailModel;
    private Context context;
    private AppPreference appPreference;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class DressSuggestionViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;

        ImageView img_product;
        TextView tv_event,tv_outfit,tv_weather;


        public DressSuggestionViewHolder(View v) {
            super(v);
            this.view = v;
            tv_event = v.findViewById(R.id.tv_event);
            tv_outfit = v.findViewById(R.id.tv_outfit);
            tv_weather = v.findViewById(R.id.tv_weather);
            img_product = v.findViewById(R.id.img_product);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecomendationListAdapter(Context context, AppPreference appPreference, ArrayList<RecomendationModel> recomendationModels, EventDetailModel eventDetailModel) {
        this.context = context;
        this.recomendationModels = recomendationModels;
        this.eventDetailModel = eventDetailModel;
        this.appPreference = appPreference;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecomendationListAdapter.DressSuggestionViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                 int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_recomendations, parent, false);

        DressSuggestionViewHolder vh = new DressSuggestionViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DressSuggestionViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Glide.with(context)
                .load(recomendationModels.get(position).getImageUrl())
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(holder.img_product);
        holder.tv_event.setText(recomendationModels.get(position).getEvent_type());
        holder.tv_outfit.setText(recomendationModels.get(position).getEvent_out_fit_type());
        holder.tv_weather.setText(recomendationModels.get(position).getWeather());

        if (eventDetailModel == null && appPreference.getEmail().equals(ApiConstant.adminEmail)) {
            holder.img_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu p = new PopupMenu(context, view, Gravity.CENTER_VERTICAL);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        p.setGravity(Gravity.RIGHT | Gravity.TOP);
                    }
                    p.getMenuInflater().inflate(R.menu.main_popup_menu, p.getMenu());
                    p.show();
                    p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.edit:
                                    editData(recomendationModels.get(position));
                                    break;
                                case R.id.delete:
                                    deleteData(position);
                                    break;
                            }
                            return true;
                        }
                    });
                }
            });
        } else {
            holder.img_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAskForSimilarLookAlertDialog(context.getString(R.string.do_you_have_similar_look_in_your_wardrobe), position);
                }
            });
        }

    }

    public void deleteData(int position) {
        final CollectionReference events = FirebaseFirestore.getInstance().collection("admin_event_detail");
        events.document(recomendationModels.get(position).getId()).delete();
        recomendationModels.remove(position);
        notifyDataSetChanged();
    }

    public void editData(RecomendationModel recomendationModel) {
        Intent intent = new Intent(context, RecomendationFormAdminActivity.class);
        intent.putExtra("recomendationModel", recomendationModel);
        context.startActivity(intent);
    }

    public void showAskForSimilarLookAlertDialog(String message, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(context.getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,
                                context.getString(R.string.wear_this_dress_for_your_event),
                                Toast.LENGTH_SHORT).show();
                        final CollectionReference events = FirebaseFirestore.getInstance().collection("evenet_detail");

                        String id = eventDetailModel.getUser_id();

                        eventDetailModel.setUser_id(appPreference.getUserId());
                        eventDetailModel.setEvent_out_fit_type(recomendationModels.get(position).getEvent_out_fit_type().trim());
                        eventDetailModel.setImageUrl(recomendationModels.get(position).getImageUrl());

                        events.document(id).set(eventDetailModel);

                        Utils.setReminder(context, AlarmReceiver.class, eventDetailModel);

                        ((Activity) context).finish();
                    }
                });
        builder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                showAddLookAlertDialog(context.getString(R.string.add_your_own_look));
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void showAddLookAlertDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(title);
        builder.setNegativeButton(context.getString(R.string.add_look),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,
                                context.getString(R.string.wear_this_dress_for_your_event),
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, AddLookActivity.class);
                        intent.putExtra("calendarListModel", eventDetailModel);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                });

        builder.setCancelable(false);
        builder.show();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return recomendationModels.size();
    }
}