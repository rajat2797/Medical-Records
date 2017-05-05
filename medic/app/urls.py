from django.conf.urls import url
from app.views import *

urlpatterns = [
	url(r'^$', index, name='index'),
	url(r'^createUser/$', createUser, name='createUser'),
	url(r'^loginUser/$', loginUser, name='loginUser'),
	url(r'^userProfile/$', userProfile, name='userProfile'),
	url(r'^createFolder/$', createFolder, name='createFolder'),
	url(r'^userFolder/$', userFolder, name='userFolder'),
	url(r'^uploadFile/$', uploadFile, name='uploadFile'),
	url(r'^folderData/$', folderData, name='folderData'),
]