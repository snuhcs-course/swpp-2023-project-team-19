package com.example.gathernow.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

import com.example.gathernow.main_ui.cards.EventCardView;
import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.main_ui.event_info.EventInfoActivity;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class EventCardHelper {
    public static void createEventCardList(Context context, List<EventDataModel> eventDataList, LinearLayout eventCardContainer, int userId) {
        if (eventDataList == null || eventDataList.isEmpty()) {
            return;
        }
        for (int i = 0; i < eventDataList.size(); i++) {
            EventDataModel currentEvent = eventDataList.get(i);
            EventCardView newEventCard = new EventCardView(context, null);
            newEventCard.setEventName(currentEvent.getEventTitle());
            newEventCard.setEventPhoto(currentEvent.getEventType(), currentEvent.getEventImages());
            newEventCard.setEventCapacity(currentEvent.getEventNumJoined(), currentEvent.getEventNumParticipants());
            newEventCard.setEventLocation(currentEvent.getEventLocation());
            newEventCard.setEventLanguage(currentEvent.getEventLanguage());
            newEventCard.setEventDateTime(Date.valueOf(currentEvent.getEventDate()), Time.valueOf(currentEvent.getEventTime()));

            // Add vertical padding to the newEventCard
            int verticalPadding = (int) (10 * context.getResources().getDisplayMetrics().density); // 16dp converted to pixels
            newEventCard.setPadding(newEventCard.getPaddingLeft(), verticalPadding, newEventCard.getPaddingRight(), verticalPadding);
            eventCardContainer.addView(newEventCard);

            newEventCard.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), EventInfoActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("eventId", currentEvent.getEventId());
                context.startActivity(intent);
            });

        }
    }
}
