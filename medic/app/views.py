from django.shortcuts import render
from django.http import HttpResponse, JsonResponse, Http404
from django.contrib.auth import authenticate, login
from django.contrib.auth.models import User
from django.contrib.auth.decorators import login_required
from models import Userinfo, UserFolders, TestFiles, PrescriptionFiles
import requests, base64
from django.views.decorators.csrf import csrf_exempt
from pprint import pprint

def index(request):
	return HttpResponse("Hello")

@csrf_exempt
def createUser(request):
	if request.method == 'POST':
		username = request.POST.get("username").lower()
		password = request.POST.get("password").lower()
		print "username = %s password = %s"%(username, password)
		d = {}
		if User.objects.filter(username=username).exists():
			d["status"] = "fail"
			d["message"] = "Username Already Exists"
			print "fail"
			return JsonResponse(d)
		
		if username is None or password is None:
			d["status"] = "fail"
			d["message"] = "Username Already Exists"
			print "fail"
			return JsonResponse(d)
			
		user = User.objects.create(username=username)
		user.set_password(password)
		u = Userinfo.objects.create(user=user)
		user.save()
		u.save()
		d["status"] = "success"
		d["message"] = "Registration Successful"
		d["uId"] = user.id
		print "success"
		return JsonResponse(d)

	else:
		raise Http404("Access Denied")

