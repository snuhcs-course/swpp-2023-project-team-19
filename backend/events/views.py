from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from .models import Event
from .serializers import EventSerializer

@api_view(['GET', 'POST'])
def event_list(request):
    # List all events or create a new one
    if request.method == 'GET':
        events = Event.objects.all()
        serializer = EventSerializer(events, many=True)
        return Response(serializer.data)
    elif request.method == 'POST':
        serializer = EventSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

# Get a list of events
@api_view(['GET', 'PUT', 'DELETE'])
def event_detail(request, pk):
    # Retrieve, update or delete an event
    try:
        event = Event.objects.get(pk=pk)
    except Event.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = EventSerializer(event)
        return Response(serializer.data)

    elif request.method == 'PUT':
        serializer = EventSerializer(event, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        event.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


@api_view(['GET'])
#@permission_classes([IsAuthenticated])  # Optional: If you want to protect the endpoint
def events_by_user(request, user_id):
    if request.method == 'GET':
        events = Event.objects.filter(host_id=user_id)
        serializer = EventSerializer(events, many=True)
        return Response(serializer.data)
    return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)

# Get & delete events by id
@api_view(['GET', 'DELETE'])
def events_by_id(request, event_id):
    events = Event.objects.filter(event_id=event_id)
    if request.method == 'GET':
        serializer = EventSerializer(events, many=True)
        return Response(serializer.data)
    elif request.method == 'DELETE':
        events.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
    return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)
