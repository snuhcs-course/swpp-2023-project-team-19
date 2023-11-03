package com.example.gathernow;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    public EventHome() {
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


        // TODO: check condition here, whether the user has any registered event
        Integer condition = 1;
        if (condition == 1) {   // no registered events
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

        } else { // there is upcoming events, either in pending or confirmed state
            layoutOne.setVisibility(View.GONE);
            layoutTwo.setVisibility(View.VISIBLE);
        }

        return rootView;
    }
}