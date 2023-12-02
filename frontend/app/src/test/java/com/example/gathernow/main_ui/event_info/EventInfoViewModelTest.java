package com.example.gathernow.main_ui.event_info;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.gathernow.api.models.ApplicationDataModel;
import com.example.gathernow.api.models.ApplicationDataModelBuilder;
import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.api.models.EventDataModelBuilder;
import com.example.gathernow.api.models.UserDataModel;
import com.example.gathernow.api.models.UserDataModelBuilder;
import com.example.gathernow.main_ui.CallbackInterface;
import com.example.gathernow.main_ui.EventRepository;
import com.example.gathernow.main_ui.UserRemoteDataSource;
import com.example.gathernow.main_ui.UserRemoteRepository;

import org.junit.Before;
import org.junit.Ignore;
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
    @Mock
    private UserRemoteRepository userRemoteRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private MutableLiveData<EventDataModel> eventData;

    @Before
    public void setUp() {
//        MockitoAnnotations.openMocks(this);
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

    @Ignore("TODO")
    public void testLoadHostInfo() {
        int hostId = 2;

        // Mock the repository
        doAnswer(invocation -> {
            // Simulate a successful response
            ((CallbackInterface) invocation.getArgument(1)).onSuccess(mockUserDataResult());
            System.out.println("Mock the repository response");
            return null;
        }).when(userRemoteRepository).getUserInfo(eq(hostId), any(CallbackInterface.class));

        // When: Trigger the loadHostInfo method
        eventInfoViewModel.loadHostInfo(hostId);
        verify(userRemoteRepository, times(1)).getUserInfo(eq(hostId), any(CallbackInterface.class));

        Observer<UserDataModel> userDataModelObserver = userDataModel -> {
            // Then: Assert that the data is correct
            System.out.println("Observe userData: " + userDataModel.getUserId());
            assertNotNull(userDataModel);
            // Print out the userData to see what it looks like
            System.out.println("Observe userData: " + userDataModel.getUserId());
        };

        eventInfoViewModel.getHostData().observeForever(userDataModelObserver);

        // Then: Verify that the repository is called
//        System.out.println("Finished loadHostInfo: "+ eventInfoViewModel.getHostData().getValue().getName());
//        eventInfoViewModel.getHostData().removeObserver(userDataModelObserver);


    }

    @Test
    public void testButtonVisibility_Host() {
        // Mock data, assume user_id == host_id
        int hostId = 0;
        int userId = 0;
        int eventId = 0;

        // When: Trigger setButtonVisibility method
        eventInfoViewModel.setButtonVisibility(hostId, userId, eventId);

        // Observer
        Observer<Boolean> showViewApplicantButtonObserver = showViewApplicantButton -> {
            // Then: Assert that the data is correct
            System.out.println("Observe showViewApplicantButton: " + showViewApplicantButton);
            assert(showViewApplicantButton);
        };

        Observer<Boolean> showDeleteButtonObserver = showDeleteButton -> {
            // Then: Assert that the data is correct
            System.out.println("Observe showDeleteButton: " + showDeleteButton);
            assert(showDeleteButton);
        };

        eventInfoViewModel.getShowViewApplicantsButton().observeForever(showViewApplicantButtonObserver);
        eventInfoViewModel.getShowDeleteEventButton().observeForever(showDeleteButtonObserver);

        // Then: Verify that the repository is not called
        verify(eventRepository, never()).checkUserAppliedEvent(eq(eventId), eq(userId), any(CallbackInterface.class));

    }

    private ApplicationDataModel mockNotAppliedApplicationResult() {
        return new ApplicationDataModelBuilder()
                .setApplicationId(0)
                .setEventId(0)
                .setHostId(0)
                .setApplicantId(1)
                .setMessage("Test Message")
                .setRequestStatus(0)
                .build();
    }

    @Test
    public void testButtonVisibility_Participant_NotApplied_DeadlineNotPassed() {
        // Mock data, assume user_id == host_id
        int hostId = 0;
        int userId = 1;
        int eventId = 0;

        when(eventData.getValue()).thenReturn(mockEventDataResult());

        // Mock the repository response
        doAnswer(invocation -> {
            // Simulate a successful response
            ((CallbackInterface) invocation.getArgument(2)).onSuccess(mockNotAppliedApplicationResult());
            System.out.println("Mock the repository response");
            return null;
        }).when(eventRepository).checkUserAppliedEvent(eq(eventId), eq(userId), any(CallbackInterface.class));


        // When: Trigger setButtonVisibility method
        eventInfoViewModel.setButtonVisibility(userId, hostId, eventId);
        // Then: Verify that the repository is called
        verify(eventRepository).checkUserAppliedEvent(eq(eventId), eq(userId), any(CallbackInterface.class));

        // Observer
        Observer<Boolean> showResultButtonObserver = showResultButton -> {
            // Then: Assert that the data is correct
            System.out.println("Observe showResultButton: " + showResultButton);
            assert(showResultButton);
        };

        Observer<Boolean> showCancelButtonObserver = showCancelButton -> {
            // Then: Assert that the data is correct
            System.out.println("Observe showCancelButton: " + showCancelButton);
            assert(showCancelButton);
        };

        Observer<String> applicationStatusObserver = applicationStatus -> {
            // Then: Assert that the data is correct
            System.out.println("Observe applicationStatus: " + applicationStatus);
            assert(applicationStatus.equals("PENDING"));
        };

        eventInfoViewModel.getShowResultButton().observeForever(showResultButtonObserver);
        eventInfoViewModel.getShowCancelRegButton().observeForever(showCancelButtonObserver);
        eventInfoViewModel.getApplicationStatus().observeForever(applicationStatusObserver);
    }

    private ApplicationDataModel mockAppliedApplicationResult() {
        return new ApplicationDataModelBuilder()
                .setApplicationId(0)
                .setEventId(0)
                .setHostId(0)
                .setApplicantId(1)
                .setMessage("Test Message")
                .setRequestStatus(1)
                .build();
    }

    @Test
    public void testButtonVisibility_Participant_Applied_DeadlineNotPassed() {
        // Mock data, assume user_id == host_id
        int hostId = 0;
        int userId = 1;
        int eventId = 0;

        when(eventData.getValue()).thenReturn(mockEventDataResult());

        // Mock the repository response
        doAnswer(invocation -> {
            // Simulate a successful response
            ((CallbackInterface) invocation.getArgument(2)).onSuccess(mockAppliedApplicationResult());
            System.out.println("Mock the repository response");
            return null;
        }).when(eventRepository).checkUserAppliedEvent(eq(eventId), eq(userId), any(CallbackInterface.class));
    }

    @Test
    public void testDeleteApplication() {
    }
}