from django.shortcuts import render
from django.http import JsonResponse, HttpResponse,HttpResponseBadRequest
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_GET, require_POST
from rest_framework.authtoken.models import Token
from django.contrib.auth import authenticate
from django.contrib.auth.hashers import make_password
from datetime import datetime
from django.core.mail import send_mail
from django.db import transaction
from django.db.models import Count,Q,Avg,OuterRef,Exists,F
import json,random,ast
from decouple import config
from .models import Accounts,Jobs,Applications,Contacts,Employer_Reviews,Freelancer_Ratings,Portfolios,Item_types,Default_Items,Portfolio_Items,Custom_items,Skill_Categories,Skills,Portfolio_Skills,Job_Requirement_Skills,EmailOTP


def index(request):
    return HttpResponse("You're at the api index.")

class Account:
    @csrf_exempt
    @require_POST
    def login(request):
        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')

        username = data.get('username')
        password = data.get('password') 
        if not username or not password:
            return JsonResponse({'success':False,'message': 'username or password is required.'}, status=400)
        user = authenticate(request, username=username, password=password)
        if user is None:
            return JsonResponse({'success':False,'message': 'username or password is incorrect.'}, status=400)
        if not user.is_active:
            return JsonResponse({'success':False,'message': 'account is not active.'}, status=400)
        # if Token.objects.filter(user=user).exists():
        #     return JsonResponse({'success':False,'message': 'account has been used.'}, status=400)

        token, created = Token.objects.get_or_create(user=user)
        account=Accounts.objects.get(id=user.id)
        return JsonResponse({'success':True,'token': token.key,'account_id':account.pk,'employer_status':account.employer_status,'freelancer_status':account.freelancer_status,'message': 'Logged in successfully.'}, status=200)

    @csrf_exempt
    @require_GET
    def logout(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)

        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)

        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)

        token.delete()
        return JsonResponse({'success':True,'message': 'Logged out successfully.'}, status=200)

    @csrf_exempt
    @require_POST
    def register(request):
        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')

        username = (data.get('username') or '').strip()
        password = data.get('password') or ''
        email = (data.get('email') or '').strip()
        full_name = (data.get('full_name') or '').strip()
        phone_number = (data.get('phone_number') or '').strip()


        # Validate cơ bản
        if not username or not password:
            return JsonResponse({'success':False,'message': 'username and password are required.'}, status=400)
        if not email or not full_name or not phone_number:
            return JsonResponse({'success':False,'message': 'information is missing.'}, status=400)
        if len(password) < 8:
            return JsonResponse({'success':False,'message': 'Password must be at least 8 characters long.'}, status=400)
        if Accounts.objects.filter(username=username).exists():
            return JsonResponse({'success':False,'message': 'Username already exists.'}, status=400)
        if email and Accounts.objects.filter(email=email).exists():
            return JsonResponse({'success':False,'message': 'Email is already in use.'}, status=400)
        user = Accounts(username=username, email=email, first_name=full_name)
        user.set_password(password)
        # Nếu model Account có các trường khác, gán ở đây
        user.full_name = full_name
        user.company_name = full_name  # Giả sử company_name giống full_name ban đầu
        user.phone_number = phone_number # Giá trị mặc định  
        user.save()
        # user.user_permissions.clear()
        # user.groups.clear()
        # user.save()
        portfolio=Portfolios(freelancer_id=user)
        portfolio.save()
        return JsonResponse({'success':True,'message': 'Đăng ký thành công.'}, status=201)
    
    @csrf_exempt
    @require_POST
    def send_otp(request):
        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')
        email = (data.get('email') or '').strip()
        try:
            user=Accounts.objects.get(email=email)
        except Accounts.DoesNotExist:
            return JsonResponse({'success': False, 'message': 'account does not exits'}, status=400)
        otp_code = str(random.randint(100000, 999999))
        EmailOTP.objects.create(user=user, otp_code=otp_code)
        subject = "Your OTP Code"
        message = f"Xin chào {user.username}, mã OTP của bạn là: {otp_code}"
        from_email = config('mail')
        recipient_list = [user.email]
        send_mail(subject, message, from_email, recipient_list)
        return JsonResponse({'success':True,'message': 'OTP send successfully.'}, status=200)

    @csrf_exempt
    @require_POST
    def check_otp(request):
        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'message': 'Invalid JSON'}), content_type='application/json')
        
        otp_code = data.get('otp')
        if not otp_code:
            return JsonResponse({'success': False, 'message': 'Missing fields'}, status=400)
        try:
            otp_obj = EmailOTP.objects.filter(otp_code=otp_code).latest('created_at')
        except EmailOTP.DoesNotExist:
            return JsonResponse({'success': False, 'message': 'Invalid OTP'}, status=400)
        if not otp_obj.is_valid():
            return JsonResponse({'success': False, 'message': 'OTP expired'}, status=400)
        return JsonResponse({'success': True, 'message': 'otp is correct '}, status=200)

    @csrf_exempt
    @require_POST
    def change_password(request):
        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'message': 'Invalid JSON'}), content_type='application/json')
        
        otp_code = data.get('otp')
        new_password = data.get('new_password')
        if not all([otp_code, new_password]):
            return JsonResponse({'success': False, 'message': 'Missing fields'}, status=400)
        try:
            otp_obj = EmailOTP.objects.filter(otp_code=otp_code).latest('created_at')
        except EmailOTP.DoesNotExist:
            return JsonResponse({'success': False, 'message': 'Invalid OTP'}, status=400)
        if not otp_obj.is_valid():
            return JsonResponse({'success': False, 'message': 'OTP expired'}, status=400)
        user=otp_obj.user
        user.password = make_password(new_password)
        user.save()
        return JsonResponse({'success': True, 'message': 'Password changed successfully'}, status=200)

