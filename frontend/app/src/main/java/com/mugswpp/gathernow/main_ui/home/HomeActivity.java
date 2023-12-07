package com.mugswpp.gathernow.main_ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mugswpp.gathernow.FilterFragment;
import com.mugswpp.gathernow.R;
import com.mugswpp.gathernow.api.models.EventDataModel;
import com.mugswpp.gathernow.authenticate.UserLocalDataSource;
import com.mugswpp.gathernow.main_ui.event_search.EventSearchActivity;
import com.mugswpp.gathernow.utils.EventCardHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class HomeActivity extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private HomeViewModel homeViewModel;

    private RelativeLayout loadingLayout;
    private ScrollView scroll;

    private String frag = "home";

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
        loadingLayout = rootView.findViewById(R.id.loading_layout);
        scroll = rootView.findViewById(R.id.scroll);
        loadingLayout.setVisibility(View.VISIBLE);
        scroll.setVisibility(View.GONE);

        homeViewModel.getAlertMessage().observe(getViewLifecycleOwner(), message -> Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show());
        homeViewModel.getAllEvents().observe(getViewLifecycleOwner(), eventDataModels -> fetchEventsUI(eventDataModels, rootView));
        homeViewModel.fetchAllEvents();


        setupSearchListener(rootView);
        setupFilterListener(rootView);


        return rootView;
    }

    private void fetchEventsUI(List<EventDataModel> eventDataList, View rootView) {

        //get current login user id
        UserLocalDataSource userLocalDataSource = new UserLocalDataSource(getActivity());
        if (userLocalDataSource.getUserId() == null) {
            return;
        }
        int userId = Integer.parseInt(userLocalDataSource.getUserId());

        if (eventDataList == null || eventDataList.isEmpty()){
            Log.d("HomeActivity", "Event data is null or empty");
        } else {
            Log.d("HomeActivity", "Loaded user events");
            LinearLayout eventCardContainer = rootView.findViewById(R.id.eventCardContainer);
            EventCardHelper.createEventCardList(getContext(), eventDataList, eventCardContainer, userId, frag);
        }
        loadingLayout.setVisibility(View.GONE);
        scroll.setVisibility(View.VISIBLE);
    }

    private void setupSearchListener(View rootView) {
        ImageButton searchButton = rootView.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EventSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupFilterListener(View rootView) {
        ImageButton filterButton = rootView.findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bottomSheetDialogFragment = new FilterFragment();
                bottomSheetDialogFragment.show(getChildFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
    }

}
