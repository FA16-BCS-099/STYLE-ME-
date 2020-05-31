package com.ba.styleme.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ba.styleme.R;
import com.ba.styleme.data.network.model.RecomendationModel;
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    ArrayList<RecomendationModel> recomendationModels;

    public SliderAdapterExample(Context context, ArrayList<RecomendationModel> recomendationModels) {
        this.context = context;
        this.recomendationModels = recomendationModels;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        // viewHolder.textViewDescription.setText("This is slider item " + position);

        Glide.with(context)
                .load(recomendationModels.get(position).getImageUrl())
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(viewHolder.imageViewBackground);


    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return recomendationModels.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        // TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            //  textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}