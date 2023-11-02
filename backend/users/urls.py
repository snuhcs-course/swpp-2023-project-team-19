from django.urls import path
from .views import UserSignup, UserLogin, UserAvatarView
from . import views

urlpatterns = [
    path('signup/', UserSignup.as_view(), name='user-signup'),
    path('login/', UserLogin.as_view(), name='user-login'),
    path('userinfo/<int:user_id>/', views.get_userId, name='user-info'),
    path('useravatar/<int:user_id>/', UserAvatarView.as_view(), name='user-image'),  # Add this line
]
