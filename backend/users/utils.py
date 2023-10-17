# users/utils.py

import random

def assign_random_avatar_image(user):
    photo_paths = [
        'avatar_image/picture1.png',
        'avatar_image/picture2.png',
        'avatar_image/picture3.png',
        'avatar_image/picture4.png',
        'avatar_image/picture5.png',
        'avatar_image/picture6.png',
        'avatar_image/picture7.png',
        'avatar_image/picture8.png',
        'avatar_image/picture9.png',
        'avatar_image/picture10.png',
    ]
    user.avatar = random.choice(photo_paths)
    user.save()
