package com.example.gathernow.main_ui.event_info;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.api.models.EventDataModelBuilder;
import com.example.gathernow.api.models.UserDataModel;
import com.example.gathernow.api.models.UserDataModelBuilder;
import com.example.gathernow.main_ui.CallbackInterface;
import com.example.gathernow.main_ui.EventRepository;
import com.example.gathernow.main_ui.UserRemoteDataSource;
import com.example.gathernow.main_ui.UserRemoteRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EventInfoViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private EventInfoViewModel eventInfoViewModel;
    @Spy
    @InjectMocks
    private UserRemoteRepository userRemoteRepository;
    @Spy
    @InjectMocks
    private EventRepository eventRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        eventInfoViewModel = new EventInfoViewModel(eventRepository);
    }

    private EventDataModel mockEventDataResult() {
        return new EventDataModelBuilder()
                .setEventId(0)
                .setEventType("Leisure")
                .setEventTitle("Test Event")
                .setEventMaxParticipants(10)
                .setEventJoined(0)
                .setEventDate("2024-04-20")
                .setEventTime("12:00:00")
                .setEventDuration("2 hours")
                .setEventLanguage("English")
                .setEventPrice(0)
                .setEventLocation("Test Location")
                .setEventLongitude(0.0)
                .setEventLatitude(0.0)
                .setEventDescription("Test description")
                .setHostId(0)
                .setHostId(0)
                .setEventRegisterDate("2024-04-20")
                .setEventRegisterTime("12:00:00")
                .setEventImages("Test Image")
                .build();
    }

    private UserDataModel mockUserDataResult() {
        return new UserDataModelBuilder()
                .setUserId(2)
                .setName("Kiwi")
                .setEmail("kiwi@gmail.com")
                .setCreatedAt("2023-11-27 16:22:41.734874")
                .setAvatar("avatar_image/picture10.png")
                .build();
    }

    @Test
    public void testLoadEventInfoSuccessful() {
        // Mock data, assume user_id == host_id
        int eventId = 0;
        int userId = 0;

        // Mock the repository
        doAnswer(invocation -> {
            // Simulate a successful response
            ((CallbackInterface) invocation.getArgument(1)).onSuccess(mockEventDataResult());
            System.out.println("Mock the repository response");
            return null;
        }).when(eventRepository).getEventInfo(eq(eventId), any(CallbackInterface.class));

        Observer<EventDataModel> eventDataModelObserver = eventInfo -> {
            // Then: Assert that the data is correct
            System.out.println("Observe eventInfo: " + eventInfo);
            assertNotNull(eventInfo);
            // Print out the eventInfo to see what it looks like
            System.out.println("Observe eventInfo: " + eventInfo.getEventId());
//            assertEquals(eventInfo, mockEventDataResult());
            assert(eventInfo.getEventId() == 0);
            assert(eventInfo.getEventType().equals("Leisure"));
            assert(eventInfo.getEventTitle().equals("Test Event"));
            assert(eventInfo.getEventMaxParticipants() == 10);
            assert(eventInfo.getEventJoined() == 0);
            assert(eventInfo.getEventDate().equals("2024-04-20"));
            assert(eventInfo.getEventTime().equals("12:00:00"));
            assert(eventInfo.getEventDuration().equals("2 hours"));
            assert(eventInfo.getEventLanguage().equals("English"));
            assert(eventInfo.getEventPrice() == 0);
            assert(eventInfo.getEventLocation().equals("Test Location"));
            assert(eventInfo.getEventLongitude() == 0.0);
            assert(eventInfo.getEventLatitude() == 0.0);
            assert(eventInfo.getEventDescription().equals("Test description"));
            assert(eventInfo.getHostId() == 0);
            assert(eventInfo.getEventRegisterDate().equals("2024-04-20"));
            assert(eventInfo.getEventRegisterTime().equals("12:00:00"));
            assert(eventInfo.getEventImages().equals("Test Image"));
        };

        eventInfoViewModel.getEventData().observeForever(eventDataModelObserver);

        // When: Trigger the loadEventInfo method
        eventInfoViewModel.loadEventInfo(eventId, userId);
        // Then: Verify that the repository is called
        verify(eventRepository, times(1)).getEventInfo(eq(eventId), any(CallbackInterface.class));
        eventInfoViewModel.getEventData().removeObserver(eventDataModelObserver);
    }

    @Test
    public void testLoadHostInfo() {
        int hostId = 2;

        // Mock the repository
        doAnswer(invocation -> {
            // Simulate a successful response
            ((CallbackInterface) invocation.getArgument(1)).onSuccess(mockUserDataResult());
            System.out.println("Mock the repository response");
            return null;
        }).when(userRemoteRepository).getUserInfo(eq(hostId), any(CallbackInterface.class));

        System.out.println("Finished mocking the repository response");
        Observer<UserDataModel> userDataModelObserver = userDataModel -> {
            // Then: Assert that the data is correct
            System.out.println("Observe userData: " + userDataModel.getUserId());
            assertNotNull(userDataModel);
            // Print out the userData to see what it looks like
            System.out.println("Observe userData: " + userDataModel.getUserId());
        };

        eventInfoViewModel.getHostData().observeForever(userDataModelObserver);

        // When: Trigger the loadHostInfo method
        eventInfoViewModel.loadHostInfo(hostId);
        // Then: Verify that the repository is called
//        verify(userRemoteRepository, times(1)).getUserInfo(eq(hostId), any(CallbackInterface.class));
//        System.out.println("Finished loadHostInfo: "+ eventInfoViewModel.getHostData().getValue().getName());
//        eventInfoViewModel.getHostData().removeObserver(userDataModelObserver);


    }

    @Test
    public void testDeleteApplication() {
    }
}