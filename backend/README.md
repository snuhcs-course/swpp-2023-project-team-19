# Backend

## Project Structure

The structure of backend project includes these files 

```
backend/
    manage.py
    backend/
        __init__.py
        settings.py
        urls.py
        wsgi.py
    requirements.txt
```

These files are:
- The outer backend/ root directory is a container for your project. 
- The inner backend/ directory is the actual Python package for this project. Its name is the Python package name you must use when importing anything from it, like backend.urls.
- `manage.py` is a command-line utility that lets you interact with this Django project in various ways. The detailed could be refered via [https://docs.djangoproject.com/en/2.2/ref/django-admin/](https://docs.djangoproject.com/en/2.2/ref/django-admin/)
- `mysite/init.py`: This is an empty file that informs Python that this directory should be recognized as a Python package.
- `mysite/settings.py`: This file contains the settings and configuration for your Django project. 
- `mysite/urls.py`: This file contains the URL declarations for your Django project, essentially serving as a "table of contents" for your Django-powered site. For further information on URLs, you can consult the URL dispatcher documentation.
- `mysite/wsgi.py`: This file serves as an entry point for WSGI-compatible web servers to host your project.
- `requirements.txt`: This file specifies dependencies of the backend project. It's already included the common driver packages with specified version for setting up Python on Amazon Web Service (Django and mysqlclient).

## Installation 

To install the necessary libraries for this project from PyPI:
```
pip install -r requirements.txt
```

Run the following commands to start the services:
```
python manage.py runserver 
```
The web server will be live on `http://127.0.0.1:8000/`.