class General:
    @csrf_exempt
    @require_GET
    def load_list_skill(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
            freelancer=token.user
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)

        list_skills = []
        list_type=Skill_Categories.objects.all()
        for cate in list_type:
            skills_in_category = Skills.objects.filter(category_id=cate)
            skill_list = [{'id': skill.id, 'skill_name': skill.skill_name} for skill in skills_in_category]
            list_skills.append({
                'type':cate.title,
                'skills': skill_list
            })
        return JsonResponse({'success':True,'skills':list_skills},status=200)

    @csrf_exempt
    @require_GET
    def load_list_item(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
            freelancer=token.user
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)

        items_list = []
        item_types = Item_types.objects.all()
        for item_type in item_types:
            default_qs = Default_Items.objects.filter(
                type_id=item_type
            ).select_related('type_id')

            default_list = [
                {
                    'id': pi.pk, 
                    'title': pi.title,
                    'description': pi.description,
                    'icon': pi.type_id.picture,
                    'start_year': 2000,
                    'end_year': 2000
                }
                for pi in default_qs
            ]

            custom_qs = Custom_items.objects.filter(type_id=item_type).select_related('type_id')
            custom_list = [
                {
                    'type_id': item_type.pk,
                    'title': ci.title,
                    'description': ci.description,
                    'icon': item_type.picture
                }
                for ci in custom_qs
            ]

            items_list.append(
                {
                    'type':item_type.name,
                    'type_id':item_type.pk,
                    'default': default_list,
                    'custom': custom_list
                }
            )
        return JsonResponse({'success':True,'items':items_list},status=200)

    @csrf_exempt
    @require_POST
    def load_list_application(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)

        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'message': 'Invalid JSON'}), content_type='application/json')

        job_id = data.get('job_id')
        if not job_id:
            return JsonResponse({'success':False,'message': 'job_id is required.'}, status=400)
        result = []
        applications = list(Applications.objects.filter(job_id=job_id).order_by('-applied_date'))
        for application in applications:
            result.append({
                'id': application.id,
                'freelancer_id':application.freelancer_id.pk,
                #'freelancer_avatar':application.freelancer_id.avatar,
                #'freelancer_name': application.freelancer_id.full_name,
                'description': application.description,
                'apply_status': application.apply_status,
                'wanted_salary': application.wanted_salary,
                'applied_date': application.applied_date,
                'skills': [{'skill_name':skill.skill.skill_name,'id':skill.skill.pk} for skill in Portfolio_Skills.objects.filter(portfolio_id__freelancer_id=application.freelancer_id)],
                'is_applied': Contacts.objects.filter(application_id=application).exists()
            })
        return JsonResponse({'success':True,'applications': result}, status=200)
        
