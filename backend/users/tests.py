import unittest
import coverage

from django.test import TestCase
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APIRequestFactory, APIClient

from users.models import User
from users.views import UserSignup, UserLogin, UserAvatarView
from users.views import get_userId
from users.serializers import UserSerializer2

# Create your tests here.
class UserModelTestCase(TestCase):
    @classmethod
    def setUpTestData(cls):
        User.objects.create(name='Test User', password='test', email='test@example.com', avatar = 'media/avatar_image/test.png')
    
    def test_user_creation(self):
        user = User.objects.get(name='Test User')
        self.assertNotEqual(user.user_id, None)
        self.assertEqual(user.name, 'Test User')
        self.assertEqual(user.password, 'test')
        self.assertNotEqual(user.created_at, None)
        self.assertEqual(user.email, 'test@example.com')
        self.assertEqual(user.avatar, 'media/avatar_image/test.png')

class UserSignupTest(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.signup_view = UserSignup.as_view()

    def test_signup_success(self):
        data = {
            'email': 'testuser@example.com',
            'password': 'testpassword',
            'name': 'Test User',
        }
        request = self.factory.post('/signup/', data)
        response = self.signup_view(request)
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)

    def test_signup_duplicate_email(self):
        User.objects.create(email='existing@example.com', password='testpassword')
        data = {
            'email': 'existing@example.com',
            'password': 'testpassword',
            'name': 'Test User',
        }
        request = self.factory.post('/signup/', data)
        response = self.signup_view(request)
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
        self.assertEqual(response.data['message'], 'Email already exists.')
    
    def test_signup_serializer_invalid(self):
        data = {
            'email': 'testuser@example.com',
            'password': '',  
            'name': 'Test User',
        }
        request = self.factory.post('/signup/', data)
        response = self.signup_view(request)
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)

class UserLoginTest(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.login_view = UserLogin.as_view()
    
    def test_login_success(self):
        # Create a user for testing
        user = User.objects.create(email='testuser@example.com', password='testpassword')
        data = {
            'email': 'testuser@example.com',
            'password': 'testpassword',
        }
        request = self.factory.post('/login/', data)
        response = self.login_view(request)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data['message'], 'Login successful.')
        self.assertEqual(response.data['user_id'], user.user_id)

    def test_login_user_not_found(self):
        data = {
            'email': 'nonexistent@example.com',
            'password': 'testpassword',
        }
        request = self.factory.post('/login/', data)
        response = self.login_view(request)
        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)
        self.assertEqual(response.data['message'], 'User not found.')

    def test_login_invalid_password(self):
        User.objects.create(email='testuser@example.com', password='testpassword')
        data = {
            'email': 'testuser@example.com',
            'password': 'wrongpassword',
        }
        request = self.factory.post('/login/', data)
        response = self.login_view(request)
        self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)
        self.assertEqual(response.data['message'], 'Invalid password.')

class UserAvatarViewTest(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.user = User.objects.create(user_id=1, avatar='media/avatar_image/test.png')
        self.avatar_view = UserAvatarView.as_view()

    def test_get_user_avatar_exists(self):
        user_id = self.user.user_id
        request = self.factory.get(f'/useravatar/{user_id}/')
        response = self.avatar_view(request, user_id=user_id)

        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertIn('avatar_url', response.data)

    def test_get_user_avatar_not_found(self):
        user_id = 999
        request = self.factory.get(f'/useravatar/{user_id}/')
        response = self.avatar_view(request, user_id=user_id)

        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)
        self.assertEqual(response.data['message'], 'User not found.')

    def test_get_user_avatar_no_avatar(self):
        user = User.objects.create(user_id=2)
        user_id = user.user_id
        request = self.factory.get(f'/useravatar/{user_id}/')
        response = self.avatar_view(request, user_id=user_id)

        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)
        self.assertEqual(response.data['message'], 'User has no avatar image.')

class GetUserIdTest(TestCase):
    def setUp(self):
        self.factory = APIRequestFactory()
        self.user = User.objects.create(user_id=1, name="Test User") 

    def test_get_user_info(self):
        request = self.factory.get(f'/userinfo/{self.user.user_id}/')
        response = get_userId(request, user_id=self.user.user_id)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        expected_data = UserSerializer2(self.user).data
        self.assertEqual(response.data, expected_data)

    def test_get_user_info_user_not_found(self):
        non_existing_user_id = 999 
        request = self.factory.get(f'/userinfo/{non_existing_user_id}/')
        response = get_userId(request, user_id=non_existing_user_id)
        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)

    def test_get_user_info_method_not_allowed(self):
        request = self.factory.delete(f'/userinfo/{self.user.user_id}/')
        response = get_userId(request, user_id=self.user.user_id)
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)

if __name__ == '__main__':
    cov = coverage.Coverage(source=['.'])  
    cov.start()
    unittest.main()
    cov.stop()
    cov.save()
    cov.report()