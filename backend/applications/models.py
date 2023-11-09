from django.db import models

from users.models import User

from datetime import date, time


# Create your models here.
class Application(models.Model):
    application_id = models.AutoField(primary_key=True)
    event_id = models.IntegerField(default=0)
    host_id = models.IntegerField(default=0)
    applicant_id = models.IntegerField(default=0)
    applicant_name  = models.CharField(max_length=255, null=True, blank=True)
    applicant_avatar = models.CharField(max_length=255, null=True, blank=True)
    applicant_contact = models.CharField(max_length=255, null=True, blank=True)
    message = models.TextField(null=True, blank=True)
    request_status = models.IntegerField(default=0)
    # 0: Pending , 1: Accepted, 2: Rejected
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now_add=True)
   