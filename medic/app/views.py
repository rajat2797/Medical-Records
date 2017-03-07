from django.shortcuts import render
from django.http import HttpResponse, JsonResponse, Http404
from django.contrib.auth import authenticate, login
from django.contrib.auth.models import User
from django.contrib.auth.decorators import login_required
from models import Userinfo
import requests

def index(request):
	return HttpResponse("Hello")

def createUser(request):
	if request.method == 'POST':
		username = request.POST["username"]
		password = request.POST["password"]
		d = {}
		if User.objects.filter(username=username).exists():
			d["status"] = "fail"
			d["message"] = "Username Already Exists"
			return JsonResponse(d)
		
		user = User.objects.create(username=username, password=password)
		d["status"] = "success"
		d["message"] = "Registration Successful"
		return JsonResponse(d)

	else:
		raise Http404("Access Denied")

def loginUser(request):
	if request.method=='POST':
		username = request.POST["username"]
		password = request.POST["password"]
		user = authenticate(username=username, password=password)
		if user is not None:
			d = {
				"status" : "success",
				"message" : "Login Successful",
				"user" : user
			}
			return JsonResponse(d)

		else:
			d = {
				"status" : "fail",
				"message" : "Invalid Username or Password",
				"user" : None
			}
			return JsonResponse(d)

	else:
		raise Http404("Access Denied")

def userProfile(request):
	if request.method == 'POST':
		user = request.POST['user']
		name = request.POST['name']
		adhaarId = request.POST['adhaarId']
		gender = request.POST['gender']
		dob = request.POST['dob']
		u = Userinfo.objects.get(user=user)
		u.name = name
		u.adhaarId = adhaarId
		u.gender = gender
		u.dob = dob
		u.save()
		d = {
			"status" : "succes",
			"message" : "Profile Updated"
		}
		return JsonResponse(d)

	else:
		user = request.GET['user']
		u = Userinfo.objects.get(user=user)
		d = {
			"status" : "success",
			"message" : "Profile Page",
			"name" : u.name,
			"gender" : u.gender,
			"adhaarId" : u.adhaarId,
			"dob" : u.dob
		}
		return JsonResponse(d)

def scanadhaar(request):
	pass
	# if request.method == 'POST':
	# 	adhaarId = request.POST['']


