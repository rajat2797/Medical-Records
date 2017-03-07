from django.conf.urls import url
from app.views import *

urlpatterns = [
	url(r'^$', index, name='index'),
	url(r'^createUser/$', createUser, name='createUser'),
	url(r'^loginUser/$', loginUser, name='loginUser'),
	url(r'^userProfile/$', userProfile, name='userProfile'),
]