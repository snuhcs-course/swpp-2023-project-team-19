import unittest
import coverage

from django.test import TestCase
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APIRequestFactory, APIClient

from events.models import Event
from events.serializers import EventSerializer
from events.views import event_list, event_detail, events_by_id, events_by_user
from datetime import date

# Create your tests here.
class EventModelTestCase(TestCase):
    @classmethod
    def setUpTestData(cls):
        Event.objects.create(event_id=1, host_id=1, event_type="test", event_title="test", event_num_participants=1, event_date="2021-01-01", event_time="00:00:00", event_duration="test", event_language="test", event_price=1, event_location="test", event_description="test", created_at="2021-01-01", event_num_joined=1)

    def test_event_id(self):
        event = Event.objects.get(event_id=1)
        self.assertEqual(event.event_id, 1)
        self.assertEqual(event.host_id, 1)
        self.assertEqual(event.event_type, "test")
        self.assertEqual(event.event_title, "test")
        self.assertEqual(event.event_num_participants, 1)
        self.assertEqual(event.event_date.isoformat(), "2021-01-01")
        self.assertEqual(event.event_time.strftime('%H:%M:%S'), "00:00:00")
        self.assertEqual(event.event_duration, "test")
        self.assertEqual(event.event_language, "test")
        self.assertEqual(event.event_price, 1)
        self.assertEqual(event.event_location, "test")
        self.assertEqual(event.event_description, "test")
        self.assertEqual(event.event_num_joined, 1)

# Create test case for events/views.py
class EventListTestCase(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.event = Event.objects.create(event_id = 1, host_id = 1, event_type = "test", event_title = "test", event_num_participants = 1, event_date = "2021-01-01", event_time = "00:00:00", event_duration = "test", event_language = "test", event_price = 1, event_location = "test", event_description = "test", created_at = "2021-01-01", event_num_joined = 1)
    
    def test_get_event_list(self):
        request = self.factory.get('/events/')
        response = event_list(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)

    def test_post_event_list(self):
        request = self.factory.post('/events/', {'event_id': 1, 'host_id': 1, 'event_type': "test", 'event_title': "test", 'event_num_participants': 1, 'event_date': "2021-01-01", 'event_time': "00:00:00", 'event_duration': "test", 'event_language': "test", 'event_price': 1, 'event_location': "test", 'event_description': "test", 'created_at': "2021-01-01", 'event_num_joined': 1}, format='json')
        response = event_list(request)
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
    
class EventDetailTestCase(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.event = Event.objects.create(event_id = 1, host_id = 1, event_type = "test", event_title = "test", event_num_participants = 1, event_date = "2021-01-01", event_time = "00:00:00", event_duration = "test", event_language = "test", event_price = 1, event_location = "test", event_description = "test", created_at = "2021-01-01", event_num_joined = 1)

    def test_exception_event_detail(self):
        request = self.factory.get('/events/2/')
        response = event_detail(request, 2)
        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)

    def test_get_event_detail(self):
        request = self.factory.get('/events/1/')
        response = event_detail(request, 1)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_put_event_detail(self):
        request = self.factory.put('/events/1/', {'event_id': 1, 'host_id': 1, 'event_type': "test", 'event_title': "test", 'event_num_participants': 1, 'event_date': "2021-01-01", 'event_time': "00:00:00", 'event_duration': "test", 'event_language': "test", 'event_price': 1, 'event_location': "test", 'event_description': "test", 'created_at': "2021-01-01", 'event_num_joined': 1}, format='json')
        response = event_detail(request, 1)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_delete_event_detail(self):
        request = self.factory.delete('/events/1/')
        response = event_detail(request, 1)
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)

class EventsByUserTest(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.event = Event.objects.create(event_id = 1, host_id = 1, event_type = "test", event_title = "test", event_num_participants = 1, event_date = "2021-01-01", event_time = "00:00:00", event_duration = "test", event_language = "test", event_price = 1, event_location = "test", event_description = "test", created_at = "2021-01-01", event_num_joined = 1)

    def test_get_events_by_user(self):
        request = self.factory.get('/events/user/1/')
        response = events_by_user(request, 1)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_method_events_by_user(self):
        request = self.factory.post('/events/user/1/')
        response = events_by_user(request, 1)
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)

class EventsByIdTest(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.event = Event.objects.create(event_id = 1, host_id = 1, event_type = "test", event_title = "test", event_num_participants = 1, event_date = "2021-01-01", event_time = "00:00:00", event_duration = "test", event_language = "test", event_price = 1, event_location = "test", event_description = "test", created_at = "2021-01-01", event_num_joined = 1)
    
    def test_get_events_by_id(self):
        request = self.factory.get('/events/id/1/')
        response = events_by_id(request, 1)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_delete_events_by_id(self):
        request = self.factory.delete('/events/id/1/')
        response = events_by_id(request, 1)
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)
    
    def test_method_events_by_id(self):
        request = self.factory.post('/events/id/1/')
        response = events_by_id(request, 1)
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)

if __name__ == '__main__':
    cov = coverage.Coverage(source=['.'])  # Specify the source directory
    cov.start()
    unittest.main()
    cov.stop()
    cov.save()
    cov.report()