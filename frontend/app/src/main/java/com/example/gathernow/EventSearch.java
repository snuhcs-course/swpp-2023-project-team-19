package com.example.gathernow;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;


import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventSearch extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ServiceApi service;

    public EventSearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment eventSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static EventSearch newInstance(String param1, String param2) {
        EventSearch fragment = new EventSearch();
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
        View rootView = inflater.inflate(R.layout.fragment_event_search, container, false);

        // TODO: implement get event details from db and create event card view here

        service = RetrofitClient.getClient().create(ServiceApi.class);
        service.getALlEvents().enqueue(new Callback<List<EventData>> (){
            @Override
            public void onResponse(Call<List<EventData>> call, Response<List<EventData>>  response){
                if (response.isSuccessful()) {
                    List<EventData>  events_list = response.body();

                    //GetEventData current_event = events_list.get(0);

                    //System.out.println(current_event.event_title);

                    LinearLayout eventCardContainer = rootView.findViewById(R.id.eventCardContainer);
                    // TODO: Update UI with the list of events

                    Collections.reverse(events_list);


                    for (int i = 0; i < events_list.size(); i++){

                        EventCardView newEventCard = new EventCardView(getContext(), null);
                        EventData current_event = events_list.get(i);
                        newEventCard.setEventName(current_event.event_title);
                        newEventCard.setEventPhoto(current_event.event_type);
                        newEventCard.setEventCapacity(current_event.event_num_joined, current_event.event_num_participants);
                        newEventCard.setEventLocation(current_event.event_location);
                        newEventCard.setEventLanguage(current_event.event_language);
                        newEventCard.setEventDateTime(Date.valueOf(current_event.event_date), Time.valueOf(current_event.event_time));

                        // Add vertical padding to the newEventCard
                        int verticalPadding = (int) (10 * getResources().getDisplayMetrics().density); // 16dp converted to pixels
                        newEventCard.setPadding(newEventCard.getPaddingLeft(), verticalPadding, newEventCard.getPaddingRight(), verticalPadding);
                        eventCardContainer.addView(newEventCard);

                        newEventCard.setOnEventCardClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Handle the click event here
                                Toast.makeText(v.getContext(), "Event card clicked!", Toast.LENGTH_SHORT).show();
                                // Simulate to see if the event info page can be opened
                                Intent intent = new Intent(v.getContext(), EventInfo.class);
                                startActivity(intent);
                            }
                        });
                    }


                }
                else {
                    // Handle API error
                    // Handle the case where the response is not successful (e.g., non-2xx HTTP status)
                    Toast.makeText(getActivity(), "Event display failed.", Toast.LENGTH_SHORT).show();

                    // Logging the error
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("EventDisplay", "Failed with response: " + errorBody);
                        } catch (IOException e) {
                            Log.e("EventDisplay", "Error while reading errorBody", e);
                        }
                    } else {
                        Log.e("EventDisplay", "Response not successful and error body is null");
                    }
                }
            }
            @Override
            public void onFailure(Call<List<EventData>>  call, Throwable t) {
                Toast.makeText(getActivity(), "Get Event Error", Toast.LENGTH_SHORT).show();
                Log.e("EventDisplay", "Error occurred", t);
            }

        });

        return rootView;


        /*
        // modify the EventCardView contents:
        // to be deleted
        EventCardView eventCard = rootView.findViewById(R.id.eventCard);
        eventCard.setEventName("Defined Event");
        eventCard.setEventPhoto("Sports");
        eventCard.setEventCapacity(3,10);
        eventCard.setEventLocation("COEX, Gangnam-gu");
        eventCard.setEventLanguage("Russian");
        eventCard.setEventDateTime(new Date(1234567890123L), Time.valueOf("14:00:00"));

        LinearLayout eventCardContainer = rootView.findViewById(R.id.eventCardContainer);

        // Create a new EventCardView dynamically

        EventCardView newEventCard = new EventCardView(getContext(), null);
        newEventCard.setEventName("Dynamic Event");
        newEventCard.setEventPhoto("Leisure");
        newEventCard.setEventCapacity(5, 20);
        newEventCard.setEventLocation("Dongdaemun, Jongno-gu");
        newEventCard.setEventLanguage("Korean");
        newEventCard.setEventDateTime(new Date(1622534400000L), Time.valueOf("18:30:00"));

        // Add vertical padding to the newEventCard
        int verticalPadding = (int) (10 * getResources().getDisplayMetrics().density); // 16dp converted to pixels
        newEventCard.setPadding(newEventCard.getPaddingLeft(), verticalPadding, newEventCard.getPaddingRight(), verticalPadding);
        eventCardContainer.addView(newEventCard);

        newEventCard.setOnEventCardClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                Toast.makeText(v.getContext(), "Event card clicked!", Toast.LENGTH_SHORT).show();
            }
        });

         */


    }
}