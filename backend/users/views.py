from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView
from users.models import User
from users.serializers import UserSerializer, UserSerializer2
from users.utils import assign_random_avatar_image

from django.contrib.auth import authenticate
from rest_framework.authtoken.models import Token
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework import status
#from django.contrib.auth.models import User

from rest_framework.decorators import api_view

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
    def post(self, request, *args, **kwargs):
        email = request.data.get('email')
        password = request.data.get('password')
        #user = authenticate(username=email, password=password)
        #user = User.objects.create_user(username=email, password=password)

        try:
            user = User.objects.get(email=email)
        except User.DoesNotExist:
            return Response({'message': 'User not found.'}, status=status.HTTP_404_NOT_FOUND)

        if user.password == password:
        #if user is not None:
            #token, created = Token.objects.get_or_create(user=user)

            response_data = {
                'message': 'Login successful.',
                'user_id': user.user_id,
                #'token': token.key,
            }
            
            return Response(response_data, status=status.HTTP_200_OK)
        else:
            return Response({'message': 'Invalid password.'}, status=status.HTTP_401_UNAUTHORIZED)
        

    
        
#class UserInfo(APIView):
@api_view(['GET'])
#@permission_classes([IsAuthenticated])  # Optional: If you want to protect the endpoint
def get_userId(request, user_id):
        try:
            current_user = User.objects.get(user_id=user_id)
        except User.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        
        if request.method == 'GET':
            
            serializer = UserSerializer2(current_user)
            return Response(serializer.data)
        return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)


