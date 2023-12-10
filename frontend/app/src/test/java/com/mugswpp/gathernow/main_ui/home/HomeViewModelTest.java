package com.mugswpp.gathernow.main_ui.home;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.mugswpp.gathernow.api.models.EventDataModel;
import com.mugswpp.gathernow.main_ui.CallbackInterface;
import com.mugswpp.gathernow.main_ui.EventDataSource;
import com.mugswpp.gathernow.main_ui.EventRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class HomeViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Spy
    @InjectMocks
    private EventDataSource eventDataSource;

    private HomeViewModel viewModel;
    @Spy
    @InjectMocks
    private EventRepository mockEventRepository;
    @Mock
    private Context mockContext;

    @Spy
    @InjectMocks
    private HomeActivity homeActivity;

    @Before
    public void setup() {
        //mockEventDataSource= Mockito.spy(new EventDataSource());
        //mockContext = Mockito.mock(Context.class);
        //mockEventRepository = Mockito.spy(new EventRepository(mockEventDataSource));
        try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {
            // Other setup code...
            viewModel = new HomeViewModel(homeActivity.getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private List<EventDataModel> createMockEventData() {
        // Parse the JSON array and create a list of EventDataModel objects
        String jsonData = "[{\"event_id\":109,\"host_id\":24,\"event_type\":\"Parties\",\"event_title\":\"BBQ party\",\"event_num_participants\":5,\"event_date\":\"2023-12-16\",\"event_time\":\"18:30:00\",\"event_duration\":\"2 hours\",\"event_language\":\"Korean, English\",\"event_price\":15000,\"event_location\":\"서울특별시 관악구 신림동  BBQ chicken\",\"event_longitude\":126.95085594169672,\"event_latitude\":37.45483721173874,\"event_description\":\"Let's celebrate the end of semester by eating BBQ chicken tgt in SNU!!!\",\"created_at\":\"2023-11-25T04:01:52.292152Z\",\"event_num_joined\":0,\"event_register_date\":\"2023-12-14\",\"event_register_time\":\"13:01:00\",\"event_images\":null},{\"event_id\":110,\"host_id\":24,\"event_type\":\"Sports\",\"event_title\":\"Bowling\",\"event_num_participants\":3,\"event_date\":\"2023-12-02\",\"event_time\":\"13:00:00\",\"event_duration\":\"3 hours\",\"event_language\":\"Japanese\",\"event_price\":10000,\"event_location\":\"서울특별시 영등포구 영등포동  Bowling center\",\"event_longitude\":126.9066980423355,\"event_latitude\":37.51610699996736,\"event_description\":\"Finding people who speaks Japanese to play bowling together~\",\"created_at\":\"2023-11-25T04:04:47.095854Z\",\"event_num_joined\":0,\"event_register_date\":\"2023-12-01\",\"event_register_time\":\"10:00:00\",\"event_images\":null},{\"event_id\":111,\"host_id\":24,\"event_type\":\"Leisure\",\"event_title\":\"Hangang fireworks\",\"event_num_participants\":4,\"event_date\":\"2023-12-31\",\"event_time\":\"22:00:00\",\"event_duration\":\"3 hours\",\"event_language\":\"Korean, German\",\"event_price\":0,\"event_location\":\"서울특별시 영등포구 여의도동  Yeouido Hangang Park\",\"event_longitude\":126.93484360477618,\"event_latitude\":37.53664067694941,\"event_description\":\"It might be freezing weather but anyways let's countdown at Hangang together!!!\",\"created_at\":\"2023-11-25T04:09:11.375469Z\",\"event_num_joined\":0,\"event_register_date\":\"2023-12-31\",\"event_register_time\":\"12:00:00\",\"event_images\":null},{\"event_id\":113,\"host_id\":9,\"event_type\":\"Parties\",\"event_title\":\"Home party\",\"event_num_participants\":3,\"event_date\":\"2023-12-30\",\"event_time\":\"17:00:00\",\"event_duration\":\"3 hours\",\"event_language\":\"Korean, English\",\"event_price\":5000,\"event_location\":\"서울특별시 관악구 봉천동  No. 308\",\"event_longitude\":126.95193001513206,\"event_latitude\":37.48098894454624,\"event_description\":\"Welcome\",\"created_at\":\"2023-11-26T06:59:47.543776Z\",\"event_num_joined\":1,\"event_register_date\":\"2023-12-29\",\"event_register_time\":\"18:00:00\",\"event_images\":\"/media/event_image/event_thumbnail.jpg\"}]";
        Type listType = new TypeToken<ArrayList<EventDataModel>>(){}.getType();
        return new Gson().fromJson(jsonData, listType);
    }
    private List<EventDataModel> mockEventDataModels() {
        List<EventDataModel> eventDataModels = new ArrayList<>();
        eventDataModels.add(new EventDataModel("Leisure", "Test event", 10, "2024-04-20", "12:00:00", "2 hours", "English", 0, "Test location", 0.0, 0.0, "Test description", 0, 0, 0, "2024-04-20", "12:00:00", "Test image"));
        return eventDataModels;
    }

    @Test
    public void fetchAllEvents_updatesAllEventsLiveData() throws InterruptedException {
        List<EventDataModel> mockEvents = createMockEventData();
//        System.out.println("Mock events: " + mockEvents);
        System.out.println("Mock event repository: " + mockEventRepository);

        // Call the method under test
        viewModel.fetchAllEvents();

    }
    @Test
    public void fetchEventsTestWithLiveData() {
        lenient().doAnswer(invocation -> {

            // Simulate a successful response
            CallbackInterface callback = (CallbackInterface) invocation.getArgument(0);
            callback.onSuccess(mockEventDataModels());

            return null;
        }).when(mockEventRepository).getAllEvents(any(CallbackInterface.class));

        // Observe the ViewModel
        viewModel.getAllEvents().observeForever(events -> {
            // Assert that the observed events match the expected ones

            assertNotNull(events);
            assertEquals(mockEventDataModels(), events);
            System.out.println("Mock event data models: ");
        });

        viewModel.fetchAllEvents();
        viewModel.getAllEvents().removeObservers(homeActivity);
    }

    @Test
    public void getAllEvents_updatesCorrectly() throws InterruptedException {
        // Mock data or use a spy to manipulate the real EventRepository if necessary
        // ...

        // Prepare expected data
        List<EventDataModel> mockEvents = createMockEventData();
        // Manually set the LiveData within HomeViewModel
        viewModel.getAllEvents().setValue(mockEvents);

        // Assert that getAllEvents() returns the LiveData with expected data
        assertEquals(mockEvents, viewModel.getAllEvents().getValue());


    }

    @Test
    public void getALertMessage_updatesCorrectly() throws InterruptedException {
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

