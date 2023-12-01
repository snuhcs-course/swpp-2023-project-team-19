import unittest
import coverage
from datetime import datetime, timedelta

from django.test import TestCase
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APIRequestFactory, APIClient

from events.models import Event
from events.serializers import EventSerializer
from events.views import event_list, event_detail, events_by_id, events_by_user, increase_num_joined, decrease_num_joined, events_filter, events_search

# Create your tests here.
class EventModelTestCase(TestCase):
    @classmethod
    def setUpTestData(cls):
        Event.objects.create(
            event_id=1,
            host_id=1,
            event_type="test",
            event_title="test",
            event_num_participants=1,
            event_date="2023-11-30",
            event_time="00:00:00",
            event_duration="test",
            event_language="test",
            event_price=1,
            event_location="test",
            event_longitude=0.0,  # Update with appropriate values
            event_latitude=0.0,   # Update with appropriate values
            event_description="test",
            created_at="2023-11-30",
            event_num_joined=1,
            event_register_date="2023-11-30",  # Update with appropriate values
            event_register_time="00:00:00",    # Update with appropriate values
        )

    def test_event_id(self):
        event = Event.objects.get(event_id=1)
        self.assertEqual(event.event_id, 1)
        self.assertEqual(event.host_id, 1)
        self.assertEqual(event.event_type, "test")
        self.assertEqual(event.event_title, "test")
        self.assertEqual(event.event_num_participants, 1)
        self.assertEqual(event.event_date.isoformat(), "2023-11-30")
        self.assertEqual(event.event_time.strftime('%H:%M:%S'), "00:00:00")
        self.assertEqual(event.event_duration, "test")
        self.assertEqual(event.event_language, "test")
        self.assertEqual(event.event_price, 1)
        self.assertEqual(event.event_location, "test")
        self.assertEqual(event.event_longitude, 0.0)  # Update with appropriate values
        self.assertEqual(event.event_latitude, 0.0)   # Update with appropriate values
        self.assertEqual(event.event_description, "test")
        self.assertEqual(event.event_num_joined, 1)
        self.assertEqual(event.event_register_date.isoformat(), "2023-11-30")  # Update with appropriate values
        self.assertEqual(event.event_register_time.strftime('%H:%M:%S'), "00:00:00")  # Update with appropriate values


# Create test case for events/views.py
class EventListTestCase(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.event = Event.objects.create(event_id = 1, host_id = 1, event_type = "test", event_title = "test", event_num_participants = 1, event_date = "2023-01-01", event_time = "00:00:00", event_duration = "test", event_language = "test", event_price = 1, event_location = "test", event_description = "test", created_at = "2023-01-01", event_num_joined = 1)
    
    def test_get_event_list(self):
        request = self.factory.get('/events/')
        response = event_list(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)

    def test_post_event_list(self):
        request = self.factory.post('/events/', {'event_id': 1, 'host_id': 1, 'event_type': "test", 'event_title': "test", 'event_num_participants': 1, 'event_date': "2023-01-01", 'event_time': "00:00:00", 'event_duration': "test", 'event_language': "test", 'event_price': 1, 'event_location': "test", 'event_description': "test", 'created_at': "2023-01-01", 'event_num_joined': 1}, format='json')
        response = event_list(request)
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
    
class EventDetailTestCase(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.event = Event.objects.create(event_id = 1, host_id = 1, event_type = "test", event_title = "test", event_num_participants = 1, event_date = "2023-01-01", event_time = "00:00:00", event_duration = "test", event_language = "test", event_price = 1, event_location = "test", event_description = "test", created_at = "2023-01-01", event_num_joined = 1)

    def test_exception_event_detail(self):
        request = self.factory.get('/events/one/2/')
        response = event_detail(request, 2)
        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)

    def test_get_event_detail(self):
        request = self.factory.get('/events/one/1/')
        response = event_detail(request, 1)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_put_event_detail(self):
        request = self.factory.put('/events/one/1/', {'event_id': 1, 'host_id': 1, 'event_type': "test", 'event_title': "test", 'event_num_participants': 1, 'event_date': "2023-01-01", 'event_time': "00:00:00", 'event_duration': "test", 'event_language': "test", 'event_price': 1, 'event_location': "test", 'event_description': "test", 'created_at': "2023-01-01", 'event_num_joined': 1}, format='json')
        response = event_detail(request, 1)
        self.assertEqual(response.status_code, status.HTTP_200_OK)

    def test_delete_event_detail(self):
        request = self.factory.delete('/events/one/1/')
        response = event_detail(request, 1)
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)
    
class EventsByUserTest(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.event = Event.objects.create(event_id = 1, host_id = 1, event_type = "test", event_title = "test", event_num_participants = 1, event_date = "2023-01-01", event_time = "00:00:00", event_duration = "test", event_language = "test", event_price = 1, event_location = "test", event_description = "test", created_at = "2023-01-01", event_num_joined = 1)

    def test_get_events_by_user(self):
        request = self.factory.get('/events/by_user/1/')
        response = events_by_user(request, 1)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_method_events_by_user(self):
        request = self.factory.post('/events/by_user/1/')
        response = events_by_user(request, 1)
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)

