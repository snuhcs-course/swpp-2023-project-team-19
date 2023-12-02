package com.example.gathernow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.gathernow.main_ui.event_filter.EventFilterActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ChipGroup chipGroup;

    public FilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance(String param1, String param2) {
        FilterFragment fragment = new FilterFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);

        SwitchCompat freeSwitch = rootView.findViewById(R.id.freeSwitch);

        ChipGroup languageFilter = rootView.findViewById(R.id.language_filter);
        ChipGroup eventTypeFilter = rootView.findViewById(R.id.event_type_filter);
        ChipGroup dateFilter = rootView.findViewById(R.id.date_filter);
        ChipGroup timeFilter = rootView.findViewById(R.id.time_filter);
        EditText locationFilter = rootView.findViewById(R.id.location_filter);

        ImageButton resetButton = rootView.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear selected filters
                clearChipGroup(languageFilter);
                clearChipGroup(eventTypeFilter);
                clearChipGroup(dateFilter);
                clearChipGroup(timeFilter);

                // Set the switch to off
                freeSwitch.setChecked(false);
            }
        });

        ImageButton closeButton = rootView.findViewById(R.id.bottomSheetCloseButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button confirmButton = rootView.findViewById(R.id.applyfilter_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the state of the switch
                boolean isFreeEvent = freeSwitch.isChecked();

                // get selected chips from ChipGroups
                List<String> selectedLanguageChips = getSelectedChipTexts(languageFilter);
                List<String> selectedEventTypeChips = getSelectedChipTexts(eventTypeFilter);
                List<String> selectedDateChips = getSelectedChipTexts(dateFilter);
                List<String> selectedTimeChips = getSelectedChipTexts(timeFilter);
                String location = locationFilter.getText().toString().trim();
                List<String> selectedEventLocation = new ArrayList<>();
                selectedEventLocation.add(location);

                Intent intent = new Intent(getActivity(), EventFilterActivity.class);

                intent.putExtra("isFreeEvent", isFreeEvent);
                intent.putStringArrayListExtra("selectedLanguageChips", new ArrayList<>(selectedLanguageChips));
                intent.putStringArrayListExtra("selectedEventTypeChips", new ArrayList<>(selectedEventTypeChips));
                intent.putStringArrayListExtra("selectedDateChips", new ArrayList<>(selectedDateChips));
                intent.putStringArrayListExtra("selectedTimeChips", new ArrayList<>(selectedTimeChips));
                intent.putStringArrayListExtra("selectedEventLocation", new ArrayList<>(selectedEventLocation));
                if (getActivity() instanceof EventFilterActivity) {
                    getActivity().finish();
                }
                startActivity(intent);
                dismiss();
            }
        });

        return rootView;
    }

    private List<String> getSelectedChipTexts(ChipGroup chipGroup) {
        List<String> selectedChipTexts = new ArrayList<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                selectedChipTexts.add(chip.getText().toString());
            }
        }
        return selectedChipTexts;
    }

    private void clearChipGroup(ChipGroup chipGroup) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setChecked(false);
        }
    }

}

