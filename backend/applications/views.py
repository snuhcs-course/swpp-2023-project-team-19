from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from .models import Application
from .serializers import ApplicationSerializer

@api_view(['GET', 'POST'])
def application(request):
    # List all events or create a new one
    if request.method == 'GET':
        applications = Application.objects.all()
        serializer = ApplicationSerializer(applications, many=True)
        return Response(serializer.data)
    elif request.method == 'POST':
        serializer = ApplicationSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

# CRUD of application
@api_view(['GET', 'PUT', 'DELETE'])
def application_detail(request, pk):
    # Retrieve, update or delete an event
    try:
        applications = Application.objects.get(pk=pk)
    except Application.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = ApplicationSerializer(applications)
        return Response(serializer.data)

    elif request.method == 'PUT':
        serializer = ApplicationSerializer(applications, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        applications.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

#get list of applications by user id
@api_view(['GET'])
#@permission_classes([IsAuthenticated])  # Optional: If you want to protect the endpoint
def user_application(request, user_id):
    if request.method == 'GET':
        applications = Application.objects.filter(host_id=user_id)
        serializer = ApplicationSerializer(applications, many=True)
        return Response(serializer.data)
    return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)

# Get & delete applications by event id
@api_view(['GET', 'DELETE'])
def events_application(request, event_id):
    applications = Application.objects.filter(event_id=event_id)
    if request.method == 'GET':
        serializer = ApplicationSerializer(applications, many=True)
        return Response(serializer.data)
    elif request.method == 'DELETE':
        applications.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
    return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)


@api_view(['GET'])
def check_application(request, user_id, event_id):
    try:
        application = Application.objects.get(applicant_id=user_id, event_id=event_id)
        serializer = ApplicationSerializer(application, many=False)
        return Response(serializer.data)
    except Application.DoesNotExist:
        return Response({"message": "User has not applied for this event."}, status=status.HTTP_404_NOT_FOUND)