@csrf_exempt
def loginUser(request):
	if request.method=='POST':
		username = request.POST.get("username").lower()
		password = request.POST.get("password").lower()
		pprint("****username = %s ****password = %s"%(username, password))
		user = authenticate(username=username, password=password)
		print user
		if user is not None:
			print "********49*********"
			d = {
				"status" : "success",
				"message" : "Login Successful",
				"uId" : user.id
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

@csrf_exempt
def userProfile(request):
	if request.method == 'POST':

		uId = request.POST.get("uId")
		user = User.objects.get(id=uId)

		if request.POST.get("type") == 'update':
			name = request.POST.get("name")
			adhaarId = request.POST.get("adhaarId")
			gender = request.POST.get("gender")
			dob = request.POST.get("dob")

			print 'uId', uId
			print 'user', user
			try:
				u = Userinfo.objects.get(user=user)
				u.name = name
				u.adhaarId = adhaarId
				u.gender = gender
				u.dob = dob
				u.save()
				d = {
					"status" : "success",
					"message" : "Profile Updated"
				}

			except Exception as e:
				d= {
					"status" : "fail",
					"message" : str(e)
				}

		else:
			try:
				u =Userinfo.objects.get(user=user)
				d = {
					"status" : "success",
					"name" : u.name,
					"adhaarId" : u.adhaarId,
					"gender" : u.gender,
					"dob" : u.dob
				}

			except Exception as e:
				d= {
					"status" : "fail",
					"message" : str(e)
				}
		
		return JsonResponse(d)

	else:
		user = request.user
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

@csrf_exempt
def scanadhaar(request):
	pass

@csrf_exempt
def createFolder(request):
	if request.method == 'POST':
		try:
			user = User.objects.get(id=request.POST.get('uId'))
			name = request.POST.get('name')
			folder = UserFolders.objects.create(user=user, name=name)
			folder.save()
			d = {
				"status" : "success",
				"message" : "Folder created successfully"
			}

		except Exception as e:
			d = {
				"status" : "fail",
				"message" : str(e)
			}
		return JsonResponse(d)

	else:
		raise Http404("Access Denied")

@csrf_exempt
def userFolder(request):
	if request.method == 'POST':
		try:
			uId = request.POST.get('uId')
			folders = UserFolders.objects.filter(user=User.objects.get(id=uId))
			d={
				"status" : "success",
				"message" : "Fetching Data...",
				"folders" : []
			}
			for i in folders:
				d['folders'].append(i.name)

		except Exception as e:
			d = {
				"status" : "fail",
				"message" : str(e)
			}

		print d
		return JsonResponse(d)

	else:
		raise Http404("Access Denied")

@csrf_exempt
def uploadFile(request):
	if request.method == 'POST':
		try:
			uId = request.POST.get('uId')
			dataType = request.POST.get('dataType')
			image = request.POST.get('image')
			imgDesc = request.POST.get('imgDesc') or ''
			imgName = request.POST.get('imgName')
			folderName = request.POST.get('folderName')
			print 'uId', uId
			print 'dataType', dataType
			print 'imgName', imgName
			print 'folderName', folderName
			# print 'image', image
			
			print type(image)
			# image = base64.b64decode(image)
			
			folder = UserFolders.objects.filter(user=User.objects.get(id=uId), name=folderName)

			if dataType == 'tests':
				testModel = TestFiles.objects.create(
					folder=folder[0], file=image, name=imgName, desc=imgDesc)
				testModel.save()

			elif dataType == 'prescription':
				prescriptionModel = PrescriptionFiles.objects.create(
					folder=folder[0], file=image, name=imgName, desc=imgDesc)
				prescriptionModel.save()
			
			d = {
				"status" : "success",
				"message" : "Image uploaded successfully"
			}
				
		except Exception as e:
			d = {
				"status" : "fail",
				"message" : str(e)
			}

			if d['message'] == 'Request body exceeded settings.DATA_UPLOAD_MAX_MEMORY_SIZE.':
				d['message'] = 'File Size too big to upload'

		print d
		return JsonResponse(d)

'''
@csrf_exempt
def updateFile(request):
	if request.method == 'POST':
		try:
			uId = request.POST.get('uId')
			dataType = request.POST.get('dataType')
			image = request.POST.get('image')
			imgDesc = request.POST.get('imgDesc') or ''
			imgName = request.POST.get('imgName')
			folderName = request.POST.get('folderName')
			itemId = request.POST.get('itemId')
			print 'uId', uId
			print 'dataType', dataType
			print 'imgName', imgName
			print 'folderName', folderName
			# print 'image', image
			
			print type(image)
			# image = base64.b64decode(image)
			
			folder = UserFolders.objects.filter(user=User.objects.get(id=uId), name=folderName)

			if dataType == 'tests':
				testModel = TestFiles.objects.get(id=itemId, folder=folder[0])
				testModel.save()

			elif dataType == 'prescription':
				prescriptionModel = PrescriptionFiles.objects.create(
					folder=folder[0], file=image, name=imgName, desc=imgDesc)
				prescriptionModel.save()
			
			d = {
				"status" : "success",
				"message" : "Image uploaded successfully"
			}
				
		except Exception as e:
			d = {
				"status" : "fail",
				"message" : str(e)
			}

			if d['message'] == 'Request body exceeded settings.DATA_UPLOAD_MAX_MEMORY_SIZE.':
				d['message'] = 'File Size too big to upload'

		print d
		return JsonResponse(d)

'''

@csrf_exempt
def folderData(request):
	if request.method == 'POST':
		uId = request.POST.get('uId')
		dataType = request.POST.get('dataType')
		folderName = request.POST.get('folderName')
		
		folder = UserFolders.objects.filter(user=User.objects.get(id=uId), name=folderName)

		try:
			d={
				"status" : "success",
				"message" : "Fetching Data...",
				"images" : [],
				"names" : [],
				"desc" : [],
				"date" : [],
				"id" : []
			}

			if dataType == 'tests':
				testModels = TestFiles.objects.filter(folder=folder[0])
				for i in testModels:
					d['id'] = i.id
					d['images'].append(i.file)
					d['names'].append(i.name)
					if i.desc:
						d['desc'].append(i.desc)
					else:
						d['desc'].append("No Description")
					d['date'].append(str(i.date)[0:10])

			elif dataType == 'prescription':
				prescriptionModels = PrescriptionFiles.objects.filter(folder=folder[0])
				for i in prescriptionModels:
					d['id'] = i.id
					d['images'].append(i.file)
					d['names'].append(i.name)
					if i.desc:
						d['desc'].append(i.desc)
					else:
						d['desc'].append("No Description")
					d['date'].append(str(i.date)[0:10])
				
		except Exception as e:
			d = {
				"status" : "fail",
				"message" : str(e)
			}

		print d['message']
		return JsonResponse(d)
