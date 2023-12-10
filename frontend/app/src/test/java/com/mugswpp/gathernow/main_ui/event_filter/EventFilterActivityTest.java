package com.mugswpp.gathernow.main_ui.event_filter;
import android.view.View;

import com.mugswpp.gathernow.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

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
