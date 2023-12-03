package com.example.gathernow.main_ui.event_application_form;

import junit.framework.TestCase;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.gathernow.api.ServiceApi;
import com.example.gathernow.api.models.ApplicationDataModel;
import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.api.models.EventDataModelBuilder;
import com.example.gathernow.api.models.UserDataModel;
import com.example.gathernow.api.models.UserDataModelBuilder;
import com.example.gathernow.main_ui.CallbackInterface;
import com.example.gathernow.main_ui.EventRepository;
import com.example.gathernow.main_ui.UserRemoteRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationFormViewModelTest extends TestCase {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private ServiceApi mockService;

    @Mock
    private EventRepository mockEventRepository;

    @Mock
    private UserRemoteRepository mockUserRemoteRepository;

    @Mock
    private Observer<UserDataModel> mockUserDataObserver;

    @Mock
    private Observer<String> mockAlertMessageObserver;
    @Mock
    private Observer<EventDataModel> mockEventDataObserver;

    @Captor
    private ArgumentCaptor<CallbackInterface> callbackCaptor;

    private ApplicationFormViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        viewModel = new ApplicationFormViewModel();
        viewModel.service = mockService;
        viewModel.eventRepository = mockEventRepository;
        viewModel.userRemoteRepository = mockUserRemoteRepository;
        viewModel.getApplicantData().observeForever(mockUserDataObserver);
        viewModel.getAlertMessage().observeForever(mockAlertMessageObserver);
        viewModel.getEventData().observeForever(mockEventDataObserver);
    }
    private UserDataModel mockUserDataModels() {
        return new UserDataModelBuilder()
                .setName("Kiwi")
                .setEmail("kiwi@gmail.com")
                .setUserId(2)
                .setAvatar("avatar_image/picture10.png")
                .setCreatedAt("2023-11-27 16:22:41.734874")
                .build();
    }

    private EventDataModel mockEventDataModels() {
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
    public void fetchEventData_success() {
        int eventId = 0;
        EventDataModel eventDataModel = mockEventDataModels();

        viewModel.fetchEventData(eventId);

        Mockito.verify(mockEventRepository).getEventInfo(Mockito.eq(eventId), callbackCaptor.capture());
        callbackCaptor.getValue().onSuccess(eventDataModel);

        Mockito.verify(mockEventDataObserver).onChanged(Mockito.eq(eventDataModel));
        Mockito.verifyNoMoreInteractions(mockAlertMessageObserver);
    }

    @Test
    public void fetchEventData_null() {
        int eventId = 0;
        String errorMessage = "Event not found";

        viewModel.fetchEventData(eventId);

        Mockito.verify(mockEventRepository).getEventInfo(Mockito.eq(eventId), callbackCaptor.capture());
        callbackCaptor.getValue().onSuccess(null);

        Mockito.verify(mockAlertMessageObserver).onChanged(Mockito.eq(errorMessage));
        Mockito.verify(mockEventDataObserver).onChanged(Mockito.eq(null));
    }

    @Test
    public void fetchUserData_success() {
        int userId = 123;
        UserDataModel userDataModel = mockUserDataModels();

        viewModel.fetchUserData(userId);

        Mockito.verify(mockUserRemoteRepository).getUserInfo(Mockito.eq(userId), callbackCaptor.capture());
        callbackCaptor.getValue().onSuccess(userDataModel);

        Mockito.verify(mockUserDataObserver).onChanged(Mockito.eq(userDataModel));
        Mockito.verifyNoMoreInteractions(mockAlertMessageObserver);
    }

    @Test
    public void fetchUserData_error() {
        int userId = 123;
        String errorMessage = "Error message";

        viewModel.fetchUserData(userId);

        Mockito.verify(mockUserRemoteRepository).getUserInfo(Mockito.eq(userId), callbackCaptor.capture());
        callbackCaptor.getValue().onError(errorMessage);

        Mockito.verify(mockAlertMessageObserver).onChanged(Mockito.eq(errorMessage));
        Mockito.verifyNoMoreInteractions(mockUserDataObserver);
    }

    private List<ApplicationDataModel> mockApplicationDataModelsList() {
        List<ApplicationDataModel> eventDataModels = new ArrayList<>();
        eventDataModels.add(new ApplicationDataModel("@test", "Test message", 1, 1, 1, "Test name", "Test_avatar"));
        return eventDataModels;
    }

    private List<ApplicationDataModel> mockAInvalidApplicationDataModelsList() {
        List<ApplicationDataModel> eventDataModels = new ArrayList<>();
        eventDataModels.add(new ApplicationDataModel("", "Test message", 1, 1, 1, "Test name", "Test_avatar"));
        return eventDataModels;
    }


    @Test
    public void applyEvent_validInput_eventAvailable_success() {
        ApplicationDataModel applicationDataModel = mockApplicationDataModelsList().get(0);

        viewModel.applyEvent(applicationDataModel);

        Mockito.verify(mockEventRepository).applyEvent(Mockito.eq(applicationDataModel), callbackCaptor.capture());
        callbackCaptor.getValue().onSuccess("Success message");

        Mockito.verify(mockAlertMessageObserver).onChanged(Mockito.eq("Success message"));
        Mockito.verifyNoMoreInteractions(mockAlertMessageObserver);
    }

    @Test
    public void applyEvent_invalidInput_error() {
        ApplicationDataModel applicationDataModel = mockAInvalidApplicationDataModelsList().get(0); // invalid input

        viewModel.applyEvent(applicationDataModel);

        Mockito.verify(mockAlertMessageObserver).onChanged(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(mockEventRepository);
    }

    // Additional tests for isInputValid method and other scenarios as needed
}
