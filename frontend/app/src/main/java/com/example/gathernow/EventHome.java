package com.example.gathernow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ServiceApi service;

    private ServiceApi service2;

    private List<EventData> eventDataList = new ArrayList<EventData>();

    public EventHome() {
        // Required empty public constructor
    }

    private String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_id", null); // Return null if the user_id doesn't exist
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment eventHome.
     */
    // TODO: Rename and change types and number of parameters
    public static EventHome newInstance(String param1, String param2) {
        EventHome fragment = new EventHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_home, container, false);

        LinearLayout layoutOne = rootView.findViewById(R.id.layout_one);
        LinearLayout layoutTwo = rootView.findViewById(R.id.layout_two);
        RelativeLayout loading_layout = rootView.findViewById(R.id.loading_layout);
        RelativeLayout no_events_layout = rootView.findViewById(R.id.no_event_layout);

        layoutOne.setVisibility(View.GONE);
        layoutTwo.setVisibility(View.GONE);
        no_events_layout.setVisibility(View.GONE);
        loading_layout.setVisibility(View.VISIBLE);

        service = RetrofitClient.getClient().create(ServiceApi.class);



        // TODO: check condition here, whether the user has any registered event

        Integer userId = Integer.valueOf(getUserId(getActivity()));

        service.getUserAppliedEvents(userId).enqueue(new Callback<List<ApplicationData>>(){
            @Override
            public void onResponse(Call<List<ApplicationData>> call, Response<List<ApplicationData>> response){
                if (response.isSuccessful()) {
                    // User has applied events, show using Event Card
                    List<ApplicationData> events_list = response.body();

                    if (response.body() != null && !events_list.isEmpty()){

                        Log.e("UserAppliedEventDisplay", "Event list is null.");
                        no_events_layout.setVisibility(View.GONE);

                        LinearLayout eventCardContainer = rootView.findViewById(R.id.eventCardContainer);
                        LinearLayout layoutTwoSub = layoutTwo.findViewById(R.id.layout_two_sub);
                        TextView confirmed_event = (TextView) layoutTwoSub.findViewById(R.id.confirmed_text);
                        TextView pending_event = (TextView) layoutTwoSub.findViewById(R.id.pending_text);
                        TextView no_events_text = (TextView) no_events_layout.findViewById(R.id.no_events_text);

                        List<ApplicationData> confirmed_event_list = new ArrayList<>();
                        List<ApplicationData> pending_event_list = new ArrayList<>();

                        for(int i = 0; i < events_list.size(); i++){
                            ApplicationData appliedEvent = events_list.get(i);
                            // check appliedEvent status here
                            int status = appliedEvent.request_status;
                            if(status == 0){ // pending events
                                pending_event_list.add(appliedEvent);
                            }else if(status == 1){ // confirmed events
                                confirmed_event_list.add(appliedEvent);
                            }
                            //findEventByEventId(appliedEvent.event_id, eventCardContainer, service);
                        }

                        // by default, confirmed tab is displayed
                        if(confirmed_event_list.isEmpty()){ // no confirmed event
                            loading_layout.setVisibility(View.GONE);
                            layoutOne.setVisibility(View.GONE);
                            layoutTwo.setVisibility(View.VISIBLE);
                            no_events_layout.setVisibility(View.VISIBLE);
                            no_events_text.setText("No confirmed events :(");
                        }else{
                            loading_layout.setVisibility(View.GONE);
                            layoutOne.setVisibility(View.GONE);
                            layoutTwo.setVisibility(View.VISIBLE);
                            no_events_layout.setVisibility(View.GONE);
                            for(int i = 0; i < confirmed_event_list.size(); i++) {
                                ApplicationData appliedEvent = confirmed_event_list.get(i);
                                findEventByEventId(appliedEvent.event_id, eventCardContainer, service);
                            }
                        }
                        confirmed_event.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmed_event.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                pending_event.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                                if(confirmed_event_list.isEmpty()){ // no confirmed event
                                    loading_layout.setVisibility(View.GONE);
                                    layoutOne.setVisibility(View.GONE);
                                    layoutTwo.setVisibility(View.VISIBLE);
                                    no_events_layout.setVisibility(View.VISIBLE);
                                    eventCardContainer.removeAllViews();
                                    no_events_text.setText("No confirmed events :(");
                                }else{
                                    loading_layout.setVisibility(View.GONE);
                                    layoutOne.setVisibility(View.GONE);
                                    layoutTwo.setVisibility(View.VISIBLE);
                                    no_events_layout.setVisibility(View.GONE);
                                    eventCardContainer.removeAllViews();
                                    for(int i = 0; i < confirmed_event_list.size(); i++) {
                                        ApplicationData appliedEvent = confirmed_event_list.get(i);
                                        findEventByEventId(appliedEvent.event_id, eventCardContainer, service);
                                    }
                                }
                            }
                        });

                        pending_event.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pending_event.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                confirmed_event.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                                if(pending_event_list.isEmpty()){ // no pending event
                                    loading_layout.setVisibility(View.GONE);
                                    layoutOne.setVisibility(View.GONE);
                                    layoutTwo.setVisibility(View.VISIBLE);
                                    no_events_layout.setVisibility(View.VISIBLE);
                                    eventCardContainer.removeAllViews();
                                    no_events_text.setText("No pending events :(");
                                }else{
                                    loading_layout.setVisibility(View.GONE);
                                    layoutOne.setVisibility(View.GONE);
                                    layoutTwo.setVisibility(View.VISIBLE);
                                    no_events_layout.setVisibility(View.GONE);
                                    eventCardContainer.removeAllViews();
                                    for(int i = 0; i < pending_event_list.size(); i++) {
                                        ApplicationData appliedEvent = pending_event_list.get(i);
                                        findEventByEventId(appliedEvent.event_id, eventCardContainer, service);
                                    }
                                }
                            }
                        });

                        //loading_layout.setVisibility(View.GONE);
                        //layoutOne.setVisibility(View.GONE);
                        //layoutTwo.setVisibility(View.VISIBLE);

                    }
                    else{
                        // No events applied, display sad frog
                        Log.e("UserAppliedEventDisplay", "Event list is null.");
                        loading_layout.setVisibility(View.GONE);
                        layoutOne.setVisibility(View.VISIBLE);
                        layoutTwo.setVisibility(View.GONE);
                        no_events_layout.setVisibility(View.GONE);

                        TextView discover_new = (TextView) layoutOne.findViewById(R.id.discover_new);
                        discover_new.setPaintFlags(discover_new.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        discover_new.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), FragHome.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
                else{
                    // No events applied, display sad frog

                    Log.e("UserAppliedEventDisplay", "Response not successful.");
                    loading_layout.setVisibility(View.GONE);
                    layoutOne.setVisibility(View.VISIBLE);
                    layoutTwo.setVisibility(View.GONE);

                    TextView discover_new = (TextView) layoutOne.findViewById(R.id.discover_new);
                    discover_new.setPaintFlags(discover_new.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    discover_new.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), FragHome.class);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<ApplicationData>>  call, Throwable t) {
                Log.e("UserAppliedEventDisplay", "Error occurred", t);
            }

        });


        return rootView;
    }



    public void findEventByEventId(int eventId, LinearLayout eventCardContainer, ServiceApi service2){


        service2.getEventByEventId(eventId).enqueue(new Callback<List<EventData>>() {
            @Override
            public void onResponse(Call<List<EventData>> call, Response<List<EventData>> response) {
                if (response.isSuccessful()) {
                    List<EventData> events_list = response.body();
                    EventData currentEvent = events_list.get(0); // Get the first event as the list only contains one event


                    EventCardView newEventCard = new EventCardView(getContext(), null);
                    newEventCard.setEventName(currentEvent.event_title);
                    newEventCard.setEventPhoto(currentEvent.event_type, currentEvent.event_images);
                    newEventCard.setEventCapacity(currentEvent.event_num_joined, currentEvent.event_num_participants);
                    newEventCard.setEventLocation(currentEvent.event_location);
                    newEventCard.setEventLanguage(currentEvent.event_language);
                    newEventCard.setEventDateTime(Date.valueOf(currentEvent.event_date), Time.valueOf(currentEvent.event_time));

                    // Add vertical padding to the newEventCard
                    int verticalPadding = (int) (10 * getResources().getDisplayMetrics().density); // 16dp converted to pixels
                    newEventCard.setPadding(newEventCard.getPaddingLeft(), verticalPadding, newEventCard.getPaddingRight(), verticalPadding);
                    eventCardContainer.addView(newEventCard);

                    newEventCard.setOnEventCardClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle the click event here
                            //Toast.makeText(v.getContext(), "Event card clicked!", Toast.LENGTH_SHORT).show();
                            // Send the user id to the EventInfo activity
                            Intent intent = new Intent(v.getContext(), EventInfo.class);
                            intent.putExtra("userId", getUserId(v.getContext()));
                            intent.putExtra("eventId", currentEvent.event_id);
                            startActivity(intent);
                        }
                    });


                }
            }
            @Override
            public void onFailure(Call<List<EventData>> call, Throwable t) {
                Log.d("EventInfo Testing", "Failed to get event info");
            }
        });
    }



}