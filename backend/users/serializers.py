from rest_framework import serializers
from users.models import User


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields=('user_id', 'name', 'password', 'created_at', 'email', 'avatar')

class UserSerializer2(serializers.ModelSerializer):
    class Meta:
        model = User
        fields=('user_id', 'name', 'created_at', 'email', 'avatar')

class UserImageSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = '__all__'