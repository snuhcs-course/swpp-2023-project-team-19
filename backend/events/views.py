from rest_framework import status
from rest_framework.decorators import api_view, permission_classes
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticated

from .models import Event
from .serializers import EventSerializer

from datetime import datetime
from django.db.models import ExpressionWrapper, F, DateTimeField, Func, Value, CharField
from django.db.models.functions import Concat
from django.utils.timezone import make_aware

@api_view(['GET', 'POST'])
def event_list(request):
    # List all events or create a new one
    if request.method == 'GET':
        # Get current datetime in an aware format (considering timezone)
        now = make_aware(datetime.now())

        # Annotate queryset with a combined datetime field
        events = Event.objects.all().annotate(
            full_event_datetime=ExpressionWrapper(
                Func(
                    Concat(
                        F('event_date'), 
                        Value(' '),  # Space to separate date and time
                        F('event_time')
                    ),
                    function='CAST',
                    template='%(function)s(%(expressions)s as datetime)',
                ),
                output_field=DateTimeField()
            )
        )
        # Filter events happening after the current datetime
        future_events = events.filter(full_event_datetime__gt=now)

        # Serialize and return the filtered events
        serializer = EventSerializer(future_events, many=True)
        return Response(serializer.data)
    elif request.method == 'POST':
        serializer = EventSerializer(data=request.data)
        if serializer.is_valid():
            event = serializer.save()
            event_image = request.FILES.get('event_images')
            if event_image:
                event.event_image = event_image
                event.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    
@api_view(['GET'])
#@permission_classes([IsAuthenticated])  # Optional
def events_by_user(request, user_id):
    if request.method == 'GET':
        # Get current datetime in an aware format (considering timezone)
        now = make_aware(datetime.now())

        # Annotate queryset with a combined datetime field
        events = Event.objects.filter(host_id=user_id).annotate(
            full_event_datetime=ExpressionWrapper(
                Func(
                    Concat(
                        F('event_date'), 
                        Value(' '),  # Space to separate date and time
                        F('event_time')
                    ),
                    function='CAST',
                    template='%(function)s(%(expressions)s as datetime)',
                ),
                output_field=DateTimeField()
            )
        )

        # Filter events happening after the current datetime
        future_events = events.filter(full_event_datetime__gt=now)

        # Serialize and return the filtered events
        serializer = EventSerializer(future_events, many=True)
        return Response(serializer.data)

    return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)

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
#@permission_classes([IsAuthenticated])  # Optional
def events_by_user(request, user_id):
    if request.method == 'GET':
        # Get current datetime in an aware format (considering timezone)
        now = make_aware(datetime.now())

        # Annotate queryset with a combined datetime field
        events = Event.objects.filter(host_id=user_id).annotate(
            full_event_datetime=ExpressionWrapper(
                Func(
                    Concat(
                        F('event_date'), 
                        Value(' '),  # Space to separate date and time
                        F('event_time')
                    ),
                    function='CAST',
                    template='%(function)s(%(expressions)s as datetime)',
                ),
                output_field=DateTimeField()
            )
        )

        # Filter events happening after the current datetime
        future_events = events.filter(full_event_datetime__gt=now)

        # Serialize and return the filtered events
        serializer = EventSerializer(future_events, many=True)
        return Response(serializer.data)

    return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)

# Get & delete events by id
@api_view(['GET', 'DELETE'])
def events_by_id(request, event_id):
    events = Event.objects.filter(event_id=event_id)
    if request.method == 'GET':

        now = make_aware(datetime.now())

        # Annotate queryset with a combined datetime field
        events = Event.objects.filter(event_id = event_id).annotate(
            full_event_datetime=ExpressionWrapper(
                Func(
                    Concat(
                        F('event_date'), 
                        Value(' '),  # Space to separate date and time
                        F('event_time')
                    ),
                    function='CAST',
                    template='%(function)s(%(expressions)s as datetime)',
                ),
                output_field=DateTimeField()
            )
        )

        # Filter events happening after the current datetime
        future_events = events.filter(full_event_datetime__gt=now)

        if not future_events.exists():
            # If there are no future events, return 204 No Content
            return Response(status=status.HTTP_204_NO_CONTENT)

        # Serialize and return the filtered events
        serializer = EventSerializer(future_events, many=True)

        return Response(serializer.data)
    elif request.method == 'DELETE':
        events.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
    return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)

# Update request status in application
@api_view(['PUT'])
def increase_num_joined(request, event_id):
    try:
        event = Event.objects.get(event_id=event_id)
    except Event.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'PUT':
        event.event_num_joined += 1
        event.save()
        serializer = EventSerializer(event)
        return Response(serializer.data)
    
@api_view(['PUT'])
def decrease_num_joined(request, event_id):
    try:
        event = Event.objects.get(event_id=event_id)
    except Event.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'PUT':
        event.event_num_joined -= 1
        event.save()
        serializer = EventSerializer(event)
        return Response(serializer.data)

