package com.example.gathernow.main_ui.events;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gathernow.FragHome;
import com.example.gathernow.R;
import com.example.gathernow.api.RetrofitClient;
import com.example.gathernow.api.ServiceApi;
import com.example.gathernow.api.models.ApplicationDataModel;
import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.authenticate.UserLocalDataSource;
import com.example.gathernow.main_ui.profile.ProfileViewModel;
import com.example.gathernow.utils.EventCardHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsActivity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ServiceApi service;

    private LinearLayout layoutOne, layoutTwo, layoutTwoSub, eventCardContainer;
    private RelativeLayout loadingLayout, noEventsLayout;
    private TextView confirmedEventsText, pendingEventsText, noEventsText;
    private ImageView sadFrog;
    private int userId;
    private EventsViewModel eventsViewModel;
    private UserLocalDataSource userLocalDataSource;

    private List<EventDataModel> eventDataModelList = new ArrayList<EventDataModel>();

    public EventsActivity() {
        // Required empty public constructor
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
    public static EventsActivity newInstance(String param1, String param2) {
        EventsActivity fragment = new EventsActivity();
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

        eventsViewModel = new EventsViewModel(getContext());
        userLocalDataSource = new UserLocalDataSource(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        //get current login user id
        UserLocalDataSource userLocalDataSource = new UserLocalDataSource(getActivity());
        if (userLocalDataSource.getUserId() == null) {
            Log.e("EventsActivity", "User not logged in");
            return rootView;
        }
        userId = Integer.valueOf(userLocalDataSource.getUserId());

        initializeUI(rootView);
        Log.e("EventsActivity", "Initialized UI");

        eventsViewModel.getAlertMessage().observe(getViewLifecycleOwner(), message -> Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show());
        eventsViewModel.getAllEvents().observe(getViewLifecycleOwner(), applicationDataModels -> fetchAllEventsUI(applicationDataModels, rootView));
        Log.e("EventsActivity", "getAllEvents() called");

        eventsViewModel.getConfirmedEvents().observe(getViewLifecycleOwner(), eventDataModels -> fetchConfirmedEventsUI(eventDataModels, rootView));
        Log.e("EventsActivity", "getConfirmedEvents() called");

        eventsViewModel.getPendingEvents().observe(getViewLifecycleOwner(), eventDataModels -> fetchPendingEventsUI(eventDataModels, rootView));
        Log.e("EventsActivity", "getPendingEvents() called");

        eventsViewModel.fetchUserAppliedEvents(userLocalDataSource);
        Log.e("EventsActivity", "fetchUserAppliedEvents() called");

        return rootView;
    }

    private void fetchAllEventsUI(List<ApplicationDataModel>applicationDataModels, View rootView) {
        if (applicationDataModels == null || applicationDataModels.isEmpty()) {
            Log.e("EventsActivity", "Event data is null or empty");
        } else {
            Log.e("EventsActivity", "Loaded all events");

        }
    }

    // initialize UI
    private void initializeUI(View rootView) {
        layoutOne = rootView.findViewById(R.id.layout_one);
        layoutTwo = rootView.findViewById(R.id.layout_two);
        loadingLayout = rootView.findViewById(R.id.loading_layout);
        noEventsLayout = rootView.findViewById(R.id.no_event_layout);
        layoutTwoSub = layoutTwo.findViewById(R.id.layout_two_sub);
        confirmedEventsText = layoutTwoSub.findViewById(R.id.confirmed_text);
        pendingEventsText = layoutTwoSub.findViewById(R.id.pending_text);
        noEventsText = noEventsLayout.findViewById(R.id.no_events_text);
        sadFrog = noEventsLayout.findViewById(R.id.sad_frog1);

        eventCardContainer = rootView.findViewById(R.id.eventCardContainer);

        layoutOne.setVisibility(View.GONE);
        layoutTwo.setVisibility(View.GONE);

        noEventsLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    private void fetchPendingEventsUI(List<EventDataModel> eventDataList, View rootView) {

        //empty constructor
        Log.e("EventsActivity", "Events pending called");
        checkIfListsAreEmpty(rootView);
    }

    public void fetchConfirmedEventsUI(List<EventDataModel> eventDataList, View rootView) {

        Log.e("EventsActivity", "Events confirmed called");

        checkIfListsAreEmpty(rootView);

    }

    public void checkIfListsAreEmpty(View rootView) {

        List<EventDataModel> pendingEvents = eventsViewModel.getPendingEvents().getValue();
        List<EventDataModel> confirmedEvents = eventsViewModel.getConfirmedEvents().getValue();

        if ((pendingEvents == null || pendingEvents.isEmpty()) &&
                (confirmedEvents == null || confirmedEvents.isEmpty())) {
            Log.e("EventsActivity", "No events applied by user");
            // Both lists are empty, not events applied by user
            // Update sad frog blank UI
            updateBlankUI();

        } else {
            // At least one list has events
            // Update your UI to display the events
            Log.e("EventsActivity", "Got pending or confirmed events applied by user");
            eventCardContainer.removeAllViews();
            updateConfirmedEventsUI(rootView, confirmedEvents, userId);
            setupConfirmedEventListener(rootView, confirmedEvents, userId);
            setupPendingEventListener(rootView, pendingEvents, userId);

        }
    }

    private void updateBlankUI() {
        loadingLayout.setVisibility(View.GONE);
        layoutOne.setVisibility(View.VISIBLE);
        layoutTwo.setVisibility(View.GONE);
        noEventsLayout.setVisibility(View.GONE);

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

    private void updateConfirmedEventsUI(View rootView, List<EventDataModel> confirmedEvents, int userId) {
        loadingLayout.setVisibility(View.GONE);
        layoutOne.setVisibility(View.GONE);

        confirmedEventsText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        pendingEventsText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        eventCardContainer.removeAllViews();

        if (confirmedEvents == null || confirmedEvents.isEmpty()) {
            // no confirmed event
            layoutTwo.setVisibility(View.VISIBLE);
            noEventsLayout.setVisibility(View.VISIBLE);
            noEventsText.setText("No confirmed events :(");
            noEventsText.setVisibility(View.VISIBLE);
            sadFrog.setVisibility(View.VISIBLE);
        } else {
            layoutTwo.setVisibility(View.VISIBLE);
            noEventsLayout.setVisibility(View.GONE);
            noEventsText.setVisibility(View.GONE);
            sadFrog.setVisibility(View.GONE);

            EventCardHelper.createEventCardList(getContext(), confirmedEvents, eventCardContainer, userId, "events");
            Log.e("EventsActivity", "Event card made in updateConfirmedEventsUI");
        }
    }

    private void updatePendingEventsUI(View rootView, List<EventDataModel> pendingEvents, int userId) {
        pendingEventsText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        confirmedEventsText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        eventCardContainer.removeAllViews();
        layoutOne.setVisibility(View.GONE);
        layoutTwo.setVisibility(View.VISIBLE);

        if (pendingEvents == null || pendingEvents.isEmpty()) { // no pending event
            loadingLayout.setVisibility(View.GONE);
            noEventsLayout.setVisibility(View.VISIBLE);
            noEventsText.setText("No pending events :(");
            noEventsText.setVisibility(View.VISIBLE);
            sadFrog.setVisibility(View.VISIBLE);
        } else {
            loadingLayout.setVisibility(View.GONE);
            noEventsText.setVisibility(View.GONE);
            sadFrog.setVisibility(View.GONE);
            EventCardHelper.createEventCardList(getContext(), pendingEvents, eventCardContainer, userId, "events");
        }
    }

    private void setupConfirmedEventListener(View rootView, List<EventDataModel> confirmedEvents, int userId) {
        confirmedEventsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Your onClick logic here
                updateConfirmedEventsUI(rootView, confirmedEvents, userId);

            }
        });
    }

    private void setupPendingEventListener(View rootView, List<EventDataModel> pendingEvents, int userId) {
        pendingEventsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Your onClick logic here
                updatePendingEventsUI(rootView, pendingEvents, userId);

            }
        });
    }
}





