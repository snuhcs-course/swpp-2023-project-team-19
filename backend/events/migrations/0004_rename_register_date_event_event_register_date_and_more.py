# Generated by Django 4.2.7 on 2023-11-03 11:56

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('events', '0003_alter_event_host_id'),
    ]

    operations = [
        migrations.RenameField(
            model_name='event',
            old_name='register_date',
            new_name='event_register_date',
        ),
        migrations.RenameField(
            model_name='event',
            old_name='register_time',
            new_name='event_register_time',
        ),
    ]
