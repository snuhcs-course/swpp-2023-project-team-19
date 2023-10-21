package com.example.gathernow;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link eventCreate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class eventCreate extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public eventCreate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment eventCreate.
     */
    // TODO: Rename and change types and number of parameters
    public static eventCreate newInstance(String param1, String param2) {
        eventCreate fragment = new eventCreate();
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

    private TextView event_name_text;
    private TextView event_duration_text;
    private TextView event_price_text;
    private TextView event_description_text;
    private TextView event_location_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_create, container, false);

        // TODO: location implementation

        event_name_text = rootView.findViewById(R.id.event_name);
        event_duration_text = rootView.findViewById(R.id.event_duration);
        event_price_text = rootView.findViewById(R.id.event_price);
        event_description_text = rootView.findViewById(R.id.event_description);
        event_location_text = rootView.findViewById(R.id.event_location);
        TextView event_type = (TextView) rootView.findViewById(R.id.event_type);

        final int[] event_num_participants_input = {0};
        final int[] event_year_input = {0};
        final int[] event_month_input = {0};
        final int[] event_date_input = {0};
        final int[] event_hour_input = {0};
        final int[] event_min_input = {0};
        final int[] event_type_input = {0};

        Button event_date = (Button) rootView.findViewById(R.id.event_date);
        Button event_time = (Button) rootView.findViewById(R.id.event_time);
        Button event_num_participants = (Button) rootView.findViewById(R.id.event_num_participants);

        RadioGroup radioGroup = rootView.findViewById(R.id.imageRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.image1_leisure) {
                    String event_type_text = "Leisure";
                    event_type.setText(event_type_text);
                    event_type_input[0] = 1;
                } else if (checkedId == R.id.image2_sports) {
                    String event_type_text = "Sports";
                    event_type.setText(event_type_text);
                    event_type_input[0] = 2;
                } else if (checkedId == R.id.image3_workshops) {
                    String event_type_text = "Workshops";
                    event_type.setText(event_type_text);
                    event_type_input[0] = 3;
                } else if (checkedId == R.id.image4_parties) {
                    String event_type_text = "Parties";
                    event_type.setText(event_type_text);
                    event_type_input[0] = 4;
                } else if (checkedId == R.id.image5_cultural_activities) {
                    String event_type_text = "Cultural activities";
                    event_type.setText(event_type_text);
                    event_type_input[0] = 5;
                } else if (checkedId == R.id.image6_others) {
                    String event_type_text = "Others";
                    event_type.setText(event_type_text);
                    event_type_input[0] = 6;
                }
            }
        });


        // number of participants
        event_num_participants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NumberPicker numberPicker = new NumberPicker(requireContext());
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(100);

                final AlertDialog.Builder[] builder = {new AlertDialog.Builder(requireContext())};
                builder[0].setTitle("Number of participants");
                builder[0].setView(numberPicker);
                builder[0].setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: number of participants
                        event_num_participants_input[0] = numberPicker.getValue();
                        event_num_participants.setText("Number of participants: " + String.valueOf(event_num_participants_input[0]));
                    }
                });

                builder[0].setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing or add any required action for cancel.
                    }
                });
                builder[0].show();
            }
        });

        // event date
        event_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // TODO: date
                                event_year_input[0] = year;
                                event_month_input[0] = monthOfYear;
                                event_date_input[0] = dayOfMonth;
                                event_date.setText("Date: " + String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear) + "/" + String.valueOf(year));
                            }
                        },
                        year, month, day);
                // set the minimum date to today
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }

        });

        // event time
        event_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minutes = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int mins) {
                                // TODO: time
                                event_hour_input[0] = hour;
                                event_min_input[0] = mins;
                                event_time.setText("Time: " + String.valueOf(hour) + ":" + String.valueOf(mins));
                            }
                        },
                        hour, minutes, false);
                timePickerDialog.show();
            }
        });

        Spinner spinner = (Spinner) rootView.findViewById(R.id.event_language);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.language_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // create button
        Button createButton = (Button) rootView.findViewById(R.id.post);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event_name = event_name_text.getText().toString();
                String event_duration = event_duration_text.getText().toString();
                String event_price = event_price_text.getText().toString();
                String event_description = event_description_text.getText().toString();
                String event_location = event_location_text.getText().toString();

                TextView alert = (TextView) rootView.findViewById(R.id.alert);
                // TODO: handle error here! (does not fill in all required fields)
                if (event_name.isEmpty() || event_duration.isEmpty() || event_price.isEmpty() ||
                        event_description.isEmpty() || event_location.isEmpty() || event_date_input[0] == 0 ||
                        event_hour_input[0] == 0 || event_num_participants_input[0] == 0 || event_type_input[0]==0) {
                    String alert_msg = "Please fill in all required fields";
                    alert.setText(alert_msg);
                } else {
                    // Link to the createSuccessful page
                    Intent intent = new Intent(v.getContext(), createSuccessful.class);
                    startActivity(intent);
                }
            }
        });


        // return button
        ImageButton returnButton = (ImageButton) rootView.findViewById(R.id.returnBtn);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Link to the searchHomepage
                Intent intent = new Intent(v.getContext(), fragHome.class);
                startActivity(intent);
            }
        });

        return rootView;
    }


}