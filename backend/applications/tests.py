from django.test import TestCase
import unittest
import coverage

from django.test import TestCase
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APIRequestFactory, APIClient

from applications.models import Application
from applications.serializers import ApplicationSerializer
from applications.views import application, application_detail, user_application, events_application, check_application, accept_application

class ApplicationList(TestCase):
    @classmethod
    def setUpTestData(cls):
        Application.objects.create(event_id=1, host_id=1, applicant_id=1, applicant_name="test", applicant_contact="test_contact", message="test_message", request_status=0)

    def test_application_list(self):
        factory = APIRequestFactory()
        request = factory.get('application/')
        response = application(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_post_application(self):
        factory = APIRequestFactory()
        request = factory.post('application/', {'event_id': 1, 'host_id': 1, 'applicant_id': 1, 'applicant_name': "test", 'applicant_contact': "test_contact", 'message': "test_message", 'request_status': 0})
        response = application(request)
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)

    def test_application_list_wrong_method(self):
        factory = APIRequestFactory()
        request = factory.delete('application/')
        response = application(request)
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)

class ApplicationDetailTestCase(TestCase):
    def setUp(self):
        self.application = Application.objects.create(event_id=1, host_id=1, applicant_id=1, applicant_name="test", applicant_contact="test_contact", message="test_message", request_status=0)
    def test_get_application(self):
        client = APIClient()
        response = client.get('/application/1/')
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_get_application_not_exist(self):
        client = APIClient()
        response = client.get('/application/2/')
        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)
    
    def test_put_application(self):
        client = APIClient()
        response = client.put('/application/1/', {'event_id': 1, 'host_id': 1, 'applicant_id': 1, 'applicant_name': "test", 'applicant_contact': "test_contact", 'message': "test_message", 'request_status': 1})
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_put_application_not_exist(self):
        client = APIClient()
        response = client.put('/application/2/', {'event_id': 1, 'host_id': 1, 'applicant_id': 1, 'applicant_name': "test", 'applicant_contact': "test_contact", 'message': "test_message", 'request_status': 1})
        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)

    def test_delete_application(self):
        client = APIClient()
        response = client.delete('/application/1/')
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)
    
    def test_delete_application_not_exist(self):
        client = APIClient()
        response = client.delete('/application/2/')
        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)
    
class UserApplication(TestCase):
    @classmethod
    def setUpTestData(cls):
        Application.objects.create(event_id=1, host_id=1, applicant_id=1, applicant_name="test", applicant_contact="test_contact", message="test_message", request_status=0)

    def test_user_application(self):
        factory = APIRequestFactory()
        request = factory.get('application/by_user/1/')
        response = user_application(request, 1)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_user_application_wrong_method(self):
        factory = APIRequestFactory()
        request = factory.post('application/by_user/1/')
        response = user_application(request, 1)
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)

class EventApplication(TestCase):
    @classmethod
    def setUpTestData(cls):
        Application.objects.create(event_id=1, host_id=1, applicant_id=1, applicant_name="test", applicant_contact="test_contact", message="test_message", request_status=0)

    def test_event_application(self):
        factory = APIRequestFactory()
        request = factory.get('application/by_event/1/')
        response = events_application(request, 1)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_event_application_delete(self):
        factory = APIRequestFactory()
        request = factory.delete('application/by_event/1/')
        response = events_application(request, 1)
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)

    def test_event_application_wrong_method(self):
        factory = APIRequestFactory()
        request = factory.post('application/by_event/1/')
        response = events_application(request, 1)
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)

class CheckApplication(TestCase):
    @classmethod
    def setUpTestData(cls):
        Application.objects.create(event_id=1, host_id=1, applicant_id=1, applicant_name="test", applicant_contact="test_contact", message="test_message", request_status=0)

    def test_check_application(self):
        factory = APIRequestFactory()
        request = factory.get('application/check/1/1/')
        response = check_application(request, 1, 1)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_application_not_exist(self):
        factory = APIRequestFactory()
        request = factory.get('application/check/1/2/')
        response = check_application(request, 1, 2)
        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)
        self.assertEqual(response.data, {"message": "User has not applied for this event."})
    
    def test_check_application_delete(self):
        factory = APIRequestFactory()
        request = factory.delete('application/check/1/1/')
        response = check_application(request, 1, 1)
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)

class AcceptApplication(TestCase):
    @classmethod
    def setUpTestData(cls):
        Application.objects.create(event_id=1, host_id=1, applicant_id=1, applicant_name="test", applicant_contact="test_contact", message="test_message", request_status=0)

    def test_accept_application(self):
        factory = APIRequestFactory()
        request = factory.put('application/accept/1/1/')
        response = accept_application(request, 1, 1)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
    
    def test_accept_application_not_exist(self):
        factory = APIRequestFactory()
        request = factory.put('application/accept/1/2/')
        response = accept_application(request, 1, 2)
        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)
    
    def test_accept_application_wrong_method(self):
        factory = APIRequestFactory()
        request = factory.post('application/accept/1/1/')
        response = accept_application(request, 1, 1)
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)