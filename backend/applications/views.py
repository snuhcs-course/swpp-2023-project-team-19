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

#get list of applications by user(applicant) id
@api_view(['GET'])
#@permission_classes([IsAuthenticated])  # Optional: If you want to protect the endpoint
def user_application(request, user_id):
    try:
        applications = Application.objects.filter(applicant_id=user_id)
    except Application.DoesNotExist:
        return Response({"message": "User has no applications."}, status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = ApplicationSerializer(applications, many=True)
        return Response(serializer.data)
    return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)

# Get & delete applications by event id
@api_view(['GET', 'DELETE'])
def events_application(request, event_id):
    try:
        applications = Application.objects.filter(event_id=event_id)
    except Application.DoesNotExist:
        return Response({"message": "No application for this event."}, status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = ApplicationSerializer(applications, many=True)
        return Response(serializer.data)
    elif request.method == 'DELETE':
        applications.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
    return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)

# check if user has applied for event
@api_view(['GET', 'DELETE'])
def check_application(request, user_id, event_id):
    try:
        application = Application.objects.get(applicant_id=user_id, event_id=event_id)
    except Application.DoesNotExist:
        return Response({"message": "User has not applied for this event."}, status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = ApplicationSerializer(application, many=False)
        return Response(serializer.data)
    
    elif request.method == 'DELETE':
        application.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
    
    return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)


# Update request status in application (status: 0 - pending, 1 - accepted, -1 - rejected)
@api_view(['PUT'])
def accept_application(request, application_id, status):
    try:
        application = Application.objects.get(application_id=application_id)
    except Application.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'PUT':
        application.request_status = status
        application.save()
        serializer = ApplicationSerializer(application)
        return Response(serializer.data)
    


