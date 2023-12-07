package com.mugswpp.gathernow.main_ui.event_search;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.mugswpp.gathernow.authenticate.UserLocalDataSource;
import com.mugswpp.gathernow.utils.EventCardHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EventSearchActivityTest {

    @Mock
    private EventSearchViewModel mockViewModel;

    @Mock
    private UserLocalDataSource mockUserLocalDataSource;

    @Mock
    private LinearLayout mockEventCardContainer;

    @Mock
    private InputMethodManager mockInputMethodManager;

    @Mock
    private View mockView;

    @Mock
    private EventCardHelper EventCardHelper;

    private EventSearchActivity activity;


    @Test
    public void searchEventTest() {
        // refer to android test since its all about UI in this class...
    }
}