class Freelancer:

    @csrf_exempt
    @require_GET
    def load_list_job(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
            freelancer=token.user
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)

        result = []
        list_jobs = list(Jobs.objects.filter(status=True).exclude(employer_id=freelancer).filter(current_employee__lt=F('max_employee')).order_by('-created_at')[:50])
        for job in list_jobs:
            result.append({
                'id': job.id,
                'employer_id':job.employer_id.pk,
                'employer_name': job.employer_id.company_name,
                'avatar': job.employer_id.company_logo,
                'title': job.title,
                'description': job.description,
                'salary_min': job.salary_min,
                'salary_max': job.salary_max,
                'publish_date': job.created_at,
                'location': job.location,
                'deadline': job.deadline,
                'max_employee': job.max_employee,
                'current_employee': job.current_employee,
                'requirements': [skill.skill_id.skill_name for skill in Job_Requirement_Skills.objects.filter(job_id=job)],
                'is_applied': Applications.objects.filter(job_id=job, freelancer_id=freelancer).exists()
            })
        return JsonResponse({'success':True,'jobs': result}, status=200)

    @csrf_exempt
    @require_POST
    def load_list_job_by_search(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)
        freelancer=token.user

        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')
        keyword = data.get('keyword','').strip()
        requirement_ids = ast.literal_eval(data.get('requirement', '[]'))
        print(data)
        print(keyword)
        print(requirement_ids)
        try:
            requirement_ids = [int(x) for x in requirement_ids]
        except (TypeError, ValueError):
            return JsonResponse({'success':False,'message': 'requirement must be a list of integer ids'}, status=400)

        qs=Jobs.objects.filter(status=True).exclude(employer_id=freelancer).filter(current_employee__lt=F('max_employee'))
        if keyword:
            qs = qs.filter(Q(title__icontains=keyword) | Q(employer_id__company_name__icontains=keyword))
        if requirement_ids:
            qs = qs.filter(job_requirement_skills__skill_id__in=requirement_ids) \
               .annotate(matching_skills=Count('job_requirement_skills__skill_id', distinct=True)) \
               .filter(matching_skills=len(requirement_ids))
        qs = qs.order_by('-created_at')[:50]
        result = []
        for job in qs:
            result.append({
                'id': job.id,
                'employer_id':job.employer_id.pk,
                'employer_name': job.employer_id.company_name,
                'avatar': job.employer_id.company_logo,
                'title': job.title,
                'description': job.description,
                'salary_min': job.salary_min,
                'salary_max': job.salary_max,
                'publish_date': job.created_at,
                'location': job.location,
                'deadline': job.deadline,
                'max_employee': job.max_employee,
                'current_employee': job.current_employee,
                'requirements': [skill.skill_id.skill_name for skill in Job_Requirement_Skills.objects.filter(job_id=job)],
                'is_applied': Applications.objects.filter(job_id=job, freelancer_id=freelancer).exists()
            })
        return JsonResponse({'success':True,'jobs': result}, status=200)

    @csrf_exempt
    @require_POST
    def apply_job(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)

        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')

        freelancer=token.user
        description = data.get('description','').strip()
        wanted_salary = data.get('wanted_salary')
        job_id=data.get('job_id')

        if wanted_salary is None:
            return JsonResponse({'success':False,'message': 'wanted_salary is required.'}, status=400)
        if job_id is None:
            return JsonResponse({'success':False,'message': 'job_id is required.'}, status=400)
        try:
            job=Jobs.objects.get(id=job_id)
        except Jobs.DoesNotExist:
            return JsonResponse({'success':False,'message': 'job does not exits.'}, status=400)
        
        apply=Applications(
            job_id=job,
            freelancer_id=freelancer,
            description=description,
            wanted_salary=wanted_salary
        )
        apply.save()
        return JsonResponse({'success':True,'message': 'Application submitted successfully.'}, status=200)

    @csrf_exempt
    @require_POST
    def load_portfolio(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)

        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')

        freelancer=data.get('freelancer_id')
        try:
            portfolio=Portfolios.objects.get(freelancer_id=freelancer)
        except Portfolios.DoesNotExist:
            return JsonResponse({'success':False,'message': 'freelancer does not exist portfolio.'}, status=400)
        
        items_list = []
        item_types = Item_types.objects.all()
        for item_type in item_types:
            default_qs = Portfolio_Items.objects.filter(
                portfolio_id=portfolio,
                item_id__type_id=item_type
            ).select_related('item_id', 'item_id__type_id')

            default_list = [
                {
                    'id': pi.item_id.pk, 
                    'title': pi.item_id.title,
                    'description': pi.item_id.description,
                    'icon': pi.item_id.type_id.picture,
                    'start_year': pi.start_year,
                    'end_year': pi.end_year
                }
                for pi in default_qs
            ]

            custom_qs = Custom_items.objects.filter(portfolio_id=portfolio, type_id=item_type).select_related('type_id')
            custom_list = [
                {
                    'type_id': item_type.pk,
                    'title': ci.title,
                    'description': ci.description,
                    'icon': item_type.picture
                }
                for ci in custom_qs
            ]

            items_list.append(
                {
                    'type_id':item_type.pk,
                    'type':item_type.name,
                    'default': default_list,
                    'custom': custom_list
                }
            )


        skills_list = []
        for cate in Skill_Categories.objects.all():
            skills_qs = Portfolio_Skills.objects.filter(portfolio_id=portfolio, skill__category_id=cate).select_related('skill')
            skills = [{'skill_name':ps.skill.skill_name,'id':ps.skill.pk} for ps in skills_qs]
            skills_list.append({'type': cate.title,'skills':skills})


        portfolio_obj={
            'freelancer_name': portfolio.freelancer_id.full_name,
            'avatar': portfolio.freelancer_id.avatar,
            'email': portfolio.freelancer_id.email,
            'phone_number': portfolio.freelancer_id.phone_number,
            'description': portfolio.description,
            'complete': portfolio.complete,
            'rating':portfolio.rating,
            'items': items_list,
            'skills': skills_list
        }
        return JsonResponse({'success':True,'portfolio': portfolio_obj}, status=200)

    @csrf_exempt
    @require_POST
    def edit_portfolio(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)

        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')
        print(data)
        freelancer=token.user
        freelancer_name = data.get('freelancer_name','').strip()
        avatar = data.get('avatar','').strip()
        email = data.get('email','').strip()
        phone_number = data.get('phone_number','').strip()
        description = data.get('description','').strip()
        skills=ast.literal_eval(data.get('skills', '[]'))
        items=ast.literal_eval(data.get('items', '[]'))
        custom=ast.literal_eval(data.get('customs', '[]'))

        try:
            portfolio = Portfolios.objects.get(freelancer_id=freelancer)
        except Portfolios.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Portfolio not found'}, status=404)

        with transaction.atomic():
            if freelancer_name:
                freelancer.full_name = freelancer_name
            if avatar:
                freelancer.avatar = avatar
            if phone_number:
                freelancer.phone_number = phone_number
            freelancer.save()

            if description is not None:
                portfolio.description = description
            portfolio.save()

            try:
                skill_ids = [int(x) for x in skills]
            except (TypeError, ValueError):
                return JsonResponse({'success':False,'message': 'listskill must be list of int'}, status=400)

            Portfolio_Skills.objects.filter(portfolio_id=portfolio).exclude(skill_id__in=skill_ids).delete()

            existing_skill_ids = set(Portfolio_Skills.objects.filter(portfolio_id=portfolio).values_list('skill_id', flat=True))
            for sid in skill_ids:
                if sid not in existing_skill_ids:
                    Portfolio_Skills.objects.create(portfolio_id=portfolio, skill=Skills.objects.get(pk=sid))

            try:
                keep_ids = [int(i['id']) for i in items]
            except Exception:
                return JsonResponse({'success':False,'message': 'listitem must be list of dicts with id'}, status=400)

            Portfolio_Items.objects.filter(portfolio_id=portfolio).exclude(item_id__in=keep_ids).delete()
            for i in items:
                try:
                    iid = int(i.get('id',0))
                    start_year = int(i.get('start_year', 2000))
                    end_year = int(i.get('end_year', 2000))
                except Exception:
                    continue
                Portfolio_Items.objects.update_or_create(
                    portfolio_id=portfolio,
                    item_id_id=iid,
                    defaults={'start_year': start_year, 'end_year': end_year}
                )

            Custom_items.objects.filter(portfolio_id=portfolio).delete()
            new_custom_objs = []
            for c in custom:
                title = c.get('title', '')
                desc = c.get('description', '')
                type_id = c.get('type_id')
                type_obj = None
                if type_id:
                    try:
                        type_obj = Item_types.objects.get(id=int(type_id))
                    except (Item_types.DoesNotExist, ValueError, TypeError):
                        type_obj = None
                new_custom_objs.append(Custom_items(
                    portfolio_id=portfolio,
                    title=title,
                    description=desc,
                    type_id=type_obj
                ))
            if new_custom_objs:
                Custom_items.objects.bulk_create(new_custom_objs)

        # --- Build response ---
        listItem = {}
        for item_type in Item_types.objects.all():
            default_list = [
                {'title': pi.item_id.title, 'description': pi.item_id.description, 'icon': pi.item_id.type_id.picture,
                'start_year': pi.start_year, 'end_year': pi.end_year}
                for pi in Portfolio_Items.objects.filter(portfolio_id=portfolio, item_id__type_id=item_type)
            ]
            for item in Custom_items.objects.filter(portfolio_id=portfolio, type_id=item_type):
                default_list.append({'custom': item.title, 'description': item.description, 'icon': item.type_id.picture})
            listItem[item_type.name] = default_list

        listSkill = {}
        for cat in Skill_Categories.objects.all():
            listSkill[cat.title] = [ps.skill.skill_name for ps in Portfolio_Skills.objects.filter(portfolio_id=portfolio, skill__category_id=cat)]

        portfolio_obj = {
            'freelancer_name': portfolio.freelancer_id.full_name,
            'avatar': portfolio.freelancer_id.avatar,
            'email': portfolio.freelancer_id.email,
            'phone_number': portfolio.freelancer_id.phone_number,
            'description': portfolio.description,
            'complete': portfolio.complete,
            'rating': portfolio.rating,
            'items': listItem,
            'skills': listSkill
        }

        return JsonResponse({'success':True,'message': 'Portfolio updated successfully', 'portfolio': portfolio_obj}, status=200)

    @csrf_exempt
    @require_GET
    def load_current_job(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)
        freelancer=token.user

        contacts_base = Contacts.objects.filter(application_id__freelancer_id=freelancer)
        review_exists = Employer_Reviews.objects.filter(contact_id=OuterRef('pk'))
        contacts_qs = contacts_base.annotate(has_review=Exists(review_exists)).filter(Q(current_status=True) | (Q(current_status=False) & Q(has_review=False))).select_related('application_id__job_id','application_id__freelancer_id').order_by('-created_at').distinct()

        result = []
        for contact in contacts_qs:
            result.append({
                'contact_id':contact.pk,
                'job_title':contact.application_id.job_id.title,
                'company_id':contact.application_id.job_id.employer_id.pk,
                # 'company_name':contact.application_id.job_id.employer_id.company_name,
                # 'company_logo':contact.application_id.job_id.employer_id.company_logo,
                'is_done':not contact.current_status,
                'is_rating':bool(contact.has_review)
            })

        return JsonResponse({'success':True,'jobs': result}, status=200)
        
    @csrf_exempt
    @require_POST
    def create_review(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)
        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')

        freelancer=token.user
        contact_id=data.get("contact_id")
        comment=data.get('comment','').strip()
        score_raw=data.get('score')
        
        try:
            score = float(score_raw) if score_raw is not None else 0.0
        except (TypeError, ValueError):
            return JsonResponse({'success': False, 'message': 'score must be a number'}, status=400)
        if score < 0 or score > 5:
            return JsonResponse({'success': False, 'message': 'score must be between 0 and 5'}, status=400)

        if not contact_id:
            return JsonResponse({'success':False,'message': 'contact id is required'}, status=400)
        try:
            contact = Contacts.objects.select_related('application_id__job_id__employer_id').get(pk=contact_id)
        except Contacts.DoesNotExist:
            return JsonResponse({'success': False, 'message': 'Contact not found'}, status=404)
        if Employer_Reviews.objects.filter(contact_id=contact).exists():
            return JsonResponse({'success': False, 'message': 'Review already exists for this contact'}, status=400)
        if contact.current_status:
            return JsonResponse({'success': False, 'message': 'Contact is not complete'}, status=404)     
        if contact.application_id.freelancer_id != freelancer:
            return JsonResponse({'success':False,'message': 'user is not have permission'}, status=400)
        review=Employer_Reviews(contact_id=contact,comment=comment,score=score)
        review.save()
        return JsonResponse({'success':True,'message': 'Review created successfully'}, status=200)
   
    @csrf_exempt
    @require_POST
    def load_rating(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)

        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')

        freelancer_id=data.get("freelancer_id")
        
        if not freelancer_id:
            return JsonResponse({'success':False,'message': 'employer id is required'}, status=400)
        freelancer=Accounts.objects.get(pk=freelancer_id)
        rating_qs = Freelancer_Ratings.objects.filter(contact_id__application_id__freelancer_id=freelancer,status=True).select_related('contact_id__application_id__job_id__employer_id','contact_id__application_id__job_id','contact_id__application_id__freelancer_id','contact_id__application_id','contact_id').distinct()
        if not rating_qs.exists():
            return JsonResponse({'success': True, 'ratings': []}, status=200)
        result = []
        for rating in rating_qs:
            result.append({
                'rating_id':rating.pk,
                'comment':rating.comment,
                'rating':rating.rating,
                'complete':rating.complete,
                'created_at':rating.created_at,
                'employer_id': rating.contact_id.application_id.job_id.employer_id.pk,
                'employer_name': rating.contact_id.application_id.job_id.employer_id.company_name,
                'employer_avatar':rating.contact_id.application_id.job_id.employer_id.company_logo,
            })
        return JsonResponse({'success':True,'ratings': result}, status=200)
        
    @csrf_exempt
    @require_GET
    def load_history_job(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)
        freelancer=token.user

        contacts_qs = Contacts.objects.filter(application_id__freelancer_id=freelancer,current_status=False).select_related('application_id__job_id','application_id__freelancer_id','application_id').distinct()
        if not contacts_qs.exists():
            return JsonResponse({'success': True, 'contacts': []}, status=200)
        result = []
        for contact in contacts_qs:
            rating = Freelancer_Ratings.objects.filter(contact_id=contact).first()
            review = Employer_Reviews.objects.filter(contact_id=contact).first()

            if not rating or not review:
                continue # Skip if either rating or review is missing

            job = contact.application_id.job_id
            employer = job.employer_id

            result.append({
                'contact_id': contact.pk,
                'job_title': job.title,
                'score': review.score,
                'complete': rating.complete,
                'company_name': employer.company_name,
                'company_avatar': employer.company_logo,
                'employer_id': employer.pk,
                'start_date': contact.start_date,
                'end_date': contact.end_date
            })
        return JsonResponse({'success':True,'jobs': result}, status=200)
        
