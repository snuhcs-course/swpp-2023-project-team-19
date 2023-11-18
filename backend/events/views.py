from rest_framework import status
from rest_framework.decorators import api_view, permission_classes
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticated

from .models import Event
from .serializers import EventSerializer

from datetime import datetime, timedelta
from django.db.models import ExpressionWrapper, F, DateTimeField, Func, Value, CharField
from django.db.models.functions import Concat
from django.utils.timezone import make_aware
from django.utils import timezone
from datetime import time

from django.shortcuts import render
from django.db.models import Q
from .models import Event

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
    return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)
    
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
    return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)

@api_view(['GET'])
def events_filter(request):
    if request.method == 'GET':
        # Extracting filter parameters from the GET request
        languages = request.GET.getlist('language')  # Returns a list of languages
        event_types = request.GET.getlist('event_type')  # Returns a list of event types
        dates = request.GET.getlist('date')  # Returns a list of dates
        times = request.GET.getlist('time')  # Returns a list of times
        is_free = request.GET.get('is_free')  # Returns 'true' or 'false' as a string


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
        queryset = events.filter(full_event_datetime__gt=now)

        # Applying language filter
        if languages:
            for language in languages:
                queryset = queryset.filter(event_language__icontains=language)

        # Applying event type filter
        if event_types:
            queryset = queryset.filter(event_type__in=event_types)

        # Applying date filter
        if dates:
            today = timezone.now().date()
            one_week_later = today + timedelta(days=7)
            eight_day = today + timedelta(days=8)
            two_week_later = today + timedelta(days=14)
        
            for date in dates:
                if (date == "This week"):
                    queryset = queryset.filter(event_date__range=(today, one_week_later))
                elif(date == "Next week"):
                    queryset = queryset.filter(event_date__range=(eight_day, two_week_later))
                else:
                    queryset = queryset.exclude(event_date__range=(today, two_week_later))

        # Applying time filter
        if times:
            for time_period in times:
                if (time_period == "Morning"):
                    queryset = queryset.filter(event_time__gte=time(5, 0, 0), event_time__lt=time(11, 59, 59))
                elif (time_period == "Afternoon"):
                    queryset = queryset.filter(event_time__gte=time(12, 0, 0), event_time__lt=time(18, 59, 59))
                else:
                    queryset = queryset.exclude(event_time__gte=time(5, 0, 0), event_time__lt=time(11, 59, 59))
                    queryset = queryset.exclude(event_time__gte=time(12, 0, 0), event_time__lt=time(18, 59, 59))

        # Applying price filter (assuming 'price' field exists in your model)
        if is_free is not None:
            if is_free.lower() == 'true':
                queryset = queryset.filter(event_price=0)
            else:
                queryset = queryset.exclude(event_price=0)

        # Serializing the data
        serializer = EventSerializer(queryset, many=True)
        return Response(serializer.data)
    return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)

@api_view(['GET'])
def events_search(request):
    if request.method == 'GET':
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
        queryset = events.filter(full_event_datetime__gt=now)

        # Retrieve the search keyword from the request
        search_keyword = request.GET.get('search', '')

        # Filter the queryset based on the search keyword
        if search_keyword:
            queryset = queryset.filter(event_title__icontains=search_keyword)

        serializer = EventSerializer(queryset, many=True)
        return Response(serializer.data)
    return Response(status=status.HTTP_405_METHOD_NOT_ALLOWED)