class EventsByIdTestCase(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.event = Event.objects.create(event_id = 1, host_id = 1, event_type = "test", event_title = "test", event_num_participants = 1, event_date = "2023-01-01", event_time = "00:00:00", event_duration = "test", event_language = "test", event_price = 1, event_location = "test", event_longitude = 0.0, event_latitude = 0.0,  event_description = "test", created_at = "2023-01-01", event_num_joined = 1)
    
    def test_get_events_by_id(self):
        request = self.factory.get('/events/by_id/1/')
        response = events_by_id(request, 1)
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)

    def test_delete_events_by_id(self):
        request = self.factory.delete('/events/by_id/1/')
        response = events_by_id(request, 1)
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)
    
    def test_method_events_by_id(self):
        request = self.factory.post('/events/by_id/1/')
        response = events_by_id(request, 1)
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)
    
    def test_put_events_by_id(self):
        request = self.factory.put('/events/by_id/1/', {'event_id': 1, 'host_id': 1, 'event_type': "test", 'event_title': "test", 'event_num_participants': 1, 'event_date': "2023-01-01", 'event_time': "00:00:00", 'event_duration': "test", 'event_language': "test", 'event_price': 1, 'event_location': "test", 'event_longitude': 0.0, 'event_latitude': 0.0, 'event_description': "test", 'created_at': "2023-01-01", 'event_num_joined': 1}, format='json')
        response = events_by_id(request, 1)
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)    
    
class IncreaseNumJoinedTestCase(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.event = Event.objects.create(event_id = 1, host_id = 1, event_type = "test", event_title = "test", event_num_participants = 1, event_date = "2023-01-01", event_time = "00:00:00", event_duration = "test", event_language = "test", event_price = 1, event_location = "test", event_longitude = 0.0, event_latitude = 0.0,  event_description = "test", created_at = "2023-01-01", event_num_joined = 1)

    def test_increase_num_joined(self):
        request = self.factory.put('/events/num_joined/add/1/')
        response = increase_num_joined(request, 1)
        self.assertEqual(response.status_code, status.HTTP_200_OK)

    def test_method_increase_num_joined(self):
        request = self.factory.post('/events/increase_num_joined/1/')
        response = increase_num_joined(request, 1)
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)

class DecreaseNumJoinedTestCase(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.event = Event.objects.create(event_id = 1, host_id = 1, event_type = "test", event_title = "test", event_num_participants = 1, event_date = "2023-01-01", event_time = "00:00:00", event_duration = "test", event_language = "test", event_price = 1, event_location = "test", event_longitude = 0.0, event_latitude = 0.0,  event_description = "test", created_at = "2023-01-01", event_num_joined = 1)
    
    def test_decrease_num_joined(self):
        request = self.factory.put('/events/num_joined/delete/1/')
        response = decrease_num_joined(request, 1)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_method_decrease_num_joined(self):
        request = self.factory.post('/events/decrease_num_joined/1/')
        response = decrease_num_joined(request, 1)
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)

class EventFilterTestCase(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.event = Event.objects.create(event_id = 1, host_id = 1, event_type = "test", event_title = "test", event_num_participants = 1, event_date = "2023-01-01", event_time = "00:00:00", event_duration = "test", event_language = "test", event_price = 1, event_location = "test", event_longitude = 0.0, event_latitude = 0.0,  event_description = "test", created_at = "2023-01-01", event_num_joined = 1)
    
    def test_get_events_filter(self):
        request = self.factory.get('/events/filter')
        response = events_filter(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_method_events_filter(self):
        request = self.factory.post('/events/filter')
        response = events_filter(request)
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)
    
    def test_events_filter_language(self):
        request = self.factory.get('/events/filter', {'language': 'test'})
        response = events_filter(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_events_filter_event_type(self):
        request = self.factory.get('/events/filter', {'event_type': 'test'})
        response = events_filter(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)

    def test_events_filter_date(self):
        request = self.factory.get('/events/filter', {'date': 'test'})
        response = events_filter(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)

    def test_events_filter_time(self):
        request = self.factory.get('/events/filter', {'time': 'test'})
        response = events_filter(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_events_filter_is_free(self):
        request = self.factory.get('/events/filter', {'is_free': 'test'})
        response = events_filter(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_events_filter_location_address(self):
        request = self.factory.get('/events/filter', {'location_address': 'test'})
        response = events_filter(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_events_filter_all(self):
        request = self.factory.get('/events/filter', {'language': 'test', 'event_type': 'test', 'date': 'test', 'time': 'test', 'is_free': 'test', 'location_address': 'test'})
        response = events_filter(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_events_filter_all(self):
        request = self.factory.get('/events/filter', {'language': 'test', 'event_type': 'test', 'date': 'test', 'time': 'test', 'is_free': 'test', 'location_address': 'test'})
        response = events_filter(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)

class EventsSearchTestCase(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.event = Event.objects.create(event_id = 1, host_id = 1, event_type = "test", event_title = "test", event_num_participants = 1, event_date = "2023-01-01", event_time = "00:00:00", event_duration = "test", event_language = "test", event_price = 1, event_location = "test", event_longitude = 0.0, event_latitude = 0.0,  event_description = "test", created_at = "2023-01-01", event_num_joined = 1)
    
    def test_get_events_search(self):
        request = self.factory.get('/events/search')
        response = events_search(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_method_events_search(self):
        request = self.factory.post('/events/search')
        response = events_search(request)
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)
    
    def test_events_search(self):
        request = self.factory.get('/events/search', {'search': 'test'})
        response = events_search(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_events_search_all(self):
        request = self.factory.get('/events/search', {'search': 'test'})
        response = events_search(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)

if __name__ == '__main__':
    cov = coverage.Coverage(source=['.'])  # Specify the source directory
    cov.start()
    unittest.main()
    cov.stop()
    cov.save()
    cov.report()