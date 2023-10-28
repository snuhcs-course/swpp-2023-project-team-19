# Generated by Django 4.2.6 on 2023-10-27 17:26

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('events', '0001_initial'),
    ]

    operations = [
        migrations.RenameField(
            model_name='event',
            old_name='number_of_people',
            new_name='event_num_participants',
        ),
        migrations.RemoveField(
            model_name='event',
            name='date_time',
        ),
        migrations.RemoveField(
            model_name='event',
            name='description',
        ),
        migrations.RemoveField(
            model_name='event',
            name='duration',
        ),
        migrations.RemoveField(
            model_name='event',
            name='host',
        ),
        migrations.RemoveField(
            model_name='event',
            name='language',
        ),
        migrations.RemoveField(
            model_name='event',
            name='location',
        ),
        migrations.RemoveField(
            model_name='event',
            name='price',
        ),
        migrations.RemoveField(
            model_name='event',
            name='thumbnail',
        ),
        migrations.RemoveField(
            model_name='event',
            name='title',
        ),
        migrations.AddField(
            model_name='event',
            name='event_date',
            field=models.DateField(blank=True, null=True),
        ),
        migrations.AddField(
            model_name='event',
            name='event_description',
            field=models.TextField(blank=True, null=True),
        ),
        migrations.AddField(
            model_name='event',
            name='event_duration',
            field=models.CharField(blank=True, max_length=255, null=True),
        ),
        migrations.AddField(
            model_name='event',
            name='event_language',
            field=models.CharField(blank=True, max_length=255, null=True),
        ),
        migrations.AddField(
            model_name='event',
            name='event_location',
            field=models.CharField(blank=True, max_length=255, null=True),
        ),
        migrations.AddField(
            model_name='event',
            name='event_num_joined',
            field=models.IntegerField(default=0),
        ),
        migrations.AddField(
            model_name='event',
            name='event_price',
            field=models.IntegerField(default=0),
        ),
        migrations.AddField(
            model_name='event',
            name='event_time',
            field=models.TimeField(blank=True, null=True),
        ),
        migrations.AddField(
            model_name='event',
            name='event_title',
            field=models.CharField(blank=True, max_length=255, null=True),
        ),
        migrations.AddField(
            model_name='event',
            name='event_type',
            field=models.CharField(blank=True, max_length=255, null=True),
        ),
        migrations.AddField(
            model_name='event',
            name='host_id',
            field=models.IntegerField(default=1),
        ),
    ]
