from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView
from users.models import User
from users.serializers import UserSerializer
from users.utils import assign_random_avatar_image

class UserSignup(APIView):
    def post(self, request):
        serializer = UserSerializer(data=request.data)

        if serializer.is_valid():
            # Check if the email already exists
            email = serializer.validated_data['email']
            if User.objects.filter(email=email).exists():
                return Response({'message': 'Email already exists.'}, status=status.HTTP_400_BAD_REQUEST)

            user = serializer.save()
            assign_random_avatar_image(user)
            return Response({'message': 'User registered successfully.'}, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class UserLogin(APIView):
    def post(self, request):
        email = request.data.get('email')
        password = request.data.get('password')

        try:
            user = User.objects.get(email=email)
        except User.DoesNotExist:
            return Response({'message': 'User not found.'}, status=status.HTTP_404_NOT_FOUND)

        if user.password == password:
            response_data = {
                'message': 'Login successful.',
                'user_id': user.user_id,
            }
            return Response(response_data, status=status.HTTP_200_OK)
        else:
            return Response({'message': 'Invalid password.'}, status=status.HTTP_401_UNAUTHORIZED)