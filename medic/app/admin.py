from django.contrib import admin
from models import Userinfo, UserFolders, TestFiles, PrescriptionFiles

class UserinfoAdmin(admin.ModelAdmin):
	list_display = ('user', 'adhaarId', 'gender', )

admin.site.register(Userinfo, UserinfoAdmin)
admin.site.register(UserFolders)
admin.site.register(TestFiles)
admin.site.register(PrescriptionFiles)