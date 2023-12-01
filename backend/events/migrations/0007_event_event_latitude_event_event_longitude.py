# Generated by Django 4.2.6 on 2023-11-30 05:19

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('events', '0006_event_event_images'),
    ]

    operations = [
        migrations.AddField(
            model_name='event',
            name='event_latitude',
            field=models.FloatField(blank=True, null=True),
        ),
        migrations.AddField(
            model_name='event',
            name='event_longitude',
            field=models.FloatField(blank=True, null=True),
        ),
    ]
