# Generated by Django 4.2.7 on 2023-11-03 11:34

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('users', '0001_initial'),
        ('events', '0002_event_register_date_event_register_time'),
    ]

    operations = [
        migrations.AlterField(
            model_name='event',
            name='host_id',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='hosted_events', to='users.user'),
        ),
    ]