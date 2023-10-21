from django.urls import path
from . import views

urlpatterns = [
    path('events/', views.event_list, name='event-list'),
    path('events/by_user/<int:user_id>/', views.events_by_user, name='events-by-user'),
    path('events/one/<int:pk>/', views.event_detail, name='event-detail'),
]
