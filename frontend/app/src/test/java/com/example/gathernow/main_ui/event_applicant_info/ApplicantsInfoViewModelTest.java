package com.example.gathernow.main_ui.event_applicant_info;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.gathernow.api.models.ApplicationDataModel;
import com.example.gathernow.main_ui.CallbackInterface;
import com.example.gathernow.main_ui.EventRepository;

import junit.framework.TestCase;
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ApplicantsInfoViewModelTest extends TestCase {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private EventRepository eventRepository;

    @Mock
    private CallbackInterface callbackInterface;

    @Mock
    private Observer<List<ApplicationDataModel>> applicationListObserver;

    @Mock
    private Observer<String> alertMessageObserver;

    private ApplicantsInfoViewModel applicantsInfoViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        applicantsInfoViewModel = new ApplicantsInfoViewModel();
        applicantsInfoViewModel.getApplicationList().observeForever(applicationListObserver);
        applicantsInfoViewModel.getAlertMessage().observeForever(alertMessageObserver);
    }

    private List<ApplicationDataModel> mockApplicationDataModelsList() {
        List<ApplicationDataModel> eventDataModels = new ArrayList<>();
        eventDataModels.add(new ApplicationDataModel("@test", "Test message", 1, 1, 1, "Test name", "Test_avatar"));
        return eventDataModels;
    }

    @Test
    public void getAlertMessageTest() throws InterruptedException {
        // Mock data or use a spy to manipulate the real EventRepository if necessary
        // ...

        // Prepare expected data
        String mockMessage = "Test message";
        // Manually set the LiveData within HomeViewModel
        applicantsInfoViewModel.getAlertMessage().setValue(mockMessage);

        // Assert that getAllEvents() returns the LiveData with expected data
        assertEquals(mockMessage, applicantsInfoViewModel.getAlertMessage().getValue());

    }

    @Test
    public void getApplicationListTest() {
        // Mock data or use a spy to manipulate the real EventRepository if necessary
        // ...

        // Prepare expected data
        List<ApplicationDataModel> mockApplication = mockApplicationDataModelsList();
        // Manually set the LiveData within HomeViewModel
        applicantsInfoViewModel.getApplicationList().setValue(mockApplication);

        // Assert that getAllEvents() returns the LiveData with expected data
        assertEquals(mockApplication, applicantsInfoViewModel.getApplicationList().getValue());
    }


    @Test
    public void fetchEventApplication_success() {
        int eventId = 2;

        List<ApplicationDataModel> fakeApplicationList = mockApplicationDataModelsList();

        doAnswer(invocation -> {
            ((CallbackInterface) invocation.getArgument(1)).onSuccess(fakeApplicationList);
            return null;
        }).when(eventRepository).getEventApplication(eq(2), any(CallbackInterface.class));


        applicantsInfoViewModel.fetchEventApplication(eventId);

        verify(applicationListObserver, never()).onChanged(fakeApplicationList);
        verify(alertMessageObserver, Mockito.never()).onChanged(Mockito.anyString());
    }

    @Test
    public void fetchEventApplication_error() {
        int eventId = 2;
        String errorMessage = "Some error message";

        doAnswer(invocation -> {
            ((CallbackInterface) invocation.getArgument(1)).onError(errorMessage);
            return null;
        }).when(eventRepository).getEventApplication(eq(eventId), any(CallbackInterface.class));

        applicantsInfoViewModel.fetchEventApplication(eventId);

        verify(alertMessageObserver, never()).onChanged(errorMessage);
        verify(applicationListObserver, never()).onChanged(new ArrayList<>());
    }
}
