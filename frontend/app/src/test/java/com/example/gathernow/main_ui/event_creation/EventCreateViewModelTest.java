package com.example.gathernow.main_ui.event_creation;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.gathernow.main_ui.CallbackInterface;
import com.example.gathernow.main_ui.EventDataSource;
import com.example.gathernow.main_ui.EventRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class EventCreateViewModelTest {
    @Mock
    private Context mockContext;

    private EventCreateViewModel eventCreateViewModel;

    @Spy
    @InjectMocks
    private EventRepository eventRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        eventCreateViewModel = new EventCreateViewModel(mockContext, eventRepository);
//        eventCreateViewModel.getAlertMessage().observeForever(observer);
    }

    @Test
    public void createEventWithSuccess() {
        // Given: Mock data
        String thumbnailFilePath = "path";
        String creator = "creator";
        String type = "Leisure";
        String name = "name";
        String description = "description";
        String date = "2023-01-01";
        String time = "10:00:00";
        String duration = "duration";
        String location = "location";
        Double event_longitude = 0.0;
        Double event_latitude = 0.0;
        String languages = "languages";
        String maxParticipants = "10";
        String price = "0";
        String lastRegisterDate = "2023-01-01";
        String lastRegisterTime = "9:00:00";

        String expectedSuccessMessage = "Event created successfully";

        // Mock the repository
        doAnswer(invocation -> {
            CallbackInterface callback = invocation.getArgument(16);
            callback.onSuccess(expectedSuccessMessage);
            return null;
        }).when(eventRepository).createEvent(
                Mockito.eq(thumbnailFilePath),
                Mockito.eq(creator),
                Mockito.eq(type),
                Mockito.eq(name),
                Mockito.eq(description),
                Mockito.eq(date),
                Mockito.eq(time),
                Mockito.eq(duration),
                Mockito.eq(location),
                Mockito.eq(event_longitude),
                Mockito.eq(event_latitude),
                Mockito.eq(languages),
                Mockito.eq(maxParticipants),
                Mockito.eq(price),
                Mockito.eq(lastRegisterDate),
                Mockito.eq(lastRegisterTime),
                Mockito.any(CallbackInterface.class)
        );

        Observer<String> alertMessageObserver = alertMessage -> {
            System.out.println("onChanged success: " + alertMessage);
            assertEquals(expectedSuccessMessage, alertMessage);
        };

        eventCreateViewModel.getAlertMessage().observeForever(alertMessageObserver);


        // When: Trigger the createEvent method
        eventCreateViewModel.createEvent(thumbnailFilePath, creator, type, name, description, date, time, duration, location, event_longitude, event_latitude, languages, maxParticipants, price, lastRegisterDate, lastRegisterTime);

        // Verify that the repository method is called
        verify(eventRepository).createEvent(
                Mockito.eq(thumbnailFilePath),
                Mockito.eq(creator),
                Mockito.eq(type),
                Mockito.eq(name),
                Mockito.eq(description),
                Mockito.eq(date),
                Mockito.eq(time),
                Mockito.eq(duration),
                Mockito.eq(location),
                Mockito.eq(event_longitude),
                Mockito.eq(event_latitude),
                Mockito.eq(languages),
                Mockito.eq(maxParticipants),
                Mockito.eq(price),
                Mockito.eq(lastRegisterDate),
                Mockito.eq(lastRegisterTime),
                Mockito.any(CallbackInterface.class)
        );
    }

    @Test
    public void createEventWithLessInputs() {
        // Mock invalid inputs
        String thumbnailFilePath = "path";
        String creator = "creator";
        String type = "Leisure";
        String name = "name";
        String description = "description";
        String date = "2023-01-01";
        String time = "10:00:00";
        String duration = "duration";
        String location = "";
        Double event_longitude = 0.0;
        Double event_latitude = 0.0;
        String languages = "languages";
        String maxParticipants = "10";
        String price = "0";
        String lastRegisterDate = "2023-01-01";
        String lastRegisterTime = "9:00:00";

        String expectedErrorMessage = "Please fill in all required fields";
        Observer<String> alertMessageObserver = alertMessage -> {
            System.out.println("onChanged success: " + alertMessage);
            assertEquals(expectedErrorMessage, alertMessage);
        };

        eventCreateViewModel.getAlertMessage().observeForever(alertMessageObserver);

        // When: Trigger the createEvent method
        eventCreateViewModel.createEvent(thumbnailFilePath, creator, type, name, description, date, time, duration, location, event_longitude, event_latitude, languages, maxParticipants, price, lastRegisterDate, lastRegisterTime);

        // Verify that the repository method is not called
        verify(eventRepository, Mockito.never()).createEvent(
                Mockito.eq(thumbnailFilePath),
                Mockito.eq(creator),
                Mockito.eq(type),
                Mockito.eq(name),
                Mockito.eq(description),
                Mockito.eq(date),
                Mockito.eq(time),
                Mockito.eq(duration),
                Mockito.eq(location),
                Mockito.eq(event_longitude),
                Mockito.eq(event_latitude),
                Mockito.eq(languages),
                Mockito.eq(maxParticipants),
                Mockito.eq(price),
                Mockito.eq(lastRegisterDate),
                Mockito.eq(lastRegisterTime),
                Mockito.any(CallbackInterface.class)
        );

    }

    @Test
    public void setEventPriceValid() {
        // Given: Mock data
        String price = "10";

        // When: Trigger the setEventPrice method
        eventCreateViewModel.setEventPrice(price);

        // Then: The eventPrice is set
        Observer<Integer> eventPriceObserver = eventPrice -> {
            System.out.println("onChanged success: " + eventPrice);
            assertEquals(10, eventPrice.intValue());
        };

        eventCreateViewModel.getEventPrice().observeForever(eventPriceObserver);
    }

    @Test
    public void setEventPriceInvalid() {
        // Given: Mock data
        String price = "invalid";

        // When: Trigger the setEventPrice method
        eventCreateViewModel.setEventPrice(price);

        // Then: The eventPrice is set
        Observer<Integer> eventPriceObserver = eventPrice -> {
            System.out.println("onChanged success: " + eventPrice);
            assertEquals(0, eventPrice.intValue());
        };

        eventCreateViewModel.getEventPrice().observeForever(eventPriceObserver);

        Observer<String> alertMessageObserver = alertMessage -> {
            System.out.println("onChanged success: " + alertMessage);
            assertEquals("Please enter a valid price", alertMessage);
        };
        eventCreateViewModel.getAlertMessage().observeForever(alertMessageObserver);
    }

    @Test
    public void setEventMaxParticipantsValid() {
        // Given: Mock data
        String maxParticipants = "10";

        // When: Trigger the setEventMaxParticipants method
        eventCreateViewModel.setEventMaxParticipants(maxParticipants);

        // Then: The eventMaxParticipants is set
        Observer<Integer> eventMaxParticipantsObserver = eventMaxParticipants -> {
            System.out.println("onChanged success: " + eventMaxParticipants);
            assertEquals(10, eventMaxParticipants.intValue());
        };

        eventCreateViewModel.getEventMaxParticipants().observeForever(eventMaxParticipantsObserver);
    }

    @Test
    public void setEventMaxParticipantsInvalid() {
        // Given: Mock data
        String maxParticipants = "0";

        // When: Trigger the setEventMaxParticipants method
        eventCreateViewModel.setEventMaxParticipants(maxParticipants);

        // Then: The eventMaxParticipants is set
        Observer<Integer> eventMaxParticipantsObserver = eventMaxParticipants -> {
            System.out.println("onChanged success: " + eventMaxParticipants);
            assertEquals(0, eventMaxParticipants.intValue());
        };

        eventCreateViewModel.getEventMaxParticipants().observeForever(eventMaxParticipantsObserver);

        Observer<String> alertMessageObserver = alertMessage -> {
            System.out.println("onChanged success: " + alertMessage);
            assertEquals("Please enter a valid number of participants", alertMessage);
        };
        eventCreateViewModel.getAlertMessage().observeForever(alertMessageObserver);
    }



}