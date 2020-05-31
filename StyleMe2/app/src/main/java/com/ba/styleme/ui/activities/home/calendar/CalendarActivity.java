package com.ba.styleme.ui.activities.home.calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ba.styleme.R;
import com.ba.styleme.data.network.model.CommunityListModel;
import com.ba.styleme.ui.activities.home.calendar.user.EventSubmissionActivity;
import com.ba.styleme.ui.activities.home.community.community_detail.UpdateProduct;
import com.ba.styleme.ui.adapters.CalendarListAdapter;
import com.ba.styleme.data.network.model.EventDetailModel;
import com.ba.styleme.ui.activities.base.BaseActivity;
import com.ba.styleme.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends BaseActivity implements CalendarListAdapter.OnClickListener {
    RecyclerView rv_list;
    ImageView img_product;
    TextView tv_title1, tv_title2, tv_event_name;

    ArrayList<EventDetailModel> eventDetailModels = new ArrayList<>();
    CalendarListAdapter calednarListAdapter;

    Calendar calendar = Calendar.getInstance();
    int mYear, mMonth, mDay;

    TextView tv_month_year;

    ImageView img_calendar_next, img_calendar_back;
    CardView cardView;
    Timestamp start = null;
    Timestamp end = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        setTitle(getResourceString(R.string.calendar));
        setSlidingPane();
        rv_list = findViewById(R.id.rv_list);
        tv_month_year = findViewById(R.id.tv_month_year);
        img_calendar_next = findViewById(R.id.img_calendar_next);
        img_calendar_back = findViewById(R.id.img_calendar_back);

        tv_title1 = findViewById(R.id.tv_title1);
        tv_title2 = findViewById(R.id.tv_title2);
        tv_event_name = findViewById(R.id.tv_event_name);
        img_product = findViewById(R.id.img_product);
        cardView = findViewById(R.id.cardView);

        calednarListAdapter = new CalendarListAdapter(this, this, eventDetailModels);
        rv_list.setAdapter(calednarListAdapter);

        calendar.setTimeInMillis(System.currentTimeMillis());


        calednarListAdapter.setCurrentDay(calendar.get(Calendar.DAY_OF_MONTH));
        setCalendar();

        calendar.setTimeInMillis(System.currentTimeMillis());

        tv_month_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog();
            }
        });

        img_calendar_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, 1);
                setCalendar();
            }
        });
        img_calendar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, -1);
                setCalendar();
            }
        });
    }

    public void setCalendar() {
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);


        setCalendarData(mYear, mMonth);

        updateCalendarList(mYear, mMonth, (calendar.get(Calendar.DAY_OF_WEEK) - 2));

    }


    public void updateCalendarList(int year, int month, int dayOfMonth) {

        numberOfMonth(year, month);

        int firstDayOfWeek = dayOfMonth;
        if (firstDayOfWeek == -1)
            firstDayOfWeek = 6;
        int daycount = 0;
        eventDetailModels.clear();
        for (int j = 1; j <= getTotalFreeDays(firstDayOfWeek); j++) {
            eventDetailModels.add(new EventDetailModel("", "", "", "", "", new Timestamp(new Date(System.currentTimeMillis())), "", ""));
        }

        for (int i = 1; i <= daysInMonth; i++) {
            String url = "";

           /* if(i==10||i==20){
                url="http://pngimg.com/uploads/dress/dress_PNG180.png";
            }*/
            int day = i + daycount;
            String date = day + "/" + month + "/" + year;
            if (i == 1) {
                start = new Timestamp(new Date(Utils.getTimeInMillisecond(date)));
            } else if (i == daysInMonth) {
                end = new Timestamp(new Date(Utils.getTimeInMillisecond(date)));

            }


            eventDetailModels.add(new EventDetailModel(appPreference.getUserId(), url, "event name", "event description", day + "", new Timestamp(new Date(Utils.getTimeInMillisecond(date))), "event_type", "event_outfit_type"));
            Log.e("date", new Timestamp(new Date(Utils.getTimeInMillisecond(date))).toString());

        }

        getData();

        calednarListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    public void getData() {
        showLoading();
        final CollectionReference events = db.collection("evenet_detail");
        Log.e("user_id", appPreference.getUserId());
        Log.e("event_date", Utils.getDate(start));
        Log.e("event_date", Utils.getDate(end));
        events.whereEqualTo("user_id", appPreference.getUserId()).whereGreaterThanOrEqualTo("event_date", start).whereLessThanOrEqualTo("event_date", end).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    hideLoading();
                    Log.e("message", "onSuccess: LIST EMPTY" + documentSnapshots.toString());
                    return;
                } else {
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        if (documentSnapshot.exists()) {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            List<EventDetailModel> eventDetailModel = documentSnapshots.toObjects(EventDetailModel.class);

                            for (EventDetailModel eventDetailModel1 : eventDetailModels) {
                                for (EventDetailModel eventDetail : eventDetailModel) {
                                    //     Log.e("message", "onSuccess: DOCUMENT" + eventDetail.getEvent_date());

                                    if (eventDetailModel1.getEvent_date().equals(eventDetail.getEvent_date())) {
                                        //eventDetailModel1=eventDetail;
                                        eventDetailModel1.setEvent_name(eventDetail.getEvent_name());
                                        eventDetailModel1.setEvent_description(eventDetail.getEvent_description());
                                        eventDetailModel1.setEvent_out_fit_type(eventDetail.getEvent_out_fit_type());
                                        eventDetailModel1.setEvent_type(eventDetail.getEvent_type());
                                        if(eventDetail.getImageUrl().isEmpty()) {
                                            eventDetailModel1.setImageUrl("http://pngimg.com/uploads/dress");
                                        }else{
                                        eventDetailModel1.setImageUrl(eventDetail.getImageUrl());
                                         }
                                    }
                                }
                            }
                            calednarListAdapter.notifyDataSetChanged();
                            hideLoading();

                        }
                        break;
                    }
                }
            }
        });
    }

    String MonthOfName = "";
    int daysInMonth = 0;

    public void numberOfMonth(int year, int month) {

        switch (month) {
            case 1:
                MonthOfName = "January";
                daysInMonth = 31;
                break;
            case 2:
                MonthOfName = "February";
                if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
                    daysInMonth = 29;
                } else {
                    daysInMonth = 28;
                }
                break;
            case 3:
                MonthOfName = "March";
                daysInMonth = 31;
                break;
            case 4:
                MonthOfName = "April";
                daysInMonth = 30;
                break;
            case 5:
                MonthOfName = "May";
                daysInMonth = 31;
                break;
            case 6:
                MonthOfName = "June";
                daysInMonth = 30;
                break;
            case 7:
                MonthOfName = "July";
                daysInMonth = 31;
                break;
            case 8:
                MonthOfName = "August";
                daysInMonth = 31;
                break;
            case 9:
                MonthOfName = "September";
                daysInMonth = 30;
                break;
            case 10:
                MonthOfName = "October";
                daysInMonth = 31;
                break;
            case 11:
                MonthOfName = "November";
                daysInMonth = 30;
                break;
            case 12:
                MonthOfName = "December";
                daysInMonth = 31;
        }
        tv_month_year.setText(MonthOfName + " " + calendar.get(Calendar.YEAR));
    }

    public int getTotalFreeDays(int finaldayOfMonth) {

        Log.e("Saturday", Calendar.SATURDAY + "");
        Log.e("Sunday", Calendar.SUNDAY + "");
        Log.e("Monday", Calendar.MONDAY + "");
        Log.e("Tuesday", Calendar.TUESDAY + "");
        Log.e("Wedneday", Calendar.WEDNESDAY + "");
        Log.e("Thursday", Calendar.THURSDAY + "");
        Log.e("Firday", Calendar.FRIDAY + "");
        switch (finaldayOfMonth) {
            case Calendar.THURSDAY:
                //descriptionDay= "Thursday";
                //break;
                return 5;

            case Calendar.FRIDAY:
               /* descriptionDay = "Friday";
                break;*/
                return 6;
            case Calendar.SATURDAY:
               /* descriptionDay = "Saturday";
                break;*/
                return 7;
            case Calendar.SUNDAY:
               /* descriptionDay = "Sunday";
                break;*/
                return 1;
            case Calendar.MONDAY:
                // descriptionDay= "Monday";
                return 2;

            case Calendar.TUESDAY:
                // descriptionDay= "Tuesday";
                return 3;

            case Calendar.WEDNESDAY:
                // descriptionDay = "Wednesday";
                return 4;


        }
        return 0;
    }

    public void openDatePickerDialog() {
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        // date picker dialog
        DatePickerDialog picker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int finalyear, int finalmonthOfYear, int finaldayOfMonth) {

                        calednarListAdapter.setCurrentDay(finaldayOfMonth - 1);
                        setCalendarData(finalyear, finalmonthOfYear);

                        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;

                        updateCalendarList(mYear, mMonth, firstDayOfWeek);
                        calendar.set(finalyear, finalmonthOfYear, finaldayOfMonth);
                        Log.e("firstDayOfWeek", firstDayOfWeek + "");

                    }
                }, mYear, mMonth, mDay);
        Date d = new Date(new Date().getTime() + 86400000);
        picker.getDatePicker().setMinDate(d.getTime());
        picker.show();

    }

    public void setCalendarData(int finalyear, int finalmonthOfYear) {
        calendar.set(finalyear, finalmonthOfYear, 1);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    public void onClick(final EventDetailModel eventDetailModel) {

        tv_title1.setText(eventDetailModel.getDate());
        tv_title2.setText(eventDetailModel.getDate() + " " + tv_month_year.getText().toString());

        if ((!eventDetailModel.getImageUrl().isEmpty())) {
            tv_title1.setVisibility(View.VISIBLE);
            tv_title2.setVisibility(View.GONE);
            tv_event_name.setVisibility(View.VISIBLE);
            tv_event_name.setText(eventDetailModel.getEvent_name() + "\n" + eventDetailModel.getEvent_description());
               /* tv_title.setBackgroundColor(context.getResources().getColor(R.color.colorThemeBlue));
                cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));*/
            Glide.with(this)
                    .load(eventDetailModel.getImageUrl())
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(img_product);
            img_product.setVisibility(View.VISIBLE);
            cardView.setEnabled(true);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu p = new PopupMenu(context, tv_event_name, Gravity.CENTER);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        p.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                    }
                    p.getMenuInflater().inflate(R.menu.main_popup_menu, p.getMenu());
                    p.show();
                    p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.edit: {
                                    editData(eventDetailModel);

                                }
                                break;
                                case R.id.delete:


                                    final CollectionReference events = FirebaseFirestore.getInstance().collection("evenet_detail");
                                    Query query = events.whereEqualTo("date", eventDetailModel.getDate());




                                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                events.document(documentSnapshot.getId()).delete();
                                            }
//                                            finish();
//                                            startActivity(getIntent());
                                            recreate();




                                        }
                                    });
                                    getData();
                                    calednarListAdapter.notifyDataSetChanged();

                                    break;

                            }
                            return true;

                        }
                    });
                }
            });

        } else {
            tv_title1.setVisibility(View.GONE);
            tv_title2.setVisibility(View.VISIBLE);
            tv_event_name.setVisibility(View.GONE);
            img_product.setVisibility(View.GONE);
            cardView.setEnabled(true);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CalendarActivity.this, EventSubmissionActivity.class);
                    intent.putExtra("calendarListModel", eventDetailModel);
                    intent.putExtra("action","add");
                    startActivity(intent);
                }
            });
        }
    }
    public void editData(EventDetailModel eventDetailModel) {
        Intent intent = new Intent(context, EventSubmissionActivity.class);
        intent.putExtra("eventDetailModel", eventDetailModel);
        intent.putExtra("action","update");
        context.startActivity(intent);
    }


}
