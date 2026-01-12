from django.shortcuts import render
from django.http import HttpResponse


def index(request):
    return HttpResponse("Hello, world. You're at the admin_page index.")

def Login(request):
    return render(request, 'login.html')