package com.ba.styleme.ui.activities.home.community.community_detail;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ba.styleme.R;
import com.ba.styleme.data.network.model.CommunityListModel;
import com.ba.styleme.ui.activities.base.BaseActivity;

public class CommunityDetailActivity extends BaseActivity {
    ImageView img_product;
    EditText pro_name,pro_texture,pro_worth,pro_exchange_for,contact_no;
    RadioGroup rg_status;
    RadioButton rb_available,rb_not_available;

    CommunityListModel communityListModel=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);
        setTitle(getResourceString(R.string.community));

        findViews();

        if(getIntent()!=null){
            communityListModel=(CommunityListModel) getIntent().getSerializableExtra("communityListModel");
            setData();
        }
    }

    public void findViews(){
        img_product=findViewById(R.id.img_product);
        pro_name=findViewById(R.id.pro_name);
        pro_texture=findViewById(R.id.pro_texture);
        pro_worth=findViewById(R.id.pro_worth);
        pro_exchange_for=findViewById(R.id.pro_exchange_for);
        contact_no=findViewById(R.id.contact_no);
        rg_status=findViewById(R.id.rg_status);
        rb_available=findViewById(R.id.rb_available);
        rb_not_available=findViewById(R.id.rb_not_available);
    }

    public void setData(){
        if(communityListModel!=null){
            loadImage(this,communityListModel.getImageUrl(),img_product);
            pro_name.setText(communityListModel.getPro_name());
            pro_texture.setText(communityListModel.getPro_texture());
            pro_worth.setText(communityListModel.getPro_worth());
            pro_exchange_for.setText(communityListModel.getPro_exchange_for());
            contact_no.setText(communityListModel.getContact_no());
            if(communityListModel.isIs_available()){
                rb_available.setChecked(true);
            }else{
                rb_not_available.setChecked(true);
            }
        }
    }
}
