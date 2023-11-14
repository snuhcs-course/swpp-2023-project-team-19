package com.example.gathernow.main_ui.event_creation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.example.gathernow.CreateSuccessful;
import com.example.gathernow.FragHome;
import com.example.gathernow.R;
import com.example.gathernow.authenticate.UserLocalDataSource;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventCreateActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventCreateActivity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean imageUploaded = false;
    private String eventThumbnailFilePath = null;
    private ImageView eventThumbnailImageView;
    private File eventThumbnailFile;
    private EventCreateViewModel eventCreateViewModel;
    private EventRepository eventRepository;
    private EventDataSource eventDataSource;
    private Button uploadImgButton;
    private String selectedEventType = null;
    private Button eventDateButton;
    private Button eventTimeButton;
    private Button eventLastRegisterDateButton;
    private Button eventLastRegisterTimeButton;

    public EventCreateActivity() {
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
    public static EventCreateActivity newInstance(String param1, String param2) {
        EventCreateActivity fragment = new EventCreateActivity();
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
        // Data source
        eventDataSource = new EventDataSource();
        // Repository
        eventRepository = new EventRepository(eventDataSource);
        eventCreateViewModel = new EventCreateViewModel(getContext(), eventRepository);

    }

    private TextView eventNameText;
    private TextView eventDurationText;
    private TextView eventPriceText;
    private TextView eventDescriptionText;
    private TextView eventLocationText;
    private TextView eventNumParticipantsText;
    private ActivityResultLauncher<PickVisualMediaRequest> pickProfilePic;
    private TextView eventTypeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_create, container, false);
        ImageView eventImage = rootView.findViewById(R.id.event_image);
        eventCreateViewModel.getAlertMessage().observe(getViewLifecycleOwner(), message -> {
            Log.d("EventCreateActivity Testing", "Alert Message: " + message);
            if (message.equals("Event created successfully")) {
                Intent intent = new Intent(getContext(), CreateSuccessful.class);
                startActivity(intent);
                getActivity().finish();
            }
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        // TODO: upload image
        uploadImgButton = rootView.findViewById(R.id.event_image_upload);
        pickProfilePic = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                eventImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                eventImage.setImageURI(uri);
                eventCreateViewModel.handleEventThumbnailUpload(uri);
            }
        });
        uploadImgButton.setOnClickListener(v -> {
            onEventThumbnailUpload();
        });
        eventCreateViewModel.getEventFileThumbnail().observe(getViewLifecycleOwner(), filePath -> {
            if (!imageUploaded) {
                eventThumbnailFilePath = filePath;
                imageUploaded = true;
                uploadImgButton.setText("Delete");
            } else {
                eventThumbnailFilePath = null;
                imageUploaded = false;
                uploadImgButton.setText("Upload");
            }

        });

        // TODO: event type
        eventTypeView = rootView.findViewById(R.id.event_type);
        AtomicReference<String> eventTypeIn = new AtomicReference<>("");
        eventTypeView.setOnClickListener(v -> {
            showEventTypeDialog();
        });


        // TODO: location implementation

        eventNameText = rootView.findViewById(R.id.event_name);
        eventDurationText = rootView.findViewById(R.id.event_duration);
        eventPriceText = rootView.findViewById(R.id.event_price);
        eventDescriptionText = rootView.findViewById(R.id.event_description);
        eventLocationText = rootView.findViewById(R.id.event_location);
        eventNumParticipantsText = rootView.findViewById(R.id.event_num_participants);

        // event date and time
        final int[] event_year_input = {0};
        final int[] event_month_input = {0};
        final int[] event_date_input = {0};
        final int[] event_hour_input = {0};
        final int[] event_min_input = {0};

        // event registration available until: data and time
        final int[] event_reg_year_input = {0};
        final int[] event_reg_month_input = {0};
        final int[] event_reg_date_input = {0};
        final int[] event_reg_hour_input = {0};
        final int[] event_reg_min_input = {0};

        eventDateButton = rootView.findViewById(R.id.event_date);
        eventTimeButton = rootView.findViewById(R.id.event_time);
        eventLastRegisterDateButton = rootView.findViewById(R.id.event_register_date);
        eventLastRegisterTimeButton = rootView.findViewById(R.id.event_register_time);

        // event date
        eventDateButton.setOnClickListener(v -> {
            showDatePickerDialog();
        });
        // Observe the event date changes and update UI
        eventCreateViewModel.getEventDate().observe(getViewLifecycleOwner(), calendar -> updateEventDateUI(calendar, eventDateButton));

        // event time
        eventTimeButton.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minutes = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hour1, mins) -> {
                // TODO: time
                event_hour_input[0] = hour1;
                event_min_input[0] = mins;
                String formattedHour = String.format("%02d", hour1);
                String formattedMins = String.format("%02d", mins);
                eventTimeButton.setText("Time: " + formattedHour + ":" + formattedMins);
                //eventTimeButton.setText("Time: " + String.valueOf(hour) + ":" + String.valueOf(mins));
            }, hour, minutes, false);
            timePickerDialog.show();
        });

        // event registration date
        eventLastRegisterDateButton.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, monthOfYear, dayOfMonth) -> {
                // TODO: date
                event_reg_year_input[0] = year1;
                event_reg_month_input[0] = monthOfYear;
                event_reg_date_input[0] = dayOfMonth;
                eventLastRegisterDateButton.setText("Date: " + String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year1));
            }, year, month, day);
            // set the minimum date to today
            datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.show();
        });

        // event registration time
        eventLastRegisterTimeButton.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minutes = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int mins) {
                    // TODO: time
                    event_reg_hour_input[0] = hour;
                    event_reg_min_input[0] = mins;
                    String formattedHour = String.format("%02d", hour);
                    String formattedMins = String.format("%02d", mins);
                    eventLastRegisterTimeButton.setText("Time: " + formattedHour + ":" + formattedMins);
                }
            }, hour, minutes, false);
            timePickerDialog.show();
        });


        // multiple languages selection
        TextView languageTextView;
        boolean[] selectedLanguage;
        ArrayList<Integer> langList = new ArrayList<>();
        String[] langArray = {"Korean", "English", "Japanese", "Chinese", "Russian", "Vietnamese", "Thai", "Uzbek", "Khmer", "Filipino", "Nepali", "Indonesian", "Kazakh", "Mongolian", "Burmese", "Spanish", "Portuguese", "French", "German", "Hindi", "Arabic", "Bengali", "Urdu", "Turkish", "Other (specify in descriptions)"};

        // assign variable
        languageTextView = rootView.findViewById(R.id.event_language);

        // initialize selected language array
        selectedLanguage = new boolean[langArray.length];
        final String[] selected_language = {""};

        languageTextView.setOnClickListener(view -> {

            // Initialize alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            // set title
            builder.setTitle("Select Language");
            // set dialog non cancelable
            builder.setCancelable(false);
            builder.setMultiChoiceItems(langArray, selectedLanguage, (dialogInterface, i, b) -> {
                // check condition
                if (b) {
                    langList.add(i);
                    Collections.sort(langList);
                } else {
                    langList.remove(Integer.valueOf(i));
                }
            });


            builder.setPositiveButton("OK", (dialogInterface, i) -> {
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
                languageTextView.setText(stringBuilder.toString());
                selected_language[0] = stringBuilder.toString();
            });
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                // dismiss dialog
                dialogInterface.dismiss();
            });
            builder.setNeutralButton("Clear All", (dialogInterface, i) -> {
                // use for loop
                for (int j = 0; j < selectedLanguage.length; j++) {
                    // remove all selection
                    selectedLanguage[j] = false;
                    // clear language list
                    langList.clear();
                    // clear text view value
                    languageTextView.setText("");
                }
            });
            // show dialog
            builder.show();
        });

        // create button
        Button createButton = rootView.findViewById(R.id.post);
        createButton.setOnClickListener(v -> {

            // get event details in correct format
            String event_name = eventNameText.getText().toString();

            Integer event_num_joined = 0;

            int year = event_year_input[0];
            int month = event_month_input[0]; // Calendar months are 0-based (i.e., January is 0)
            int date = event_date_input[0];

            String event_date1 = String.format(Locale.ENGLISH, "%d-%02d-%02d", year, month + 1, date);

            int hour = event_hour_input[0];
            int minute = event_min_input[0];

            String event_time1 = String.format(Locale.ENGLISH, "%02d:%02d:00", hour, minute);

            int r_year = event_reg_year_input[0];
            int r_month = event_reg_month_input[0]; // Calendar months are 0-based (i.e., January is 0)
            int r_date = event_reg_date_input[0];

            String event_reg_date1 = String.format(Locale.ENGLISH, "%d-%02d-%02d", r_year, r_month + 1, r_date);

            int r_hour = event_reg_hour_input[0];
            int r_minute = event_reg_min_input[0];

            String event_reg_time1 = String.format(Locale.ENGLISH, "%02d:%02d:00", r_hour, r_minute);

            String event_duration = eventDurationText.getText().toString();
            String event_language = selected_language[0].replace("(specify in descriptions)", "");

            String event_price_str = eventPriceText.getText().toString();
            int event_price;
            try {
                event_price = Integer.parseInt(event_price_str);
            } catch (NumberFormatException e) {
                // Handle exception if the string cannot be parsed to an integer
                e.printStackTrace();
                // set it to a default value or show an error message
                event_price = 0;
            }

            String event_num_participants_str = eventNumParticipantsText.getText().toString();
            int event_num_participants;
            try {
                event_num_participants = Integer.parseInt(event_num_participants_str);
            } catch (NumberFormatException e) {
                // Handle exception if the string cannot be parsed to an integer
                e.printStackTrace();
                // set it to a default value or show an error message
                event_num_participants = 0;
            }

            String event_description = eventDescriptionText.getText().toString();
            String event_location = eventLocationText.getText().toString();


            TextView alert = rootView.findViewById(R.id.alert);

            if (event_name.isEmpty() || event_duration.isEmpty() || event_price_str.isEmpty() || event_description.isEmpty() || event_location.isEmpty() || event_date_input[0] == 0 || event_hour_input[0] == 0 || event_num_participants_str.isEmpty() || eventTypeIn.get().isEmpty() || event_reg_date_input[0] == 0 || event_reg_hour_input[0] == 0 || event_language.isEmpty()) {
                String alert_msg = "Please fill in all required fields";
                alert.setText(alert_msg);
            } else if (event_num_participants == 0) {
                String alert_msg = "Number of participants should be at least 1!";
                alert.setText(alert_msg);
            } else {
                // send info to host
                UserLocalDataSource userLocalDataSource = new UserLocalDataSource(getContext());
                String hostId = userLocalDataSource.getUserId();
                Log.d("EventCreateActivity Testing", "hostId: " + hostId);
                Log.d("EventCreateActivity Testing", "Thumbnail Filepath:" + eventThumbnailFilePath);
                eventCreateViewModel.createEvent(eventThumbnailFilePath, hostId, eventTypeIn.get(), event_name, event_description, event_date1, event_time1, event_duration, event_location, event_language, event_num_participants_str, event_price_str, event_reg_date1, event_reg_time1);
//
//                    @Override
//                    public void onResponse(Call<CodeMessageResponse> call, Response<CodeMessageResponse> response) {
//                        if (response.isSuccessful()) {
//                            CodeMessageResponse result = response.body();
//                            if (result != null) {
//                                if (response.code() == 201) {
//
//                                    //Toast.makeText(EventCreate.this, "Event created successfully.", Toast.LENGTH_SHORT).show();
//                                    // Link to the createSuccessful page
//                                    Intent intent = new Intent(v.getContext(), CreateSuccessful.class);
//                                    startActivity(intent);
//                                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
//                                    transaction.remove(EventCreateActivity.this);
//                                    transaction.commit();
//                                }
//                            } else {
//                                // Handle the case where the response body is null or empty
//                                Toast.makeText(getActivity(), "Bad Request.", Toast.LENGTH_SHORT).show();
//
//

            }

        });


        // return button
        ImageButton returnButton = rootView.findViewById(R.id.returnBtn);
        returnButton.setOnClickListener(v -> {
            // Link to the searchHomepage
            Intent intent = new Intent(v.getContext(), FragHome.class);
            startActivity(intent);
        });

        return rootView;
    }

    private void updateEventDateUI(Calendar date, Button eventDateButton) {
        if (date != null) {
            int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
            int monthOfYear = date.get(Calendar.MONTH);
            int year = date.get(Calendar.YEAR);
            eventDateButton.setText("Date: " + dayOfMonth + "/" + String.valueOf(monthOfYear + 1) + "/" + year);
        }
    }

    private void onEventThumbnailUpload() {
        if (!imageUploaded) {
            Log.d("EventCreateActivity Testing", "Upload image button clicked");
            pickProfilePic.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
        } else {
            Log.d("EventCreateActivity Testing", "Delete img");
            // TODO: Delete selected image
            eventThumbnailImageView.setImageResource(R.mipmap.ic_sad_frog_foreground);
            eventThumbnailImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            eventThumbnailFilePath = null;
            uploadImgButton.setText("Upload");
            imageUploaded = false;
        }

    }

    private void showEventTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Event Type").setCancelable(false).setSingleChoiceItems(eventCreateViewModel.getTypeChoices(), eventCreateViewModel.getEventTypeInputIdx(), (dialogInterface, i) -> {
            eventCreateViewModel.setEventTypeInputIdx(i);
            eventTypeView.setText(eventCreateViewModel.getTypeChoices()[i]);
//                    eventTypeInputIdx[0] = i + 1;
        }).setPositiveButton("Select", (dialogInterface, i) -> {
            int selectedId = eventCreateViewModel.getEventTypeInputIdx();
            selectedEventType = eventCreateViewModel.getTypeChoices()[selectedId];
            Log.d("EventCreateActivity Testing", "Event type: " + selectedEventType);
        }).setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        builder.show();
    }

    private void showDatePickerDialog() {
        Calendar currentDate = eventCreateViewModel.getEventDate().getValue();
        if (currentDate == null) {
            currentDate = Calendar.getInstance();
        }
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (datePicker1, year1, monthOfYear, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year1, monthOfYear, dayOfMonth);
            eventCreateViewModel.setEventDate(selectedDate);
        }, year, month, day);
        // set the minimum date to today
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.show();
    }
}