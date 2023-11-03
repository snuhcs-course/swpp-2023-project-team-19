package com.example.gathernow;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventCreate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventCreate extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ServiceApi service;

    public EventCreate() {
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
    public static EventCreate newInstance(String param1, String param2) {
        EventCreate fragment = new EventCreate();
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
    private TextView event_num_participants_text;

    private String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_id", null); // Return null if the user_id doesn't exist
    }

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
        event_num_participants_text = rootView.findViewById(R.id.event_num_participants);

        // event date and time
        final int[] event_year_input = {0};
        final int[] event_month_input = {0};
        final int[] event_date_input = {0};
        final int[] event_hour_input = {0};
        final int[] event_min_input = {0};

        final int[] event_type_input = {0};

        // event registration available until: data and time
        final int[] event_reg_year_input = {0};
        final int[] event_reg_month_input = {0};
        final int[] event_reg_date_input = {0};
        final int[] event_reg_hour_input = {0};
        final int[] event_reg_min_input = {0};

        Button event_date = (Button) rootView.findViewById(R.id.event_date);
        Button event_time = (Button) rootView.findViewById(R.id.event_time);
        Button event_reg_date = (Button) rootView.findViewById(R.id.event_register_date);
        Button event_reg_time = (Button) rootView.findViewById(R.id.event_register_time);
        //Button event_num_participants = (Button) rootView.findViewById(R.id.event_num_participants);

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
        /*
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

         */

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
                                event_date.setText("Date: " + String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1) + "/" + String.valueOf(year));
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
                                String formattedHour = String.format("%02d", hour);
                                String formattedMins = String.format("%02d", mins);
                                event_time.setText("Time: " + formattedHour + ":" + formattedMins);
                                //event_time.setText("Time: " + String.valueOf(hour) + ":" + String.valueOf(mins));
                            }
                        },
                        hour, minutes, false);
                timePickerDialog.show();
            }
        });

        // event registration date
        event_reg_date.setOnClickListener(new View.OnClickListener() {
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
                                event_reg_year_input[0] = year;
                                event_reg_month_input[0] = monthOfYear;
                                event_reg_date_input[0] = dayOfMonth;
                                event_reg_date.setText("Date: " + String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1) + "/" + String.valueOf(year));
                            }
                        },
                        year, month, day);
                // set the minimum date to today
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }

        });

        // event registration time
        event_reg_time.setOnClickListener(new View.OnClickListener() {
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
                                event_reg_hour_input[0] = hour;
                                event_reg_min_input[0] = mins;
                                String formattedHour = String.format("%02d", hour);
                                String formattedMins = String.format("%02d", mins);
                                event_reg_time.setText("Time: " + formattedHour + ":" + formattedMins);
                            }
                        },
                        hour, minutes, false);
                timePickerDialog.show();
            }
        });


        // multiple languages selection
        TextView textView;
        boolean[] selectedLanguage;
        ArrayList<Integer> langList = new ArrayList<>();
        String[] langArray = {"Korean", "English", "Japanese", "Chinese", "Russian", "Vietnamese", "Thai", "Uzbek", "Khmer", "Filipino", "Nepali", "Indonesian", "Kazakh", "Mongolian", "Burmese", "Spanish", "Portuguese", "French", "German", "Hindi", "Arabic", "Bengali", "Urdu", "Turkish", "Other (specify in descriptions)"};

        // assign variable
        textView = rootView.findViewById(R.id.event_language);

        // initialize selected language array
        selectedLanguage = new boolean[langArray.length];
        final String[] selected_language = {""};

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                // set title
                builder.setTitle("Select Language");
                // set dialog non cancelable
                builder.setCancelable(false);
                builder.setMultiChoiceItems(langArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            langList.add(i);
                            Collections.sort(langList);
                        } else {
                            langList.remove(Integer.valueOf(i));
                        }
                    }
                });


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < langList.size(); j++) {
                            // concat array value
                            stringBuilder.append(langArray[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value  not equal to lang list size - 1, add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        textView.setText(stringBuilder.toString());
                        selected_language[0] = stringBuilder.toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedLanguage.length; j++) {
                            // remove all selection
                            selectedLanguage[j] = false;
                            // clear language list
                            langList.clear();
                            // clear text view value
                            textView.setText("");
                        }
                    }
                });
                // show dialog
                builder.show();
            }
        });

        // create button
        Button createButton = (Button) rootView.findViewById(R.id.post);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get event details in correct format
                String event_name = event_name_text.getText().toString();
                //Integer event_num_participants = event_num_participants_input[0];

                Integer event_num_joined = 0;

                int year = event_year_input[0];
                int month = event_month_input[0]; // Calendar months are 0-based (i.e., January is 0)
                int date = event_date_input[0];

                String event_date = String.format(Locale.ENGLISH, "%d-%02d-%02d", year, month + 1, date);

                int hour = event_hour_input[0];
                int minute = event_min_input[0];

                String event_time = String.format(Locale.ENGLISH,"%02d:%02d:00", hour, minute);

                int r_year = event_reg_year_input[0];
                int r_month = event_reg_month_input[0]; // Calendar months are 0-based (i.e., January is 0)
                int r_date = event_reg_date_input[0];

                String event_reg_date = String.format(Locale.ENGLISH, "%d-%02d-%02d", r_year, r_month + 1, r_date);

                int r_hour = event_reg_hour_input[0];
                int r_minute = event_reg_min_input[0];

                String event_reg_time = String.format(Locale.ENGLISH,"%02d:%02d:00", r_hour, r_minute);


                String event_duration = event_duration_text.getText().toString();
                String event_language = selected_language[0].replace("(specify in descriptions)", "");

                String event_price_str = event_price_text.getText().toString();
                int event_price;
                try {
                    event_price = Integer.parseInt(event_price_str);
                } catch (NumberFormatException e) {
                    // Handle exception if the string cannot be parsed to an integer
                    e.printStackTrace();
                    // set it to a default value or show an error message
                    event_price = 0;
                }

                String event_num_participants_str = event_num_participants_text.getText().toString();
                int event_num_participants;
                try {
                    event_num_participants = Integer.parseInt(event_num_participants_str);
                } catch (NumberFormatException e) {
                    // Handle exception if the string cannot be parsed to an integer
                    e.printStackTrace();
                    // set it to a default value or show an error message
                    event_num_participants = 0;
                }

                String event_description = event_description_text.getText().toString();
                String event_location = event_location_text.getText().toString();


                String event_type_in = "";

                if (event_type_input[0] == 1) {
                    event_type_in = "Leisure";
                } else if (event_type_input[0] == 2) {
                    event_type_in =  "Sports";
                } else if (event_type_input[0] == 3) {
                    event_type_in =  "Workshops";
                } else if (event_type_input[0] == 4) {
                    event_type_in =  "Parties";
                } else if (event_type_input[0] == 5) {
                    event_type_in = "Cultural activities";
                } else if (event_type_input[0] == 6) {
                    event_type_in = "Others";
                }


                TextView alert = (TextView) rootView.findViewById(R.id.alert);

                if (event_name.isEmpty() || event_duration.isEmpty() || event_price_str.isEmpty() ||
                        event_description.isEmpty() || event_location.isEmpty() || event_date_input[0] == 0 ||
                        event_hour_input[0] == 0 || event_num_participants_str.isEmpty() || event_type_in.isEmpty() ||
                        event_reg_date_input[0] == 0 || event_reg_hour_input[0] == 0 || event_language.isEmpty()) {
                    String alert_msg = "Please fill in all required fields";
                    alert.setText(alert_msg);
                } else if (event_num_participants == 0) {
                    String alert_msg = "Number of participants should be at least 1!";
                    alert.setText(alert_msg);
                } else {
                        Integer host_id = Integer.valueOf(getUserId(getActivity()));

                        service = RetrofitClient.getClient().create(ServiceApi.class);
                        EventData requestData = new EventData(event_type_in, event_name, event_num_participants, event_date, event_time, event_duration, event_language, event_price, event_location, event_description, event_num_joined, host_id, event_reg_date, event_reg_time);
                        service.eventlist(requestData).enqueue(new Callback<CodeMessageResponse>() {
                            @Override
                            public void onResponse(Call<CodeMessageResponse> call, Response<CodeMessageResponse> response) {
                                if (response.isSuccessful()) {
                                    CodeMessageResponse result = response.body();
                                    if (result != null) {
                                        if (response.code() == 201) {

                                            //Toast.makeText(EventCreate.this, "Event created successfully.", Toast.LENGTH_SHORT).show();
                                            // Link to the createSuccessful page
                                            Intent intent = new Intent(v.getContext(), CreateSuccessful.class);
                                            startActivity(intent);
                                        }
                                    } else {
                                        // Handle the case where the response body is null or empty
                                        Toast.makeText(getActivity(), "Bad Request.", Toast.LENGTH_SHORT).show();


                                    }
                                } else {
                                    // Handle the case where the response is not successful (e.g., non-2xx HTTP status)
                                    Toast.makeText(getActivity(), "Event creation failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<CodeMessageResponse> call, Throwable t) {
                                Toast.makeText(getActivity(), "Event creation Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

            }
        });


        // return button
        ImageButton returnButton = (ImageButton) rootView.findViewById(R.id.returnBtn);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Link to the searchHomepage
                Intent intent = new Intent(v.getContext(), FragHome.class);
                startActivity(intent);
            }
        });

        return rootView;
    }


}