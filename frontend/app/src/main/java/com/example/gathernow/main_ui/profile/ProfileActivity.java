package com.example.gathernow.main_ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import java.util.List;
import java.util.concurrent.ExecutorService;

import com.example.gathernow.FragHome;
import com.example.gathernow.MainActivity;
import com.example.gathernow.R;
import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.api.models.UserDataModel;
import com.example.gathernow.authenticate.UserLocalDataSource;
import com.example.gathernow.utils.EventCardHelper;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileActivity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ExecutorService executorService;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // View elements
    private TextView profileText;
    private ImageView profileImg;
    private LinearLayout layoutOne, layoutTwo;
    private RelativeLayout userInfo, loadingLayout;
    private int userId;
    private ProfileViewModel profileViewModel;
    private UserLocalDataSource userLocalDataSource;

    private boolean isFirstCreation = false;
    private View rootView;

    public ProfileActivity() {
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
    public static ProfileActivity newInstance(String param1, String param2) {
        ProfileActivity fragment = new ProfileActivity();
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
        profileViewModel = new ProfileViewModel(getContext());
        userLocalDataSource = new UserLocalDataSource(getContext());
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("ProfileActivity", "onCreate");
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        isFirstCreation = true;

        initializeUI(rootView);
        profileViewModel.getAlertMessage().observe(getViewLifecycleOwner(), message -> Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show());
        profileViewModel.getUserData().observe(getViewLifecycleOwner(), this::updateProfileUI);
        profileViewModel.getUserEvents().observe(getViewLifecycleOwner(), eventDataModels -> updateUserEventsUI(eventDataModels, rootView));
        profileViewModel.fetchUserProfile(userLocalDataSource);
//        profileViewModel.fetchUserEvents(userId);

        return rootView;
    }


    private void updateUserEventsUI(List<EventDataModel> eventDataList, View rootView) {
        if (eventDataList == null || eventDataList.isEmpty()) {
            Log.d("ProfileActivity", "User event data is null or empty");
            updateBlankUI();
        } else {
            Log.d("ProfileActivity", "Loaded user events");
            LinearLayout eventCardContainer = rootView.findViewById(R.id.eventCardContainer);
            EventCardHelper.createEventCardList(getContext(), eventDataList, eventCardContainer, userId, "profile");
            loadingLayout.setVisibility(View.GONE);
            userInfo.setVisibility(View.VISIBLE);
            layoutOne.setVisibility(View.GONE);
            layoutTwo.setVisibility(View.VISIBLE);
        }
    }

    private void updateBlankUI() {
        loadingLayout.setVisibility(View.GONE);
        userInfo.setVisibility(View.VISIBLE);
        layoutOne.setVisibility(View.VISIBLE);
        layoutTwo.setVisibility(View.GONE);

        TextView discoverNew = layoutOne.findViewById(R.id.discover_new);
        discoverNew.setPaintFlags(discoverNew.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        discoverNew.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), FragHome.class);
            intent.putExtra("targetFragment", "createEvent");
            startActivity(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateProfileUI(UserDataModel userDataModel) {
        if (userDataModel != null) {
            userId = userDataModel.getUserId();
            String userName = userDataModel.getName();
            profileText.setText(userName + ",\nwelcome back!");

            // Update avatar
            String avatarUrl = userDataModel.getAvatar();
            String userAvatarUrl = "http://20.2.88.70:8000" + avatarUrl;
            Picasso.get().load(userAvatarUrl).into(profileImg);

            userInfo.setVisibility(View.VISIBLE);
            loadingLayout.setVisibility(View.GONE);
        }
    }

    private void initializeUI(View rootView) {
        profileText = rootView.findViewById(R.id.subtitle_text);
        profileImg = rootView.findViewById(R.id.profile_image);
        layoutOne = rootView.findViewById(R.id.layout_one);
        layoutTwo = rootView.findViewById(R.id.layout_two);
        userInfo = rootView.findViewById(R.id.user_info);
        loadingLayout = rootView.findViewById(R.id.loading_layout);
        ImageButton logoutButton = rootView.findViewById(R.id.logout_button);

        loadingLayout.setVisibility(View.VISIBLE);
        userInfo.setVisibility(View.GONE);
        layoutOne.setVisibility(View.GONE);
        layoutTwo.setVisibility(View.GONE);

        logoutButton.setOnClickListener(v -> {
            // Handle logout
            UserLocalDataSource userLocalDataSource = new UserLocalDataSource(getActivity());
            userLocalDataSource.clear();
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            startActivity(intent);
        });
    }

}