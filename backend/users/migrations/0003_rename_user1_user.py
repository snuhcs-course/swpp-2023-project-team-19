# Generated by Django 4.2.6 on 2023-10-28 07:27

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('users', '0002_rename_user_user1'),
    ]

    operations = [
        migrations.RenameModel(
            old_name='User1',
            new_name='User',
        ),
    ]