from django.urls import path
from . import views

urlpatterns = [
    path('application/', views.application, name='application'),
    path('application/by_user/<int:user_id>/', views.user_application, name='application-by-user'),
    path('application/one/<int:pk>/', views.application_detail, name='application-detail'),
    path('application/by_event/<int:event_id>/', views.events_application, name='application-by-id'),
    path('application/check/<int:user_id>/<int:event_id>/', views.check_application, name='check_application'),
]
