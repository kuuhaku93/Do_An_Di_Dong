from django.urls import path

from . import views

urlpatterns = [
    path("", views.index, name="index"),
    path("login", views.Account.login, name="login"),
    path("logout", views.Account.logout, name="logout"),
    path("register", views.Account.register, name="register"),
    path("send_otp", views.Account.send_otp, name="send_otp"),
    path("check_otp", views.Account.check_otp, name="check_otp"),
    path("change_password", views.Account.change_password, name="change_password"),
    path("load_list_application", views.General.load_list_application, name="load_list_application"),
    path("load_list_skill", views.General.load_list_skill, name="load_list_skill"),
    path("load_list_item", views.General.load_list_item, name="load_list_item"),
    path("freelancer/load_list_job", views.Freelancer.load_list_job, name="load_list_job_freelancer"),
    path("freelancer/load_list_job_by_search", views.Freelancer.load_list_job_by_search, name="load_list_job_by_search_freelancer"),
    path("freelancer/apply_job", views.Freelancer.apply_job, name="apply_job_freelancer"),
    path("freelancer/load_portfolio", views.Freelancer.load_portfolio, name="load_portfolio_freelancer"),
    path("freelancer/edit_portfolio", views.Freelancer.edit_portfolio, name="edit_portfolio_freelancer"),
    path("freelancer/load_current_job", views.Freelancer.load_current_job, name="load_current_job_freelancer"),
    path("freelancer/create_review", views.Freelancer.create_review, name="create_review_freelancer"),
    path("freelancer/load_rating", views.Freelancer.load_rating, name="load_rating_freelancer"),
    path("freelancer/load_history_job", views.Freelancer.load_history_job, name="load_history_job_freelancer"),
    path("employer/load_list_job", views.Employer.load_list_job, name="load_list_job_employer"),
    path("employer/create_job", views.Employer.create_job, name="create_job_employer"),
    path("employer/load_profile", views.Employer.load_profile, name="load_profile_employer"),
    path("employer/create_contact", views.Employer.create_contact, name="create_contact_employer"),
    path("employer/load_current_job", views.Employer.load_current_job, name="load_current_job_employer"),
    path("employer/create_rating", views.Employer.create_rating, name="create_rating_employer"),
    path("employer/load_review", views.Employer.load_review, name="load_review_employer"),
    path("employer/load_history_job", views.Employer.load_history_job, name="load_history_job_employer"),
]