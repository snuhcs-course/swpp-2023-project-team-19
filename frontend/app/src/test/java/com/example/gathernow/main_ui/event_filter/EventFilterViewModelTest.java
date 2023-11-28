package com.example.gathernow.main_ui.event_filter;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import android.content.Context;
import android.telecom.Call;

import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.main_ui.CallbackInterface;
import com.example.gathernow.main_ui.EventDataSource;
import com.example.gathernow.main_ui.EventRepository;

import javax.security.auth.callback.Callback;

@RunWith(MockitoJUnitRunner.class)
public class EventFilterViewModelTest {

    private EventFilterViewModel viewModel;
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Spy
    @InjectMocks
    private EventFilterActivity mockEventFilterActivity;

    @Spy
    @InjectMocks
    private EventRepository eventRepository;

    @Mock
    private MutableLiveData<String> alertMessage;

    @Mock
    private MutableLiveData<List<EventDataModel>> filteredEvents;

    @Mock
    private EventDataSource eventDataSource;

    @Captor
    private ArgumentCaptor<CallbackInterface> callbackCaptor;


    @Before
    public void setUp() {
        //MockitoAnnotations.initMocks(this);
        viewModel = new EventFilterViewModel(mockEventFilterActivity);
    }


    @Test
    public void fetchFilteredEventsTest() {
        lenient().doAnswer(invocation -> {
            // Simulate a successful response
            CallbackInterface callback = (CallbackInterface) invocation.getArgument(1);
            callback.onSuccess(mockEventDataModels());
            return null;
        }).when(eventDataSource).getSearchedEvents(anyString(), any(CallbackInterface.class));

        // Observe the ViewModel
        viewModel.getFilteredEvents().observeForever(events -> {
            // Assert that the observed events match the expected ones
            assertNotNull(events);
            assertEquals(mockEventDataModels(), events);
        });

        viewModel.fetchFilteredEvents("test query");
        viewModel.getFilteredEvents().removeObservers(mockEventFilterActivity);
    }

    @Test
    public void getAlertMessageTest() {
        String testQuery = "test query";
        String errorMessage = "Error";

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onChanged(String alertMessage) {
                // THEN
                assertEquals(errorMessage, viewModel.getAlertMessage().getValue());
            }
        };

        try{
            viewModel.getAlertMessage().observeForever(observer);
            viewModel.fetchFilteredEvents(testQuery);

        } finally {// close the observer
            viewModel.getAlertMessage().removeObserver(observer);
        }
    }

    private List<EventDataModel> mockEventDataModels() {
        List<EventDataModel> eventDataModels = new ArrayList<>();
        eventDataModels.add(new EventDataModel("Leisure", "Test event", 10, "2024-04-20", "12:00:00", "2 hours", "English", 0, "Test location", 0.0, 0.0, "Test description", 0, 0, 0, "2024-04-20", "12:00:00", "Test image"));
        return eventDataModels;
    }

}
