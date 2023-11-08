package com.example.gathernow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    // Get current user id (for intent)
    private String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_id", null); // Return null if the user_id doesn't exist
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_search, container, false);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        service.getALlEvents().enqueue(new Callback<List<EventData>>() {
            @Override
            public void onResponse(Call<List<EventData>> call, Response<List<EventData>> response) {
                if (response.isSuccessful()) {
                    List<EventData> events_list = response.body();
                    LinearLayout eventCardContainer = rootView.findViewById(R.id.eventCardContainer);

                    Collections.reverse(events_list);

                    // Get the current date and time
                    Date currentDate = new Date(System.currentTimeMillis());

                    for (int i = 0; i < events_list.size(); i++) {

                        EventData currentEvent = events_list.get(i);
                        // Create Date and Time object from currentEvent.event_date
                        Date eventDate = Date.valueOf(currentEvent.event_date);
                        Time eventTime = Time.valueOf(currentEvent.event_time);

                        // Combine the Date and Time into a single Date object
                        long dateTimeMillis = eventDate.getTime() + eventTime.getTime();
                        Date eventDateTime = new Date(dateTimeMillis);

                        //Only display events that happens after the current datetime
                        if (eventDateTime.after(currentDate)){
                            EventCardView newEventCard = new EventCardView(getContext(), null);

                            newEventCard.setEventName(currentEvent.event_title);
                            newEventCard.setEventPhoto(currentEvent.event_type, currentEvent.event_images);
                            newEventCard.setEventCapacity(currentEvent.event_num_joined, currentEvent.event_num_participants);
                            newEventCard.setEventLocation(currentEvent.event_location);
                            newEventCard.setEventLanguage(currentEvent.event_language);
                            newEventCard.setEventDateTime(Date.valueOf(currentEvent.event_date), Time.valueOf(currentEvent.event_time));

//                        Log.d("EventInfo Testing", "Clicked event id: " + events_list.get(i).event_id.toString());

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


                } else {
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
            public void onFailure(Call<List<EventData>> call, Throwable t) {
                Toast.makeText(getActivity(), "Get Event Error", Toast.LENGTH_SHORT).show();
                Log.e("EventDisplay", "Error occurred", t);
            }

        });

        return rootView;

    }
}