package com.example.gathernow.main_ui.event_search;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.authenticate.UserLocalDataSource;
import com.example.gathernow.main_ui.event_search.EventSearchActivity;
import com.example.gathernow.main_ui.event_search.EventSearchViewModel;
import com.example.gathernow.utils.EventCardHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