class Employer:
    @csrf_exempt
    @require_GET
    def load_list_job(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)
        employer=token.user

        result = []
        list_jobs = list(Jobs.objects.filter(status=True,employer_id=employer).order_by('-created_at')[:20])
        for job in list_jobs:
            result.append({
                'id': job.id,
                'employer_name': job.employer_id.company_name,
                'avatar': job.employer_id.company_logo,
                'title': job.title,
                'description': job.description,
                'salary_min': job.salary_min,
                'salary_max': job.salary_max,
                'publish_date': job.created_at,
                'location': job.location,
                'deadline': job.deadline,
                'max_employee': job.max_employee,
                'current_employee': job.current_employee,
                'requirements': [skill.skill_id.skill_name for skill in Job_Requirement_Skills.objects.filter(job_id=job)],
                'status':job.status
            })
        return JsonResponse({'success':True,'jobs': result}, status=200)

    @csrf_exempt
    @require_POST
    def create_job(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)

        employer=token.user
        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')
        
        title=data.get('title','').strip()
        description=data.get('description','').strip()
        salary_min=data.get('salary_min',0)
        salary_max=data.get('salary_max',0)
        location=data.get('location','').strip()
        deadline_str=data.get('deadline')
        end_date_str=data.get('end_date')
        max_employee=data.get('max_employee',1)

        if not title:
            return JsonResponse({'success':False,'message': 'title is required.'}, status=400)
        if not max_employee or max_employee<=0:
            return JsonResponse({'success':False,'message': 'max_employee is required and must be greater than 0.'}, status=400)
        try:
            deadline = datetime.strptime(deadline_str, "%H:%M:%S %d-%m-%Y") if deadline_str else None
            end_date = datetime.strptime(end_date_str, "%H:%M:%S %d-%m-%Y") if end_date_str else None
        except ValueError:
            return JsonResponse({'success':False,'message': 'Invalid date format. Use ISO format YYYY-MM-DD'}, status=400)
        
        new_job=Jobs(
            employer_id=employer,
            title=title,
            description=description,
            salary_min=salary_min,
            salary_max=salary_max,
            location=location,
            deadline=deadline,
            end_date=end_date,
            max_employee=max_employee,
            current_employee=0,
            status=True
        )

        new_job.save()
        requirements=ast.literal_eval(data.get('requirements', '[]'))
        for req in requirements:
            skill_obj=Skills.objects.filter(id=req).first()
            if skill_obj:
                job_req=Job_Requirement_Skills(
                    job_id=new_job,
                    skill_id=skill_obj
                )
                job_req.save()
        return JsonResponse({'success':True,'message': 'Job created successfully.'}, status=200)

    @csrf_exempt
    @require_POST
    def load_profile(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)
        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')

        employer=data.get("employer_id")
        if not employer:
            return JsonResponse({'success':False,'message': 'Employer id is required'}, status=400)
        
        try :
            profile=Accounts.objects.get(pk=employer)
        except Accounts.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Employer not found'}, status=404)
        qs=Employer_Reviews.objects.filter(status=True,contact_id__application_id__job_id__employer_id=employer).aggregate(avg_score=Avg('score'))
        rating = qs['avg_score']
        if rating is None:
            rating=0.0

        result={
            'company_name':profile.company_name,
            'company_logo':profile.company_logo,
            'email':profile.email,
            'phone_number':profile.phone_number,
            'website':profile.website,
            'address':profile.address,
            'employer_description':profile.employer_description,
            'rating':rating
        }
        return JsonResponse({'success':True,'profile':result}, status=200)
    
    @csrf_exempt
    @require_POST
    def edit_profile(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)
        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')

        

        employer_id=data.get("employer_id")
        if not employer_id:
            return JsonResponse({'success':False,'message': 'Employer id is required'}, status=400)
        try:
            user = Accounts.objects.get(pk=employer_id)
        except Accounts.DoesNotExist:
            return JsonResponse({'success':False,'message': 'User not found'}, status=404)
        if user.pk != employer_id:
            return JsonResponse({'success':False,'message': 'User does not have permission'}, status=400)
            
        company_name=data.get('company_name','').strip()
        company_logo=data.get('company_logo','').strip()
        email=data.get('email','').strip()
        phone_number=data.get('phone_number','').strip()
        website=data.get('website','').strip()
        address=data.get('address','').strip()
        employer_description=data.get('employer_description','').strip()
        if not company_name:
            return JsonResponse({'success':False,'message': 'company_name is required.'}, status=400)
        if not email:
            return JsonResponse({'success':False,'message': 'email is required.'}, status=400)
        if Accounts.objects.filter(email=email).exists() and email != user.email:
            return JsonResponse({'success':False,'message': 'email is already in use.'}, status=400)
        if phone_number and len(phone_number) < 10:
            return JsonResponse({'success':False,'message': 'phone_number is invalid.'}, status=400)
        with transaction.atomic():
            user.company_name=company_name
            user.company_logo=company_logo
            user.email=email
            user.phone_number=phone_number
            user.website=website
            user.address=address
            user.employer_description=employer_description
            user.save()

        return JsonResponse({'success':True,'message':'Profile updated successfully'}, status=200)

    @csrf_exempt
    @require_POST
    def create_contact(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)
        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')

        user=token.user
        application_id=data.get("application_id")
        start_date_str=data.get("start_date")
        end_date_str=data.get("end_date")

        try:
            start_date = datetime.strptime(start_date_str, "%H:%M:%S %d-%m-%Y") if start_date_str else None
            end_date = datetime.strptime(end_date_str, "%H:%M:%S %d-%m-%Y") if end_date_str else None
        except ValueError:
            return JsonResponse({'success':False,'message': 'Invalid date format. Use ISO format YYYY-MM-DD'}, status=400)
        try:
            application=Applications.objects.select_related('job_id','freelancer_id').get(pk=application_id)
        except Applications.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Application not found'}, status=404)
        
        job = application.job_id
        with transaction.atomic():
            job = Jobs.objects.select_for_update().get(pk=job.pk)
            if job.employer_id_id != user.id:
                return JsonResponse({'success': False, 'message': 'User does not have permission'}, status=400)
            if not application.status:
                return JsonResponse({'success': False, 'message': 'Application is not in applied state'}, status=400)
            if job.current_employee >= job.max_employee:
                return JsonResponse({'success': False, 'message': 'This job has reached max employees'}, status=400)
            if job.status is False:
                return JsonResponse({'success': False, 'message': 'This job is closed'}, status=400)

        if Contacts.objects.filter(application_id=application).exists():
            return JsonResponse({'success': False, 'message': 'Contact already exists for this application'}, status=400)

        contact = Contacts.objects.create(application_id=application,start_date=start_date,end_date=end_date,current_status=True)
        job.current_employee = F('current_employee') + 1
        job.save()
        job.refresh_from_db(fields=['current_employee'])
        application.apply_status = True
        application.save()
        return JsonResponse({'success':True,'message': 'Contact created successfully'}, status=200)

    @csrf_exempt
    @require_POST
    def close_job(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)
        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')

        user=token.user
        job_id=data.get("job_id")
        if not job_id:
            return JsonResponse({'success':False,'message': 'Job id is required'}, status=400)

        with transaction.atomic():
            try:
                job = Jobs.objects.get(pk=job_id)
            except Jobs.DoesNotExist:
                return JsonResponse({'success':False,'message': 'Jobs does not exist.'}, status=400) 
            if job.employer_id_id != user.id:
                return JsonResponse({'success': False, 'message': 'User does not have permission'}, status=400)
            if job.status is False:
                return JsonResponse({'success': False, 'message': 'This job is closed'}, status=400)

        job.max_employee=job.current_employee
        job.status=False
        job.save()

        return JsonResponse({'success':True,'message': 'Job closed successfully'}, status=200)

    @csrf_exempt
    @require_GET
    def load_current_job(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)
        
        employer=token.user

        contacts_qs = Contacts.objects.filter(application_id__job_id__employer_id=employer,current_status=True).select_related('application_id__job_id','application_id__freelancer_id','application_id').distinct()
        if not contacts_qs.exists():
            return JsonResponse({'success': True, 'jobs': []}, status=200)

        result = []
        for contact in contacts_qs:
            job = contact.application_id.job_id
            freelancer = contact.application_id.freelancer_id
            result.append({
                'contact_id':contact.pk,
                'job_title':job.title,
                'company_name':job.employer_id.company_name,
                'freelancer_id': freelancer.id,
                # 'freelancer_name': freelancer.full_name,
                # 'freelancer_avatar':freelancer.avatar,
                'start_date': contact.start_date.isoformat() if contact.start_date else None,
                'end_date': contact.end_date.isoformat() if contact.end_date else None,
            })

        return JsonResponse({'success':True,'jobs': result}, status=200)
        
    @csrf_exempt
    @require_POST
    def create_rating(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)
        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')

        user=token.user
        contact_id=data.get("contact_id")
        comment=data.get('comment','').strip()
        complete=bool(data.get('complete','False'))
        rating_raw=float(data.get('rating',0))
        
        try:
            rating = float(rating_raw) if rating_raw is not None else 0.0
        except (TypeError, ValueError):
            return JsonResponse({'success': False, 'message': 'rating must be a number'}, status=400)
        if rating < 0 or rating > 5:
            return JsonResponse({'success': False, 'message': 'rating must be between 0 and 5'}, status=400)

        if not contact_id:
            return JsonResponse({'success':False,'message': 'contact id is required'}, status=400)
        try:
            contact = Contacts.objects.select_related('application_id__job_id__employer_id').get(pk=contact_id)
        except Contacts.DoesNotExist:
            return JsonResponse({'success': False, 'message': 'Contact not found'}, status=404)
        if Freelancer_Ratings.objects.filter(contact_id=contact).exists():
            return JsonResponse({'success': False, 'message': 'Rating already exists for this contact'}, status=400)
        if contact.application_id.job_id.employer_id != user:
            return JsonResponse({'success':False,'message': 'user is not have permision'}, status=400)
        contact.current_status=False
        contact.save()
        vote=Freelancer_Ratings(contact_id=contact,comment=comment,rating=rating,complete=complete)
        vote.save()
        portfolio=Portfolios.objects.get(freelancer_id=contact.application_id.freelancer_id)

        avg_agg = Freelancer_Ratings.objects.filter(contact_id__application_id__freelancer_id=contact.application_id.freelancer_id).aggregate(avg_rating=Avg('rating'))
        avg_rating = avg_agg.get('avg_rating')
        avg_rating = float(avg_rating) if avg_rating is not None else 0.0

        total_ratings = Freelancer_Ratings.objects.filter(contact_id__application_id__freelancer_id=contact.application_id.freelancer_id).count()
        completed_ratings = Freelancer_Ratings.objects.filter(contact_id__application_id__freelancer_id=contact.application_id.freelancer_id, complete=True).count()
        if total_ratings == 0:
            complete_ratio = 0.0
        else:
            complete_ratio = completed_ratings / total_ratings 

        portfolio.rating=avg_rating    
        portfolio.complete=complete_ratio
        portfolio.save()
        return JsonResponse({'success':True,'message': 'Rating created successfully'}, status=200)
   
    @csrf_exempt
    @require_POST
    def load_review(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)
        try:
            data = json.loads(request.body.decode('utf-8'))
        except (ValueError, UnicodeDecodeError):
            return HttpResponseBadRequest(json.dumps({'success':False,'message': 'Invalid JSON'}), content_type='application/json')

        employer_id=data.get("employer_id")
        if not employer_id:
            return JsonResponse({'success':False,'message': 'employer id is required'}, status=400)

        review_qs = Employer_Reviews.objects.filter(contact_id__application_id__job_id__employer_id=employer_id,status=True).select_related('contact_id__application_id__job_id','contact_id__application_id__freelancer_id','contact_id__application_id','contact_id').distinct()
        if not review_qs.exists():
            return JsonResponse({'success': True, 'reviews': []}, status=200)
        result = []
        for review in review_qs:
            result.append({
                'review_id':review.pk,
                'comment':review.comment,
                'score':review.score,
                'created_at':review.created_at,
                'freelancer_id': review.contact_id.application_id.freelancer_id.pk,
                'freelancer_name': review.contact_id.application_id.freelancer_id.full_name,
                'freelancer_avatar':review.contact_id.application_id.freelancer_id.avatar,
            })
        return JsonResponse({'success':True,'reviews': result}, status=200)
        
    @csrf_exempt
    @require_GET
    def load_history_job(request):
        auth_header = request.META.get('HTTP_AUTHORIZATION', '')
        if not auth_header.startswith('Token '):
            return JsonResponse({'success':False,'message': 'Authorization header required: Token <key>'}, status=400)
        token_key = auth_header.split(' ', 1)[1].strip()
        if not token_key:
            return JsonResponse({'success':False,'message': 'Token is invalid.'}, status=400)
        try:
            token = Token.objects.get(key=token_key)
        except Token.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Token does not exist.'}, status=400)
        employer=token.user

        contacts_qs = Contacts.objects.filter(application_id__job_id__employer_id=employer,current_status=False).select_related('application_id__job_id','application_id__freelancer_id','application_id').distinct()
        if not contacts_qs.exists():
            return JsonResponse({'success': True, 'contacts': []}, status=200)
        result = []
        for contact in contacts_qs:
            review = Employer_Reviews.objects.filter(contact_id=contact).first()
            rating = Freelancer_Ratings.objects.filter(contact_id=contact).first()

            if not review or not rating:
                continue
            
            review=Employer_Reviews.objects.get(contact_id=contact)
            job = contact.application_id.job_id
            freelancer = contact.application_id.freelancer_id
            rating = Freelancer_Ratings.objects.get(contact_id=contact)
            result.append({
                'contact_id':contact.pk,
                'job_title':job.title,
                'score':review.score,
                'complete':rating.complete,
                'company_name':job.employer_id.company_name,
                'freelancer_id': freelancer.id,
                'freelancer_name': freelancer.full_name,
                'freelancer_avatar':freelancer.avatar,
                'start_date': contact.start_date,
                'end_date': contact.end_date
            })
        return JsonResponse({'success':True,'jobs': result}, status=200)
       