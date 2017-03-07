from django.contrib import admin
from models import Userinfo

class UserinfoAdmin(admin.ModelAdmin):
	list_display = ('user', 'adhaarId', 'gender', )

admin.site.register(Userinfo, UserinfoAdmin)
