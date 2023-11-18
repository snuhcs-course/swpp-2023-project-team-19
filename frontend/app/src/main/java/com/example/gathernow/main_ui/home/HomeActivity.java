package com.example.gathernow.main_ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.gathernow.R;
import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.authenticate.UserLocalDataSource;
import com.example.gathernow.utils.EventCardHelper;

import java.util.List;

public class HomeActivity extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private HomeViewModel homeViewModel;

    public HomeActivity() {
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
    public static HomeActivity newInstance(String param1, String param2) {
        HomeActivity fragment = new HomeActivity();
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

        homeViewModel = new HomeViewModel(getContext());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel.getAlertMessage().observe(getViewLifecycleOwner(), message -> Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show());
        homeViewModel.getAllEvents().observe(getViewLifecycleOwner(), eventDataModels -> fetchEventsUI(eventDataModels, rootView));
        homeViewModel.fetchAllEvents();

        return rootView;
    }

    private void fetchEventsUI(List<EventDataModel> eventDataList, View rootView) {

        //get current login user id
        UserLocalDataSource userLocalDataSource = new UserLocalDataSource(getActivity());
        int userId = Integer.parseInt(userLocalDataSource.getUserId());

        if (eventDataList == null || eventDataList.isEmpty()){
            Log.d("HomeActivity", "Event data is null or empty");
        } else {
            Log.d("HomeActivity", "Loaded user events");
            LinearLayout eventCardContainer = rootView.findViewById(R.id.eventCardContainer);
            EventCardHelper.createEventCardList(getContext(), eventDataList, eventCardContainer, userId);
        }

    }



}
