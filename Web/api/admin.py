from django.contrib import admin
from .models import Accounts,Jobs,Applications,Contacts,Employer_Reviews,Freelancer_Ratings,Portfolios,Item_types,Default_Items,Portfolio_Items,Custom_items,Skill_Categories,Skills,Portfolio_Skills,Job_Requirement_Skills,EmailOTP

admin.site.register(Accounts)
admin.site.register(Jobs)
admin.site.register(Applications)
admin.site.register(Contacts)
admin.site.register(Employer_Reviews)
admin.site.register(Freelancer_Ratings)
admin.site.register(Portfolios)
admin.site.register(Item_types)
admin.site.register(Default_Items)
admin.site.register(Portfolio_Items)
admin.site.register(Custom_items)
admin.site.register(Skill_Categories)
admin.site.register(Skills)
admin.site.register(Portfolio_Skills)
admin.site.register(Job_Requirement_Skills)
admin.site.register(EmailOTP)


admin.site.site_header = "LIBRARIAN"     
admin.site.site_title = "Trang quản trị của Library of Freelancer"  
admin.site.index_title = "Chào mừng đến trang quản trị"