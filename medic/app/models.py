from __future__ import unicode_literals
from django.db import models
from django.contrib.auth.models import User

class Userinfo(models.Model):
	user = models.ForeignKey(User)
	name = models.CharField(max_length=50, blank=True)
	adhaarId = models.CharField(max_length=12, unique=True)
	gender = models.CharField(max_length=1, blank=True)
	dob = models.CharField(max_length=10, blank=True)

	def __unicode__(self):
		return self.user.username
