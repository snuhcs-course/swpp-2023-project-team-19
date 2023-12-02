package com.example.gathernow.main_ui.event_creation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gathernow.FragHome;
import com.example.gathernow.R;
import com.example.gathernow.authenticate.UserLocalDataSource;
import com.example.gathernow.main_ui.EventDataSource;
import com.example.gathernow.main_ui.EventRepository;
import com.example.gathernow.utils.DateTimeHelper;
import com.naver.maps.geometry.LatLng;

import java.util.Calendar;
import java.util.function.Consumer;


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
    private boolean imageUploaded = false, fromFragHome = false;
    private ImageView eventThumbnailImageView;
    private String eventThumbnailFilePath = null;
    private EventCreateViewModel eventCreateViewModel;
    private Button uploadImgButton;
    private String selectedEventType = "";
    private Button eventDateButton;
    private Button eventTimeButton;
    private Button eventLastRegisterDateButton;
    private Button eventLastRegisterTimeButton;
    private TextView eventLanguagesView, noPhotoText;
    private String eventLanguages = "";
    private String eventName = "";
    private String eventDescription = "";
    private String eventLocation = "";
    private String eventDuration = "";
    private String eventDate = "";
    private String eventTime = "";
    private String eventLastRegisterDate = "";
    private String eventLastRegisterTime = "";
    private int eventPrice = -1;
    private int eventNumParticipants = -1;

    private double eventLongitude;

    private double eventLatitude;

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
        EventDataSource eventDataSource = new EventDataSource();
        // Repository
        EventRepository eventRepository = new EventRepository(eventDataSource);
        eventCreateViewModel = new EventCreateViewModel(getContext(), eventRepository);

        Bundle bundle = getArguments();
        if (bundle != null) {
            fromFragHome = bundle.getBoolean("fromFragHome", false);
        }
        Log.d("EventCreateActivity", "fromFragHome: " + fromFragHome);


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
        eventThumbnailImageView = rootView.findViewById(R.id.event_image);
        noPhotoText = rootView.findViewById(R.id.no_photo_text);

        // alert message
        eventCreateViewModel.getAlertMessage().observe(getViewLifecycleOwner(), message -> {
            Log.d("EventCreateActivity Testing", "Alert Message: " + message);
            if (message.equals("Event created successfully")) {
                Intent intent = new Intent(getContext(), CreateSuccessfulActivity.class);
                startActivity(intent);
                getActivity().finish();
            }



            if(!fromFragHome){
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
            else{
                fromFragHome = false;
                Log.d("EventCreateActivity", "fromFragHome(else): " + fromFragHome);
            }
            Log.d("EventCreateActivity", "fromFragHome(after else): " + fromFragHome);


        });

        // upload image
        uploadImgButton = rootView.findViewById(R.id.event_image_upload);
        pickProfilePic = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                eventThumbnailImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                eventThumbnailImageView.setImageURI(uri);
                noPhotoText.setVisibility(View.GONE);
                eventCreateViewModel.handleEventThumbnailUpload(uri);
            }
        });
        uploadImgButton.setOnClickListener(v -> onEventThumbnailUpload());
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

        // Event type
        eventTypeView = rootView.findViewById(R.id.event_type);
        eventTypeView.setOnClickListener(v -> showEventTypeDialog());

        // Event name
        eventNameText = rootView.findViewById(R.id.event_name);
        eventNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                eventCreateViewModel.setEventName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        eventCreateViewModel.getEventName().observe(getViewLifecycleOwner(), name -> {
            eventName = name;
        });

        // Event duration
        eventDurationText = rootView.findViewById(R.id.event_duration);
        eventDurationText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                eventCreateViewModel.setEventDuration(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        eventCreateViewModel.getEventDuration().observe(getViewLifecycleOwner(), duration -> {
            eventDuration = duration;
        });

        // Price
        eventPriceText = rootView.findViewById(R.id.event_price);
        eventPriceText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                eventCreateViewModel.setEventPrice(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        eventCreateViewModel.getEventPrice().observe(getViewLifecycleOwner(), price -> {
            eventPrice = price;
        });

        // Description
        eventDescriptionText = rootView.findViewById(R.id.event_description);
        eventDescriptionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                eventCreateViewModel.setEventDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        eventCreateViewModel.getEventDescription().observe(getViewLifecycleOwner(), description -> {
            eventDescription = description;
        });

        // TODO: Location
        eventLocationText = rootView.findViewById(R.id.event_location);
        eventLocationText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                eventCreateViewModel.setEventLocation(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        eventCreateViewModel.getEventLocation().observe(getViewLifecycleOwner(), location -> {
            eventLocation = location;
        });

        // Search Location Button
        Button pickLocationButton = rootView.findViewById(R.id.pick_location_button);
        pickLocationButton.setOnClickListener(this::onPickLocationClick);

        // Number of participants
        eventNumParticipantsText = rootView.findViewById(R.id.event_num_participants);
        eventNumParticipantsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                eventCreateViewModel.setEventMaxParticipants(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                eventCreateViewModel.setEventMaxParticipants(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        eventCreateViewModel.getEventMaxParticipants().observe(getViewLifecycleOwner(), maxParticipants -> {
            eventNumParticipants = maxParticipants;
        });

        eventDateButton = rootView.findViewById(R.id.event_date);

        // event date
        eventDateButton.setOnClickListener(v -> {
            showDatePickerDialog(eventCreateViewModel.getEventDate(), this::updateEventDate);
        });
        // Observe the event date changes and update UI
        eventCreateViewModel.getEventDate().observe(getViewLifecycleOwner(), this::updateEventDate);

        // event time
        eventTimeButton = rootView.findViewById(R.id.event_time);
        eventTimeButton.setOnClickListener(v -> {
            showTimePickerDialog(eventCreateViewModel.getEventTime(), this::updateEventTime);
        });
        eventCreateViewModel.getEventTime().observe(getViewLifecycleOwner(), this::updateEventTime);

        // event registration date
        eventLastRegisterDateButton = rootView.findViewById(R.id.event_register_date);
        eventLastRegisterDateButton.setOnClickListener(v -> {
            showDatePickerDialog(eventCreateViewModel.getEventLastRegistrationDate(), this::updateEventRegisterDate);
        });
        // Observe the event registration date changes and update UI
        eventCreateViewModel.getEventLastRegistrationDate().observe(getViewLifecycleOwner(), this::updateEventRegisterDate);

        // event registration time
        eventLastRegisterTimeButton = rootView.findViewById(R.id.event_register_time);
        eventLastRegisterTimeButton.setOnClickListener(v -> showTimePickerDialog(eventCreateViewModel.getEventLastRegistrationTime(), this::updateLastRegisterTime));
        // Observe the event registration time changes and update UI
        eventCreateViewModel.getEventLastRegistrationTime().observe(getViewLifecycleOwner(), this::updateLastRegisterTime);

        // languages
        eventLanguagesView = rootView.findViewById(R.id.event_language);
        eventLanguagesView.setOnClickListener(v -> {
            showLanguageDialog();
        });
        eventCreateViewModel.getSelectedLanguages().observe(getViewLifecycleOwner(), languages -> {
            eventLanguagesView.setText(languages);
            eventLanguages = languages;
        });

        // create button
        Button createButton = rootView.findViewById(R.id.post);
        createButton.setOnClickListener(v -> {
            onCreateEventButton();


//                                    //Toast.makeText(EventCreate.this, "Event created successfully.", Toast.LENGTH_SHORT).show();
//                                    // Link to the createSuccessful page
//                                    Intent intent = new Intent(v.getContext(), CreateSuccessful.class);
//                                    startActivity(intent);
//                                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
//                                    transaction.remove(EventCreateActivity.this);
//                                    transaction.commit();
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Add a touch listener to hide the keyboard when tapping on a blank space
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });
    }

    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(requireActivity().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void onCreateEventButton() {
        Log.d("EventCreateActivity Testing", "Create Event button clicked");

        UserLocalDataSource userLocalDataSource = new UserLocalDataSource(getContext());
        String hostId = userLocalDataSource.getUserId();
        Log.d("EventCreateActivity Testing", "hostId: " + hostId);

        eventCreateViewModel.createEvent(eventThumbnailFilePath, hostId, selectedEventType, eventName, eventDescription, eventDate, eventTime, eventDuration, eventLocation, eventLongitude, eventLatitude, eventLanguages, String.valueOf(eventNumParticipants), String.valueOf(eventPrice), eventLastRegisterDate, eventLastRegisterTime);
        Log.d("EventCreate", eventLongitude + " " + eventLatitude);

    }

    private void showLanguageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Language")
                .setCancelable(false)
                .setMultiChoiceItems(eventCreateViewModel.getLanguageChoices(), eventCreateViewModel.getLanguageChoicesSelected(), (dialogInterface, i, b) -> {
                    eventCreateViewModel.setLanguageChoicesSelected(i, b);
                }).setPositiveButton("Select", (dialogInterface, i) -> {
                    eventCreateViewModel.updateSelectedLanguages();
                }).setNeutralButton("Clear All", (dialogInterface, i) -> {
                    eventCreateViewModel.clearSelectedLanguages();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
        builder.show();
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
            eventThumbnailImageView.setAdjustViewBounds(true);
            noPhotoText.setVisibility(View.VISIBLE);
            noPhotoText.setText("Don't like the previous photo?\n Try uploding another one!");
            eventThumbnailFilePath = null;
            uploadImgButton.setText("Upload event photo");
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

    private void showDatePickerDialog(@NonNull LiveData<Calendar> eventLiveDate, Consumer<Calendar> eventDateConsumer) {
        Calendar currentDate = eventLiveDate.getValue();
        if (currentDate == null) {
            currentDate = Calendar.getInstance();
        }
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (datePicker1, year1, monthOfYear, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year1, monthOfYear, dayOfMonth);
            ((MutableLiveData<Calendar>) eventLiveDate).postValue(selectedDate);
            eventDateConsumer.accept(selectedDate);
        }, year, month, day);
        // set the minimum date to today
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void updateEventDate(Calendar date) {
        if (date != null) {
            String newDate = DateTimeHelper.getFormattedDate(date);
            eventDate = newDate;
            eventDateButton.setText("Date: " + newDate);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateEventRegisterDate(Calendar date) {
        if (date != null) {
            String newDate = DateTimeHelper.getFormattedDate(date);
            eventLastRegisterDate = newDate;
            eventLastRegisterDateButton.setText("Date: " + newDate);
        }
    }

    private void showTimePickerDialog(@NonNull LiveData<Calendar> eventLiveTime, Consumer<Calendar> eventTimeConsumer) {
        Calendar currentTime = eventLiveTime.getValue();
        if (currentTime == null) {
            currentTime = Calendar.getInstance();
        }
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (timePicker, hourOfDay, minute1) -> {
            Calendar selectedTime = Calendar.getInstance();
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedTime.set(Calendar.MINUTE, minute1);
            ((MutableLiveData<Calendar>) eventLiveTime).postValue(selectedTime);
            eventTimeConsumer.accept(selectedTime);
        }, hour, minute, false);
        timePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void updateEventTime(Calendar calendar) {
        if (calendar != null) {
            String time = DateTimeHelper.getFormattedTime(calendar);
            eventTime = time;
            eventTimeButton.setText("Time: " + time);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateLastRegisterTime(Calendar calendar) {
        if (calendar != null) {
            String time = DateTimeHelper.getFormattedTime(calendar);
            eventLastRegisterTime = time;
            eventLastRegisterTimeButton.setText("Time: " + time);
        }
    }

    // Add this method inside EventCreateActivity class
    private void onPickLocationClick(View view) {
        // Get the location input from the TextView
        String locationInput = eventLocationText.getText().toString();


            // Create an intent to start MapActivity
            Intent mapIntent = new Intent(requireContext(), MapActivity.class);

            // Convert the location input to LatLng using geocoding (replace this with your actual geocoding logic)
            LatLng initialCoordinates = getLatLngFromLocationInput(locationInput);

            // Pass the initial coordinates to MapActivity
            mapIntent.putExtra("initialCoordinates", initialCoordinates);

            // Start MapActivity for result
            startActivityForResult(mapIntent, MAP_ACTIVITY_REQUEST_CODE);

    }

    // Handle the result from MapActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAP_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Retrieve the selected location and locationName from MapActivity
            LatLng selectedLocation = data.getParcelableExtra("selectedLocation");
            String locationName = data.getStringExtra("locationName");

            // Now you can use the selectedLocation and locationName as needed
            // For example, update UI or perform any other actions
            // ...
            // Log.d selectedLocation
            Log.d("EventCreateActivity", "selectedLocation: " + selectedLocation);
            Log.d("EventLocation", selectedLocation.longitude + " " + selectedLocation.latitude);
            // Update the eventLocationText with the selected location and locationName (optional)
            eventLongitude = selectedLocation.longitude;
            eventLatitude = selectedLocation.latitude;
            eventLocationText.setText(locationName);
            Log.d("Event Location", eventLongitude + " " + eventLatitude);
        }
    }
    // Define a constant for the request code
    private static final int MAP_ACTIVITY_REQUEST_CODE = 123;

    // Replace this method with your actual geocoding logic
    private LatLng getLatLngFromLocationInput(String locationInput) {
        // Placeholder coordinates (Seoul, South Korea) - Replace with actual logic
        return new LatLng(37.5665, 126.9780);
    }
}