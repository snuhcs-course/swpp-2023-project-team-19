package com.mugswpp.gathernow.main_ui.event_info;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.mugswpp.gathernow.api.models.ApplicationDataModel;
import com.mugswpp.gathernow.api.models.ApplicationDataModelBuilder;
import com.mugswpp.gathernow.api.models.EventDataModel;
import com.mugswpp.gathernow.api.models.EventDataModelBuilder;
import com.mugswpp.gathernow.api.models.UserDataModel;
import com.mugswpp.gathernow.api.models.UserDataModelBuilder;
import com.mugswpp.gathernow.main_ui.CallbackInterface;
import com.mugswpp.gathernow.main_ui.EventRepository;
import com.mugswpp.gathernow.main_ui.UserRemoteRepository;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
    private Observer<EventDataModel> eventDataObserver;
    @Mock
    private Observer<Boolean> showDeleteApplicationSuccessObserver;
    @Mock
    private Observer<Boolean> showDeleteEventSuccessObserver;
    @Mock
    private Observer<Boolean> showViewApplicantsButtonObserver;
    @Mock
    private Observer<Boolean> showDeleteButtonObserver;
    @Mock
    private Observer<Boolean> showResultButtonObserver;
    @Mock
    private Observer<Boolean> showCancelButtonObserver;
    @Mock
    private Observer<Boolean> showRegisterButtonObserver;
    @Mock
    private Observer<String> applicationStatusObserver;
    @Mock
    private Observer<Boolean> clickableRegisterButtonObserver;
    @Mock private Observer<Boolean> clickableCancelButtonObserver;
    @Mock private Observer<UserDataModel> hostDataObserver;

    @Before
    public void setUp() {
//        MockitoAnnotations.openMocks(this);
        eventInfoViewModel = new EventInfoViewModel(eventRepository);
        eventInfoViewModel.getEventData().observeForever(eventDataObserver);
        eventInfoViewModel.getShowDeleteApplicationSuccess().observeForever(showDeleteApplicationSuccessObserver);
        eventInfoViewModel.getShowDeleteEventSuccess().observeForever(showDeleteEventSuccessObserver);
        eventInfoViewModel.getShowViewApplicantsButton().observeForever(showViewApplicantsButtonObserver);
        eventInfoViewModel.getShowDeleteEventButton().observeForever(showDeleteButtonObserver);
        eventInfoViewModel.getShowResultButton().observeForever(showResultButtonObserver);
        eventInfoViewModel.getShowCancelRegButton().observeForever(showCancelButtonObserver);
        eventInfoViewModel.getShowRegisterButton().observeForever(showRegisterButtonObserver);
        eventInfoViewModel.getApplicationStatus().observeForever(applicationStatusObserver);
        eventInfoViewModel.getClickableRegisterButton().observeForever(clickableRegisterButtonObserver);
        eventInfoViewModel.getClickableCancelButton().observeForever(clickableCancelButtonObserver);
        eventInfoViewModel.getHostData().observeForever(hostDataObserver);
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

    private EventDataModel mockEventData_DeadlinePassed() {
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
                .setEventRegisterDate("2023-04-20")
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
        EventDataModel fakeEventInfo = mockEventDataResult();

        // Mock the repository
        doAnswer(invocation -> {
            // Simulate a successful response
            ((CallbackInterface) invocation.getArgument(1)).onSuccess(fakeEventInfo);
            System.out.println("Mock the repository response");
            return null;
        }).when(eventRepository).getEventInfo(eq(eventId), any(CallbackInterface.class));

        // When: Trigger the loadEventInfo method
        eventInfoViewModel.loadEventInfo(eventId, userId);
        // Then: Verify that the repository is called
        verify(eventRepository).getEventInfo(eq(eventId), any(CallbackInterface.class));
        verify(eventDataObserver).onChanged(eq(fakeEventInfo));
//        eventInfoViewModel.getEventData().removeObserver(eventDataModelObserver);
    }

    @Ignore("Not working")
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
        assertNotNull(eventInfoViewModel.getHostData().getValue());

    }

    @Test
    public void testButtonVisibility_Host() {
        // Mock data, assume user_id == host_id
        int hostId = 0;
        int userId = 0;
        int eventId = 0;

        // When: Trigger setButtonVisibility method
        eventInfoViewModel.setButtonVisibility(hostId, userId, eventId);
        // Then: Assert that the data is correct
        assert(eventInfoViewModel.getShowDeleteEventButton().getValue());
        assert(eventInfoViewModel.getShowViewApplicantsButton().getValue());
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
    public void testButtonVisibility_Participant_Applied_Pending_DeadlineNotPassed() {
        // Mock data, assume user_id == host_id
        int hostId = 0;
        int userId = 1;
        int eventId = 0;

        eventInfoViewModel.getEventData().setValue(mockEventDataResult());

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
        // Then: Assert that the data is correct
        assert(eventInfoViewModel.getShowResultButton().getValue());
        assert(eventInfoViewModel.getShowCancelRegButton().getValue());
        assert(eventInfoViewModel.getApplicationStatus().getValue().equals("PENDING"));
        assert(eventInfoViewModel.getClickableCancelButton().getValue());
    }

    @Test
    public void testButtonVisibility_Participant_Applied_Pending_DeadlinePassed() {
        // Mock data, assume user_id == host_id
        int hostId = 0;
        int userId = 1;
        int eventId = 0;

        eventInfoViewModel.getEventData().setValue(mockEventData_DeadlinePassed());

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
        // Then: Assert that the data is correct
        assert(eventInfoViewModel.getShowResultButton().getValue());
        assert(eventInfoViewModel.getShowCancelRegButton().getValue());
        assert(eventInfoViewModel.getApplicationStatus().getValue().equals("PENDING"));
        assert(!eventInfoViewModel.getClickableCancelButton().getValue());
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
    public void testButtonVisibility_Participant_Applied_Confirmed_DeadlineNotPassed() {
        // Mock data, assume user_id == host_id
        int hostId = 0;
        int userId = 1;
        int eventId = 0;

        eventInfoViewModel.getEventData().setValue(mockEventDataResult());

        // Mock the repository response
        doAnswer(invocation -> {
            // Simulate a successful response
            ((CallbackInterface) invocation.getArgument(2)).onSuccess(mockAppliedApplicationResult());
            System.out.println("Mock the repository response");
            return null;
        }).when(eventRepository).checkUserAppliedEvent(eq(eventId), eq(userId), any(CallbackInterface.class));

        // When: Trigger setButtonVisibility method
        eventInfoViewModel.setButtonVisibility(userId, hostId, eventId);

        // Then: Verify that the repository is called
        verify(eventRepository).checkUserAppliedEvent(eq(eventId), eq(userId), any(CallbackInterface.class));

        // Then: Assert that the data is correct
        assert(eventInfoViewModel.getShowResultButton().getValue());
        assert(eventInfoViewModel.getShowCancelRegButton().getValue());
        assert(eventInfoViewModel.getApplicationStatus().getValue().equals("ACCEPTED"));
        assert(eventInfoViewModel.getClickableCancelButton().getValue());
    }

    @Test
    public void testButtonVisibility_Participant_Applied_Confirmed_DeadlinePassed() {
        // Mock data, assume user_id == host_id
        int hostId = 0;
        int userId = 1;
        int eventId = 0;

        eventInfoViewModel.getEventData().setValue(mockEventData_DeadlinePassed());

        // Mock the repository response
        doAnswer(invocation -> {
            // Simulate a successful response
            ((CallbackInterface) invocation.getArgument(2)).onSuccess(mockAppliedApplicationResult());
            System.out.println("Mock the repository response");
            return null;
        }).when(eventRepository).checkUserAppliedEvent(eq(eventId), eq(userId), any(CallbackInterface.class));

        // When: Trigger setButtonVisibility method
        eventInfoViewModel.setButtonVisibility(userId, hostId, eventId);

        // Then: Verify that the repository is called
        verify(eventRepository).checkUserAppliedEvent(eq(eventId), eq(userId), any(CallbackInterface.class));
        // Then: Assert that the data is correct
        assert(eventInfoViewModel.getShowResultButton().getValue());
        assert(eventInfoViewModel.getShowCancelRegButton().getValue());
        assert(eventInfoViewModel.getApplicationStatus().getValue().equals("ACCEPTED"));
        assert(!eventInfoViewModel.getClickableCancelButton().getValue());
    }

    @Test
    public void testButtonVisibility_Participant_Register_DeadlineNotPassed(){
        // Mock data, assume user_id == host_id
        int hostId = 0;
        int userId = 1;
        int eventId = 0;

        eventInfoViewModel.getEventData().setValue(mockEventDataResult());

        // Mock the repository response
        doAnswer(invocation -> {
            // Simulate a successful response
            ((CallbackInterface) invocation.getArgument(2)).onError("No application found");
            System.out.println("Mock the repository response");
            return null;
        }).when(eventRepository).checkUserAppliedEvent(eq(eventId), eq(userId), any(CallbackInterface.class));

        // When: Trigger setButtonVisibility method
        eventInfoViewModel.setButtonVisibility(userId, hostId, eventId);

        // Then: Verify that the repository is called
        verify(eventRepository).checkUserAppliedEvent(eq(eventId), eq(userId), any(CallbackInterface.class));

        // Then: Assert that the data is correct
        assert(eventInfoViewModel.getShowRegisterButton().getValue());
        assert(eventInfoViewModel.getClickableRegisterButton().getValue());
    }

    @Test
    public void testButtonVisibility_Participant_Register_DeadlinePassed(){
        // Mock data, assume user_id == host_id
        int hostId = 0;
        int userId = 1;
        int eventId = 0;

        eventInfoViewModel.getEventData().setValue(mockEventData_DeadlinePassed());

        // Mock the repository response
        doAnswer(invocation -> {
            // Simulate a successful response
            ((CallbackInterface) invocation.getArgument(2)).onError("No application found");
            System.out.println("Mock the repository response");
            return null;
        }).when(eventRepository).checkUserAppliedEvent(eq(eventId), eq(userId), any(CallbackInterface.class));

        // When: Trigger setButtonVisibility method
        eventInfoViewModel.setButtonVisibility(userId, hostId, eventId);

        // Then: Verify that the repository is called
        verify(eventRepository).checkUserAppliedEvent(eq(eventId), eq(userId), any(CallbackInterface.class));

        // Then: Assert that the data is correct
        assert(eventInfoViewModel.getShowRegisterButton().getValue());
        assert(!eventInfoViewModel.getClickableRegisterButton().getValue());

    }

    @Test
    public void testDeleteEvent() {
        // Mock event_id data
        int eventId = 0;
        String expectedSuccessMessage = "Event deleted successfully";
        // Mock the repository response
        doAnswer(invocation -> {
            // Simulate a successful response
            ((CallbackInterface) invocation.getArgument(1)).onSuccess(expectedSuccessMessage);
            System.out.println("Mock the repository response");
            return null;
        }).when(eventRepository).deleteEvent(eq(eventId), any(CallbackInterface.class));

        // When: Trigger the deleteEvent method
        eventInfoViewModel.deleteEvent(eventId);
        // Then: Verify that the repository is called
        verify(eventRepository).deleteEvent(eq(eventId), any(CallbackInterface.class));

        // Assert that the getShowDeleteEventSuccess() returns true
        assert(eventInfoViewModel.getShowDeleteEventSuccess().getValue());

    }

    @Test
    public void testDeleteApplication() {
        // Mock application_id data
        int applicationId = 0;
        eventInfoViewModel.getApplicationData().setValue(mockAppliedApplicationResult());

        String expectedSuccessMessage = "Application deleted successfully";
        // Mock the repository response
        doAnswer(invocation -> {
            // Simulate a successful response
            ((CallbackInterface) invocation.getArgument(1)).onSuccess(expectedSuccessMessage);
            System.out.println("Mock the repository response");
            return null;
        }).when(eventRepository).deleteApplication(eq(applicationId), any(CallbackInterface.class));

        // When: Trigger the deleteApplication method
        eventInfoViewModel.deleteApplication();
        // Then: Verify that the repository is called
        verify(eventRepository).deleteApplication(eq(applicationId), any(CallbackInterface.class));
        // Assert that the getShowDeleteApplicationSuccess() returns true
        assert(eventInfoViewModel.getShowDeleteApplicationSuccess().getValue());
    }
}