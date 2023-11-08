package com.example.gathernow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.media.Image;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ExecutorService executorService;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ServiceApi service;
    private ServiceApi service2;

    private boolean isEventLoaded = false;
    private boolean isUserLoaded = false;

    //private ProgressBar progressBar;

    // Function to get user id
    private String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_id", null); // Return null if the user_id doesn't exist
    }

    public ProfileHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileHome.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileHome newInstance(String param1, String param2) {
        ProfileHome fragment = new ProfileHome();
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
        View rootView = inflater.inflate(R.layout.fragment_profile_home, container, false);

        LinearLayout layoutOne = rootView.findViewById(R.id.layout_one);
        LinearLayout layoutTwo = rootView.findViewById(R.id.layout_two);
        RelativeLayout user_info = rootView.findViewById(R.id.user_info);

        layoutOne.setVisibility(View.GONE);
        layoutTwo.setVisibility(View.GONE);
        user_info.setVisibility(View.GONE);
        RelativeLayout loading_layout = rootView.findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.VISIBLE);

        TextView profile_text = rootView.findViewById(R.id.subtitle_text);
        //ImageView profile_img = rootView.findViewById(R.id.profile_image);


        Integer userId = Integer.valueOf(getUserId(getActivity()));

        service = RetrofitClient.getClient().create(ServiceApi.class);

        service.getUserInfo(userId).enqueue(new Callback<UserData>(){
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()) {
                    UserData current_userinfo = response.body();
                    String user_name = current_userinfo.name;
                    profile_text.setText(user_name + ", welcome back!");

                    // TODO: Call profile picture
                    // Get the source of image on the server by calling API http://20.2.88.70:8000/api/useravatar/{user_id}/
                    // Set the image source to the image view
                    service2 = RetrofitClient.getClient().create(ServiceApi.class);
                    service2.getUserInfo(userId).enqueue(new Callback<UserData>() {
                        @Override
                        public void onResponse(Call<UserData> call, Response<UserData> response) {
                            // Log response to console
                            Log.d("UserAvatar", "Response: " + response.toString());
                            if (response.isSuccessful()) {
                                UserData current_useravatar = response.body();
                                String user_avatar = current_useravatar.avatar;
                                user_avatar = "http://20.2.88.70:8000" + user_avatar;
                                ImageView profile_img = rootView.findViewById(R.id.profile_image);
                                Picasso.get().load(user_avatar).into(profile_img);

                                isUserLoaded = true;
                            }
                        }



                        public void onFailure(Call<UserData> call, Throwable t) {
                            Toast.makeText(getActivity(), "Get User Avatar Error", Toast.LENGTH_SHORT).show();
                            Log.e("UserAvatar", "Error occurred", t);
                        }
                    });

                } else {
                    // Handle API error
                    // Handle the case where the response is not successful (e.g., non-2xx HTTP status)
                    Toast.makeText(getActivity(), "Event display failed.", Toast.LENGTH_SHORT).show();

                    // Logging the error
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("UserEventDisplay", "Failed with response: " + errorBody);
                        } catch (IOException e) {
                            Log.e("UserEventDisplay", "Error while reading errorBody", e);
                        }
                    } else {
                        Log.e("UserEventDisplay", "Response not successful and error body is null");
                    }
                }
            }
            @Override
            public void onFailure(Call<UserData>  call, Throwable t) {
                Toast.makeText(getActivity(), "Get Event Error", Toast.LENGTH_SHORT).show();
                Log.e("UserEventDisplay", "Error occurred", t);
            }
        });


        service.getEventsByUser(userId).enqueue(new Callback<List<EventData>>(){
            @Override
            public void onResponse(Call<List<EventData>> call, Response<List<EventData>> response){
                if (response.isSuccessful()) {
                    List<EventData>  events_list = response.body();
                    if (response.body() != null){
                        if(events_list.size() >0){

                            isEventLoaded = true;
                            loading_layout.setVisibility(View.GONE);
                            user_info.setVisibility(View.VISIBLE);
                            layoutOne.setVisibility(View.GONE);
                            layoutTwo.setVisibility(View.VISIBLE);

                            LinearLayout eventCardContainer = rootView.findViewById(R.id.eventCardContainer);

                            Collections.reverse(events_list);

                            // Get the current date and time
                            Date currentDate = new Date(System.currentTimeMillis());

                            for (int i = 0; i < events_list.size(); i++){

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

                                    // Add vertical padding to the newEventCard
                                    int verticalPadding = (int) (10 * getResources().getDisplayMetrics().density); // 16dp converted to pixels
                                    newEventCard.setPadding(newEventCard.getPaddingLeft(), verticalPadding, newEventCard.getPaddingRight(), verticalPadding);
                                    eventCardContainer.addView(newEventCard);

                                    newEventCard.setOnEventCardClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // Handle the click event here
                                            Toast.makeText(v.getContext(), "Event card clicked!", Toast.LENGTH_SHORT).show();
                                            // Send the user id to the EventInfo activity
                                            Intent intent = new Intent(v.getContext(), EventInfo.class);
                                            intent.putExtra("userId", getUserId(v.getContext()));
                                            intent.putExtra("eventId", currentEvent.event_id);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }

                        }
                        else{
                            //status = 0;
                            loading_layout.setVisibility(View.GONE);
                            user_info.setVisibility(View.VISIBLE);
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
                    else{
                        //status = 0;
                        loading_layout.setVisibility(View.GONE);
                        user_info.setVisibility(View.VISIBLE);
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
                else {
                    // Handle API error
                    // Handle the case where the response is not successful (e.g., non-2xx HTTP status)
                    Toast.makeText(getActivity(), "Event display failed.", Toast.LENGTH_SHORT).show();

                    // Logging the error
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("UserEventDisplay", "Failed with response: " + errorBody);
                        } catch (IOException e) {
                            Log.e("UserEventDisplay", "Error while reading errorBody", e);
                        }
                    } else {
                        Log.e("UserEventDisplay", "Response not successful and error body is null");
                    }
                }
            }
            @Override
            public void onFailure(Call<List<EventData>>  call, Throwable t) {
                Toast.makeText(getActivity(), "Get Event Error", Toast.LENGTH_SHORT).show();
                Log.e("UserEventDisplay", "Error occurred", t);
            }

        });




        return rootView;


    }
}