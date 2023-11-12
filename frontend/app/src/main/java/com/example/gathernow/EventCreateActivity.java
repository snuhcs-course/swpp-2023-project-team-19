package com.example.gathernow;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gathernow.api.CodeMessageResponse;
import com.example.gathernow.api.RetrofitClient;
import com.example.gathernow.api.ServiceApi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
    private ServiceApi service;
    private boolean imageUploaded = false;
    private String eventThumbnailFilePath = null;
    private File eventThumbnailFile;

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
    }

    private TextView eventNameText;
    private TextView eventDurationText;
    private TextView eventPriceText;
    private TextView eventDescriptionText;
    private TextView eventLocationText;
    private TextView eventNumParticipantsText;
    private ActivityResultLauncher<PickVisualMediaRequest> pickProfilePic;

    private String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_id", null); // Return null if the user_id doesn't exist
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_create, container, false);
        ImageView eventImage = rootView.findViewById(R.id.event_image);

        // TODO: upload image
        Button uploadImgButton = rootView.findViewById(R.id.event_image_upload);
        pickProfilePic = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Log.d("EventCreateActivity Testing", "Profile picture selected");
                eventImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                eventImage.setImageURI(uri);
                // Load picture from uri
                InputStream inputStream;
                Context context = rootView.getContext();
                File outputFile = new File(context.getFilesDir(), "event_thumb.jpg");
                try {
                    inputStream = context.getContentResolver().openInputStream(uri);
                    FileOutputStream outputStream = new FileOutputStream(outputFile);
                    Bitmap selectedImgBitmap = getRotatedImage(inputStream);

                    if (selectedImgBitmap != null) {
                        // Compress bitmap
                        selectedImgBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
                        outputStream.close();
                        eventThumbnailFilePath = outputFile.getPath();
                        Log.d("EventCreateActivity Testing", "Thumbnail saved to: " + eventThumbnailFilePath);
                    }
                    imageUploaded = true;
                    uploadImgButton.setText("Delete");
                } catch (IOException e) {
                    Log.e("EventCreateActivity Testing", "Error when uploading image");
                    e.printStackTrace();
                }
            }
        });
        uploadImgButton.setOnClickListener(v -> {
            if (!imageUploaded) {
                Log.d("EventCreateActivity Testing", "Upload image button clicked");
                pickProfilePic.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
            } else {
                Log.d("EventCreateActivity Testing", "Delete img");
                // TODO: Delete selected image
                eventImage.setImageResource(R.mipmap.ic_sad_frog_foreground);
                eventImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                eventThumbnailFilePath = null;
                uploadImgButton.setText("Upload");
                imageUploaded = false;
            }
        });

        // TODO: event type
        TextView eventType = rootView.findViewById(R.id.event_type);
        AtomicReference<String> eventTypeIn = new AtomicReference<>("");
        final int[] eventTypeInputIdx = {0};
        String[] typeChoices = {"Leisure", "Sports", "Workshops", "Parties", "Cultural activities", "Others"};
        eventType.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Select Event Type")
                    .setCancelable(false)
                    .setSingleChoiceItems(typeChoices, -1, (dialogInterface, i) -> {
                        eventType.setText(typeChoices[i]);
                        eventTypeInputIdx[0] = i + 1;
                    }).setPositiveButton("Select", (dialogInterface, i) -> {
                        i = eventTypeInputIdx[0];
                        if (i == 1) {
                            eventTypeIn.set("Leisure");
                        } else if (i == 2) {
                            eventTypeIn.set("Sports");
                        } else if (i == 3) {
                            eventTypeIn.set("Workshops");
                        } else if (i == 4) {
                            eventTypeIn.set("Parties");
                        } else if (i == 5) {
                            eventTypeIn.set("Cultural activities");
                        } else if (i == 6) {
                            eventTypeIn.set("Others");
                        }
                        Log.d("EventCreateActivity Testing", "Event type: " + eventTypeIn.get());
                    }).setNegativeButton("Cancel", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });
            builder.show();
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

        Button event_date = rootView.findViewById(R.id.event_date);
        Button event_time = rootView.findViewById(R.id.event_time);
        Button event_reg_date = rootView.findViewById(R.id.event_register_date);
        Button event_reg_time = rootView.findViewById(R.id.event_register_time);

        // event date
        event_date.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // TODO: date
                    event_year_input[0] = year;
                    event_month_input[0] = monthOfYear;
                    event_date_input[0] = dayOfMonth;
                    event_date.setText("Date: " + String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year));
                }
            }, year, month, day);
            // set the minimum date to today
            datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.show();
        });

        // event time
        event_time.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minutes = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hour1, mins) -> {
                // TODO: time
                event_hour_input[0] = hour1;
                event_min_input[0] = mins;
                String formattedHour = String.format("%02d", hour1);
                String formattedMins = String.format("%02d", mins);
                event_time.setText("Time: " + formattedHour + ":" + formattedMins);
                //event_time.setText("Time: " + String.valueOf(hour) + ":" + String.valueOf(mins));
            }, hour, minutes, false);
            timePickerDialog.show();
        });

        // event registration date
        event_reg_date.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, monthOfYear, dayOfMonth) -> {
                // TODO: date
                event_reg_year_input[0] = year1;
                event_reg_month_input[0] = monthOfYear;
                event_reg_date_input[0] = dayOfMonth;
                event_reg_date.setText("Date: " + String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year1));
            }, year, month, day);
            // set the minimum date to today
            datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.show();
        });

        // event registration time
        event_reg_time.setOnClickListener(v -> {
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
                    event_reg_time.setText("Time: " + formattedHour + ":" + formattedMins);
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

        languageTextView.setOnClickListener(view ->
        {

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
                Integer host_id = Integer.valueOf(getUserId(getActivity()));

                // Thumbnail
                MultipartBody.Part thumbnailPart = null;
                if (eventThumbnailFilePath != null) {
                    eventThumbnailFile = new File(eventThumbnailFilePath);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), eventThumbnailFile);
                    thumbnailPart = MultipartBody.Part.createFormData("event_images", eventThumbnailFile.getName(), requestFile);
                }

                RequestBody eventTypeBody = RequestBody.create(MediaType.parse("text/plain"), eventTypeIn.get());
                RequestBody eventNameBody = RequestBody.create(MediaType.parse("text/plain"), event_name);
                RequestBody eventNumParticipantsBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(event_num_participants));
                RequestBody eventDateBody = RequestBody.create(MediaType.parse("text/plain"), event_date1);
                RequestBody eventTimeBody = RequestBody.create(MediaType.parse("text/plain"), event_time1);
                RequestBody eventDurationBody = RequestBody.create(MediaType.parse("text/plain"), event_duration);
                RequestBody eventLanguageBody = RequestBody.create(MediaType.parse("text/plain"), event_language);
                RequestBody eventPriceBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(event_price));
                RequestBody eventLocationBody = RequestBody.create(MediaType.parse("text/plain"), event_location);
                RequestBody eventDescriptionBody = RequestBody.create(MediaType.parse("text/plain"), event_description);
                RequestBody eventNumJoinedBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(event_num_joined));
                RequestBody hostIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(host_id));
                RequestBody eventRegDateBody = RequestBody.create(MediaType.parse("text/plain"), event_reg_date1);
                RequestBody eventRegTimeBody = RequestBody.create(MediaType.parse("text/plain"), event_reg_time1);

                service = RetrofitClient.getClient().create(ServiceApi.class);
