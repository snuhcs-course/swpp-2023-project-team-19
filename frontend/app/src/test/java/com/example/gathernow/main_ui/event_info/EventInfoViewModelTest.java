package com.example.gathernow.main_ui.event_info;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.api.models.EventDataModelBuilder;
import com.example.gathernow.main_ui.CallbackInterface;
import com.example.gathernow.main_ui.EventRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EventInfoViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Spy
    @InjectMocks
    private EventRepository eventRepository;
    private EventInfoViewModel eventInfoViewModel;

    @Before
    public void setUp() {
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

    @Test
    public void testLoadEventInfoSuccessful() {
        // Mock data, assume user_id == host_id
        int eventId = 0;
        int userId = 0;

        // Mock the repository
        doAnswer(invocation -> {
            // Simulate a successful response
            ((CallbackInterface) invocation.getArgument(1)).onSuccess(mockEventDataResult());
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
        eventInfoViewModel.getEventData().removeObserver(eventDataModelObserver);
    }

    public void testDeleteApplication() {
    }
}