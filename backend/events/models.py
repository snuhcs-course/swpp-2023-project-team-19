from django.db import models

from users.models import User

from datetime import date, time


# Create your models here.
class Event(models.Model):
    event_id = models.AutoField(primary_key=True)
    host_id = models.IntegerField(default=1)
    #host = models.ForeignKey(User, on_delete=models.CASCADE, related_name='hosted_events')
    event_type = models.CharField(max_length=255, null=True, blank=True)
    event_title = models.CharField(max_length=255, null=True, blank=True)
    event_num_participants = models.IntegerField()
    event_date = models.DateField(null=True, blank=True)
    event_time = models.TimeField(null=True, blank=True)
    event_duration = models.CharField(max_length=255, null=True, blank=True)
    event_language = models.CharField(max_length=255, null=True, blank=True)
    event_price = models.IntegerField(default=0)
    event_location = models.CharField(max_length=255, null=True, blank=True)
    event_description = models.TextField(null=True, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)
    event_num_joined = models.IntegerField(default=0)