//                EventData requestData = new EventData(eventTypeIn.get(), event_name, event_num_participants, event_date1, event_time1, event_duration, event_language, event_price, event_location, event_description, event_num_joined, host_id, event_reg_date1, event_reg_time1);
                Call<CodeMessageResponse> call = service.eventlist(thumbnailPart, hostIdBody, eventTypeBody, eventNameBody, eventNumParticipantsBody, eventDateBody, eventTimeBody, eventDurationBody, eventLanguageBody, eventPriceBody, eventLocationBody, eventDescriptionBody, eventNumJoinedBody, eventRegDateBody, eventRegTimeBody);
                call.enqueue(new Callback<CodeMessageResponse>() {
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
                                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.remove(EventCreateActivity.this);
                                    transaction.commit();
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

    private Bitmap getRotatedImage(InputStream inputStream) {
        // Clone the input stream because inputStream can only be read once
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream inputStreamClone;
        InputStream inputStreamClone2;
        try {
            inputStream.transferTo(baos);
            inputStreamClone = new ByteArrayInputStream(baos.toByteArray());
            inputStreamClone2 = new ByteArrayInputStream(baos.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("SignUpActivity Testing", "IO Exception when cloning the input stream");
            return null;
        }

        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(inputStreamClone);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("SignUpActivity Testing", "IO Exception when reading exif");
        }
        if (exifInterface != null) {
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degrees = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degrees = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degrees = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degrees = 270;
                    break;
            }
            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);
            Bitmap selectedImgBitmap = BitmapFactory.decodeStream(inputStreamClone2);
            return Bitmap.createBitmap(selectedImgBitmap, 0, 0, selectedImgBitmap.getWidth(), selectedImgBitmap.getHeight(), matrix, true);

        }
        return BitmapFactory.decodeStream(inputStream);
    }


}