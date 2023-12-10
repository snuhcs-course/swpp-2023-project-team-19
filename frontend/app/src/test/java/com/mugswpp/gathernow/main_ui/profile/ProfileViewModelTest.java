package com.mugswpp.gathernow.main_ui.profile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.mugswpp.gathernow.api.models.EventDataModel;
import com.mugswpp.gathernow.api.models.UserDataModel;
import com.mugswpp.gathernow.authenticate.UserLocalDataSource;
import com.mugswpp.gathernow.main_ui.CallbackInterface;
import com.mugswpp.gathernow.main_ui.EventDataSource;
import com.mugswpp.gathernow.main_ui.EventRepository;
import com.mugswpp.gathernow.main_ui.UserRemoteRepository;
import com.mugswpp.gathernow.main_ui.UserRemoteDataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ProfileViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private EventDataSource eventDataSource;
    @Mock
    private UserRemoteDataSource userDataSource;
    @Spy
    @InjectMocks
    private EventRepository eventRepository;
    @Mock
    public UserLocalDataSource userLocalDataSource;
    private Context context;
    @Spy
    @InjectMocks
    private UserRemoteRepository userRemoteRepository;
    private ProfileViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        context = RuntimeEnvironment.getApplication();

        viewModel = new ProfileViewModel(context);
    }

    private List<EventDataModel> mockEventDataModels() {
        List<EventDataModel> eventDataModels = new ArrayList<>();
        eventDataModels.add(new EventDataModel("Leisure", "Test event", 10, "2024-04-20", "12:00:00", "2 hours", "English", 0, "Test location", 0.0, 0.0, "Test description", 0, 0, 0, "2024-04-20", "12:00:00", "Test image"));
        return eventDataModels;
    }

    private UserDataModel mockUserDataModels() {
        UserDataModel userDataModels = new UserDataModel("Kiwi", "kiwi@gmail.com", 2, "2023-11-27 16:22:41.734874", "avatar_image/picture10.png");
        return userDataModels;
    }

    @Test
    public void fetchUserEvents_updatesUserEventsLiveData() throws InterruptedException {

        lenient().doAnswer(invocation -> {
            // Simulate a successful response
            CallbackInterface callback = (CallbackInterface) invocation.getArgument(1);
            callback.onSuccess(mockEventDataModels());
            System.out.println("Here " );
            return null;
        }).when(eventDataSource).getUserEvents(anyInt(), any(CallbackInterface.class));

        // Observe the ViewModel
        viewModel.getUserEvents().observeForever(events -> {
            // Assert that the observed events match the expected ones
            Assert.assertNotNull(events);
            Assert.assertEquals(mockEventDataModels(), events);
            System.out.println("Mock events: " + events);
        });

        viewModel.fetchUserEvents(2);
    }

    @Test
    public void fetchUserInfo_updatesUserInfoLiveData() throws InterruptedException {

        when(userLocalDataSource.getUserId()).thenReturn("2");

        lenient().doAnswer(invocation -> {
            // Simulate a successful response
            CallbackInterface callback = invocation.getArgument(1);
            callback.onSuccess(mockUserDataModels());
            System.out.println("Here " );
            return null;
        }).when(userRemoteRepository).getUserInfo(eq(2), any(CallbackInterface.class));

        // Observe the ViewModel
        viewModel.getUserData().observeForever(user -> {
            // Assert that the observed events match the expected ones
            Assert.assertNotNull(user);
            Assert.assertEquals(mockUserDataModels(), user);
            System.out.println("Mock user: " + user);
        });

        viewModel.fetchUserProfile(userLocalDataSource);
    }

    @Test
    public void getUserDataTest() {
        // Mock data or use a spy to manipulate the real EventRepository if necessary
        // ...

        // Prepare expected data
        UserDataModel mockUser = mockUserDataModels();
        // Manually set the LiveData within HomeViewModel
        viewModel.getUserData().setValue(mockUser);

        // Assert that getAllEvents() returns the LiveData with expected data
        assertEquals(mockUser, viewModel.getUserData().getValue());
    }

    @Test
    public void getUserEventsTest() {
        // Mock data or use a spy to manipulate the real EventRepository if necessary
        // ...

        // Prepare expected data
        List<EventDataModel> mockEvents = mockEventDataModels();
        // Manually set the LiveData within HomeViewModel
        viewModel.getUserEvents().setValue(mockEvents);

        // Assert that getAllEvents() returns the LiveData with expected data
        assertEquals(mockEvents, viewModel.getUserEvents().getValue());
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

}