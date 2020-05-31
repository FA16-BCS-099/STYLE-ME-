package com.ba.styleme.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ba.styleme.R;
import com.ba.styleme.data.network.model.CommunityListModel;
import com.ba.styleme.data.network.model.RecomendationModel;
import com.ba.styleme.ui.activities.home.calendar.admin.RecomendationFormAdminActivity;
import com.ba.styleme.ui.activities.home.community.community_detail.UpdateProduct;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;

public class CommunityListAdapter extends RecyclerView.Adapter<CommunityListAdapter.CommunityViewHolder> {
    private List<CommunityListModel> communityListModels;
    private boolean show_my_prod;

    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class CommunityViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;

        ImageView img_product;
        TextView tv_name, tv_texture, tv_worth, tv_exchange, tv_contact, tv_availablity, tv_location;


        public CommunityViewHolder(View v) {
            super(v);
            this.view = v;
            tv_name = v.findViewById(R.id.tv_name);
            tv_texture = v.findViewById(R.id.tv_texture);
            tv_worth = v.findViewById(R.id.tv_worth);
            tv_exchange = v.findViewById(R.id.tv_exchange);
            tv_contact = v.findViewById(R.id.tv_contact);
            tv_availablity = v.findViewById(R.id.tv_availablity);
            img_product = v.findViewById(R.id.img_product);
            tv_location = v.findViewById(R.id.tv_location);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommunityListAdapter(Context context, List<CommunityListModel> communityListModels) {
        this.context = context;
        this.communityListModels = communityListModels;
    }

    public CommunityListAdapter(Context context, List<CommunityListModel> communityListModels, boolean show_my_prod) {
        this.context = context;
        this.communityListModels = communityListModels;
        this.show_my_prod = show_my_prod;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CommunityListAdapter.CommunityViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_community, parent, false);

        CommunityViewHolder vh = new CommunityViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final CommunityViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Glide.with(context)
                .load(communityListModels.get(position).getImageUrl())
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(holder.img_product);
        holder.tv_name.setText(communityListModels.get(position).getPro_name());
        holder.tv_texture.setText(communityListModels.get(position).getPro_texture());
        holder.tv_worth.setText(communityListModels.get(position).getPro_worth());
        holder.tv_exchange.setText(communityListModels.get(position).getPro_exchange_for());
        holder.tv_location.setText(communityListModels.get(position).getLocation());

        if (communityListModels.get(position).isIs_available()) {
            holder.tv_availablity.setTextColor(context.getResources().getColor(R.color.available));
            holder.tv_availablity.setText(String.valueOf((char) 0xf111) + "\t" + context.getString(R.string.available));
        } else {
            holder.tv_availablity.setTextColor(context.getResources().getColor(R.color.not_available));
            holder.tv_availablity.setText(String.valueOf((char) 0xf111) + "\t" + context.getString(R.string.not_available));
        }
        holder.tv_exchange.setText(communityListModels.get(position).getPro_exchange_for());
        holder.tv_contact.setText("+92" + communityListModels.get(position).getContact_no().substring(1));

        holder.tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeoPoint current_loc = getLocationFromAddress(holder.tv_location.getText().toString());
                String latitude = Double.toString(current_loc.getLatitude());
                String longitude = Double.toString(current_loc.getLongitude());
                Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?z=24");

                Intent mapIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("geo:0,0?q="+holder.tv_location.getText().toString()));

                mapIntent.setPackage("com.google.android.apps.maps");


                context.startActivity(mapIntent);






            }
        });


        holder.img_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent=new Intent(context, CommunityDetailActivity.class);
                intent.putExtra("communityListModel",communityListModels.get(position));
                context.startActivity(intent);*/
                if (show_my_prod) {
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
                                case R.id.edit: {
                                    editData(communityListModels.get(position));
                                }
                                break;
                                case R.id.delete:
                                    deleteData(position);
                                    break;
                            }
                            return true;

                        }
                    });
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return communityListModels.size();
    }

    public void editData(CommunityListModel communityListModel) {
        Intent intent = new Intent(context, UpdateProduct.class);
        intent.putExtra("communityListModel", communityListModel);
        context.startActivity(intent);
    }

    public void deleteData(int position) {
        final CollectionReference events = FirebaseFirestore.getInstance().collection("add_for_exchange");
        Query query = events.whereEqualTo("imageUrl", communityListModels.get(position).getImageUrl());

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    events.document(documentSnapshot.getId()).delete();
                }
            }
        });

//       events.document(communityListModels.get(position).getUser_id()).delete();
        communityListModels.remove(position);
        notifyDataSetChanged();
    }

    public GeoPoint getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint((double) (location.getLatitude() ),
                    (double) (location.getLongitude() ));

            return p1;
        } catch (IOException e) {

            e.printStackTrace();
            return(null);
        }
    }


}