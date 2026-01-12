from django.urls import path

from . import views

urlpatterns = [
    path("", views.index, name="index"),
    path("login/", views.Account.login, name="login"),
    path("logout/", views.Account.logout, name="logout"),
    path("register/", views.Account.register, name="register"),
    path("load_list_application", views.General.load_list_application, name="load_list_application"),
    path("load_list_skill", views.General.load_list_skill, name="load_list_skill"),
    path("load_list_item", views.General.load_list_item, name="load_list_item"),
    path("freelancer/load_list_job", views.Freelancer.load_list_job, name="load_list_job_freelancer"),
    path("freelancer/load_list_job_by_search", views.Freelancer.load_list_job_by_search, name="load_list_job_by_search_freelancer"),
    path("freelancer/apply_job", views.Freelancer.apply_job, name="apply_job_freelancer"),
    path("freelancer/load_portfolio", views.Freelancer.load_portfolio, name="load_portfolio_freelancer"),
    path("employer/load_list_job", views.Employer.load_list_job, name="load_list_job_employer"),
    path("employer/create_job", views.Employer.create_job, name="create_job_employer"),

]