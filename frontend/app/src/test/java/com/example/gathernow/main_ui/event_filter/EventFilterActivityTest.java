package com.example.gathernow.main_ui.event_filter;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.gathernow.R;
import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.main_ui.event_filter.EventFilterActivity;
import com.example.gathernow.main_ui.event_filter.EventFilterViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

@RunWith(MockitoJUnitRunner.class)
public class EventFilterActivityTest {

    @Mock
    private EventFilterViewModel mockEventFilterViewModel;

    @Mock
    private View mockRootView;

    @Mock
    private BottomSheetDialogFragment mockBottomSheetDialogFragment;

    @InjectMocks
    private EventFilterActivity eventFilterActivity;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Test
    public void testGetQueryString() {

        eventFilterActivity.selectedLanguageChips.add("English");
        eventFilterActivity.selectedLanguageChips.add("Spanish");
        eventFilterActivity.selectedEventTypeChips.add("Parties");
        eventFilterActivity.selectedDateChips.add("Next week");
        eventFilterActivity.selectedTimeChips.add("Morning");
        eventFilterActivity.isFreeEvent = true;

        // Call the method under test
        String queryString = eventFilterActivity.getQueryString();

        // Assert the expected query string
        //assertEquals("?is_free=true&language=English&language=Spanish&event_type=Parties&", queryString);
        assertEquals("?is_free=true&language=English&language=Spanish&event_type=Parties&date=Next week&time=Morning&", queryString);
    }

}
