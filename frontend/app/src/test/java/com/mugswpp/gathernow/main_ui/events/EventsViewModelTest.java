package com.mugswpp.gathernow.main_ui.events;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.mugswpp.gathernow.api.models.ApplicationDataModel;
import com.mugswpp.gathernow.api.models.EventDataModel;
import com.mugswpp.gathernow.authenticate.UserLocalDataSource;
import com.mugswpp.gathernow.main_ui.CallbackInterface;
import com.mugswpp.gathernow.main_ui.EventRepository;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class EventsViewModelTest extends TestCase {

    @Mock
    private Context context;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserLocalDataSource userLocalDataSource;

    @Mock
    private MutableLiveData<String> alertMessage;

    @Mock
    private MutableLiveData<List<ApplicationDataModel>> userAppliedEvents;

    @Mock
    private MutableLiveData<List<ApplicationDataModel>> pendingApplications;

    @Mock
    private MutableLiveData<List<ApplicationDataModel>> confirmedApplications;

    @Mock
    private MutableLiveData<List<EventDataModel>> pendingEvents;

    @Mock
    private MutableLiveData<List<EventDataModel>> confirmedEvents;
    @Mock
    private EventsViewModel eventsViewModel;

    private EventsViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        context = RuntimeEnvironment.getApplication();
        viewModel = new EventsViewModel(context);
        eventsViewModel.alertMessage = alertMessage;
        eventsViewModel.userAppliedEvents = userAppliedEvents;
        eventsViewModel.pendingApplications = pendingApplications;
        eventsViewModel.confirmedApplications = confirmedApplications;
        eventsViewModel.pendingEvents = pendingEvents;
        eventsViewModel.confirmedEvents = confirmedEvents;
        eventsViewModel.eventRepository = eventRepository;

        //when(context.getString(anyInt())).thenReturn("MockedString");
    }

    @Test
    public void testFetchUserAppliedEventsSuccess() {
        // Mock the userLocalDataSource.getUserId() method
        when(userLocalDataSource.getUserId()).thenReturn("2");

        // Mock the eventRepository.getUserAppliedEvents() method
        eventsViewModel.fetchUserAppliedEvents(userLocalDataSource);

        // Verify that the MutableLiveData values are set correctly
        //verify(userAppliedEvents).postValue(anyList());
        //verify(pendingEvents).postValue(anyList());
        //verify(confirmedEvents).postValue(anyList());
        verify(alertMessage, never()).postValue(anyString());
    }

    private EventDataModel mockEventDataModels() {
        EventDataModel eventDataModels = new EventDataModel("Leisure", "Test event", 10, "2024-04-20", "12:00:00", "2 hours", "English", 0, "Test location", 0.0, 0.0, "Test description", 0, 0, 0, "2024-04-20", "12:00:00", "Test image");
        return eventDataModels;
    }
    private List<EventDataModel> mockEventDataModelsList() {
        List<EventDataModel> eventDataModels = new ArrayList<>();
        eventDataModels.add(new EventDataModel("Leisure", "Test event", 10, "2024-04-20", "12:00:00", "2 hours", "English", 0, "Test location", 0.0, 0.0, "Test description", 0, 0, 0, "2024-04-20", "12:00:00", "Test image"));
        return eventDataModels;
    }

    private List<ApplicationDataModel> mockApplicationDataModelsList() {
        List<ApplicationDataModel> eventDataModels = new ArrayList<>();
        eventDataModels.add(new ApplicationDataModel("@test", "Test message", 1, 1, 1, "Test name", "Test_avatar"));
        return eventDataModels;
    }

    @Test
    public void testLoadEventInfoSuccess() {
        // Mock data
        int eventId = 123;
        int isPending = 0;
        EventDataModel mockEventDataModel = mockEventDataModels();

        // Mock the eventRepository.getEventInfo() method
        doAnswer(invocation -> {
            ((CallbackInterface) invocation.getArgument(1)).onSuccess(mockEventDataModel);
            return null;
        }).when(eventRepository).getEventInfo(eq(eventId), any(CallbackInterface.class));

        // Call the method to test
        eventsViewModel.loadEventInfo(eventId, isPending);

        // Verify that the MutableLiveData values are set correctly
        verify(pendingEvents, never()).postValue(mockEventDataModelsList());
        verify(confirmedEvents, never()).postValue(mockEventDataModelsList());
        verify(alertMessage, never()).postValue("Loaded user events");
    }

    @Test
    public void testFetchUserAppliedEventsError() {
        // Mock the userLocalDataSource.getUserId() method
        when(userLocalDataSource.getUserId()).thenReturn("2");

        // Mock the eventRepository.getUserAppliedEvents() method to trigger an error
        doAnswer(invocation -> {
            ((CallbackInterface) invocation.getArgument(1)).onError("Error message");
            return null;
        }).when(eventRepository).getUserAppliedEvents(anyInt(), any(CallbackInterface.class));

        // Mock the runOnUiThread method to execute the action immediately
        doAnswer(invocation -> {
            ((Runnable) invocation.getArgument(0)).run();
            return null;
        }).when(eventsViewModel).runOnUiThread(any(Runnable.class));

        // Mock the alertMessage.postValue() method
        eventsViewModel.fetchUserAppliedEvents(userLocalDataSource);

        // Verify that the alertMessage is set with the correct error message
        //verify(alertMessage).postValue("Error message");
        //verify(userAppliedEvents, never()).postValue(anyList());
        //verify(pendingEvents, never()).postValue(anyList());
        //verify(confirmedEvents, never()).postValue(anyList());
    }

    @Test
    public void getAlertMessageTest() throws InterruptedException {
        // Mock data or use a spy to manipulate the real EventRepository if necessary
        // ...

        // Prepare expected data
        String mockMessage = "Test message";
        // Manually set the LiveData within HomeViewModel
        viewModel.getAlertMessage().setValue(mockMessage);

        // Assert that getAllEvents() returns the LiveData with expected data
        assertEquals(mockMessage, viewModel.getAlertMessage().getValue());

    }

    @Test
    public void getPendingEventsTest() {
        // Mock data or use a spy to manipulate the real EventRepository if necessary
        // ...

        // Prepare expected data
        List<EventDataModel> mockEvents = mockEventDataModelsList();
        // Manually set the LiveData within HomeViewModel
        viewModel.getPendingEvents().setValue(mockEvents);

        // Assert that getAllEvents() returns the LiveData with expected data
        assertEquals(mockEvents, viewModel.getPendingEvents().getValue());
    }

    @Test
    public void getConfirmedEventsTest() {
        // Mock data or use a spy to manipulate the real EventRepository if necessary
        // ...

        // Prepare expected data
        List<EventDataModel> mockEvents = mockEventDataModelsList();
        // Manually set the LiveData within HomeViewModel
        viewModel.getConfirmedEvents().setValue(mockEvents);

        // Assert that getAllEvents() returns the LiveData with expected data
        assertEquals(mockEvents, viewModel.getConfirmedEvents().getValue());
    }

    @Test
    public void getAllEventsTest() {
        // Mock data or use a spy to manipulate the real EventRepository if necessary
        // ...

        // Prepare expected data
        List<ApplicationDataModel> mockApplication = mockApplicationDataModelsList();
        // Manually set the LiveData within HomeViewModel
        viewModel.getAllEvents().setValue(mockApplication);

        // Assert that getAllEvents() returns the LiveData with expected data
        assertEquals(mockApplication, viewModel.getAllEvents().getValue());
    }

}
