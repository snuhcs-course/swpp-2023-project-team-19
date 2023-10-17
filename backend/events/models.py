from django.db import models

from users.models import User

# Create your models here.
class Event(models.Model):
    event_id = models.AutoField(primary_key=True)
    host = models.ForeignKey(User, on_delete=models.CASCADE, related_name='hosted_events')
    thumbnail = models.CharField(max_length=255)
    title = models.CharField(max_length=255)
    number_of_people = models.IntegerField()
    date_time = models.DateTimeField()
    duration = models.FloatField()
    language = models.CharField(max_length=255)
    price = models.IntegerField()
    location = models.CharField(max_length=255)
    description = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)