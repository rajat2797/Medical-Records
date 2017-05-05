from __future__ import unicode_literals
from django.db import models
from django.contrib.auth.models import User
import base64

class Userinfo(models.Model):
	user = models.ForeignKey(User)
	name = models.CharField(max_length=50, blank=True)
	adhaarId = models.CharField(max_length=12, blank=True)
	gender = models.CharField(max_length=1, blank=True)
	dob = models.CharField(max_length=10, blank=True)

	def __unicode__(self):
		return self.user.username

class UserFolders(models.Model):
	user = models.ForeignKey(User)
	name = models.CharField(max_length=128, blank=False, unique=True)
	date = models.DateTimeField(auto_now_add=True)

	def __unicode__(self):
		return self.name
	
class TestFiles(models.Model):
	folder = models.ForeignKey(UserFolders)
	file = models.TextField(blank=False)
	name = models.CharField(max_length=128)
	date = models.DateTimeField(auto_now_add=True)
	desc = models.CharField(max_length=128, blank=True)

	def __unicode__(self):
		return self.name

class PrescriptionFiles(models.Model):
	folder = models.ForeignKey(UserFolders)
	file = models.TextField(blank=False)
	name = models.CharField(max_length=128)
	date = models.DateTimeField(auto_now_add=True)
	desc = models.CharField(max_length=128, blank=True)

	def __unicode__(self):
		return self.name