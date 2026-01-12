from django.shortcuts import render
from django.http import JsonResponse, HttpResponse,HttpResponseBadRequest
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_GET, require_POST
from rest_framework.authtoken.models import Token
from django.contrib.auth import authenticate
from datetime import datetime
from django.db import transaction
from django.db.models import Count,Q
import json
from .models import Accounts,Jobs,Applications,Contacts,Employer_Reviews,Freelancer_Ratings,Portfolios,Item_types,Default_Items,Portfolio_Items,Custom_items,Skill_Categories,Skills,Portfolio_Skills,Job_Requirement_Skills


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
            return JsonResponse({'success':False,'message': 'account is not exist.'}, status=400)
        if not user.is_active:
            return JsonResponse({'success':False,'message': 'account is not active.'}, status=400)
        token, created = Token.objects.get_or_create(user=user)
        account=Accounts.objects.get(id=user.id)
        return JsonResponse({'success':True,'token': token.key,'employer_status':account.employer_status,'freelancer_status':account.freelancer_status,'message': 'Logged in successfully.'}, status=200)

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
        return JsonResponse({'message': 'Đăng ký thành công.'}, status=201)

        portfolio=Portfolios(freelancer_id=user)
        portfolio.save()
        return JsonResponse({'success':True,'message': 'Đăng ký thành công.'}, status=201)

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
                cate.title: skill_list
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

        list_item = []
        list_type=Item_types.objects.all()
        for type_item in list_type:
            item_in_type = Default_Items.objects.filter(type_id=type_item)
            item_list = [{'id': item.id , 'title': item.title,'description':item.description} for item in item_in_type]
            list_item.append({
                type_item.name: {'piture':type_item.picture,'item':item_list}
            })
        return JsonResponse({'success':True,'items':list_item},status=200)

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
        resuilt = []
        applications = list(Applications.objects.filter(job_id=job_id).order_by('-applied_date'))
        for application in applications:
            resuilt.append({
                'id': application.id,
                'freelancer_name': application.freelancer_id.full_name,
                'description': application.description,
                'apply_status': application.apply_status,
                'wanted_salary': application.wanted_salary,
                'applied_date': application.applied_date,
                'skills': [skill.skill.skill_name for skill in Portfolio_Skills.objects.filter(portfolio_id__freelancer_id=application.freelancer_id)]
            })
        return JsonResponse({'success':True,'applications': resuilt}, status=200)
        
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

        resuilt = []
        list_jobs = list(Jobs.objects.filter(status=True).exclude(employer_id=freelancer).order_by('-created_at')[:20])
        for job in list_jobs:
            resuilt.append({
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
                'is_applied': Applications.objects.filter(job_id=job, freelancer_id=freelancer).exists()
            })
        return JsonResponse({'success':True,'jobs': resuilt}, status=200)

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
        requirement_ids = data.get('requirement', []) or []

        try:
            requirement_ids = [int(x) for x in requirement_ids]
        except (TypeError, ValueError):
            return JsonResponse({'success':False,'message': 'requirement must be a list of integer ids'}, status=400)

        qs=Jobs.objects.filter(status=True).exclude(employer_id=freelancer) 
        if keyword:
            qs = qs.filter(Q(title__icontains=keyword) | Q(employer_id__company_name__icontains=keyword))
        if requirement_ids:
            qs = qs.filter(job_requirement_skills__skill_id__in=requirement_ids) \
               .annotate(matching_skills=Count('job_requirement_skills__skill_id', distinct=True)) \
               .filter(matching_skills=len(requirement_ids))
        qs = qs.order_by('-created_at')[:10]
        resuilt = []
        for job in qs:
            resuilt.append({
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
                'is_applied': Applications.objects.filter(job_id=job, freelancer_id=freelancer).exists()
            })
        return JsonResponse({'success':True,'jobs': resuilt}, status=200)


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
        
        apply=Applications(
            job_id=Jobs.objects.get(id=job_id),
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
        if not freelancer:
            return JsonResponse({'success':False,'message': 'freelancer does not exist.'}, status=400)
        portfolio=Portfolios.objects.get(freelancer_id=freelancer)
        listItem={}
        for item_type in Item_types.objects.all():
            listItem[item_type.name]=[{'title':item.item_id.title,'description':item.item_id.description,'icon':item.item_id.type_id.picture,'start_year':item.start_year,'end_year':item.end_year} for item in Portfolio_Items.objects.filter(portfolio_id=portfolio, item_id__type_id=item_type)]
            for item in Custom_items.objects.filter(portfolio_id=portfolio, type_id=item_type):
                listItem[item_type.name].append({'custom':item.title,'description':item.description,'icon':item_type.picture})

        listSkill={}
        for cate in Skill_Categories.objects.all():
            listSkill[cate.title] = [ps.skill.skill_name for ps in Portfolio_Skills.objects.filter(portfolio_id=portfolio, skill__category_id=cat)]

        portfolio_obj={
            'freelancer_name': portfolio.freelancer_id.full_name,
            'avatar': portfolio.freelancer_id.avatar,
            'email': portfolio.freelancer_id.email,
            'phone_number': portfolio.freelancer_id.phone_number,
            'description': portfolio.description,
            'complete': portfolio.complete,
            'rating':portfolio.rating,
            'items': listItem,
            'skills': listSkill
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

        freelancer=token.user
        freelancer_name = data.get('freelancer_name','').strip()
        avatar = data.get('avatar','').strip()
        email = data.get('email','').strip()
        phone_number = data.get('phone_number','').strip()
        description = data.get('description','').strip()
        skills=data.get('skill',[])or []
        items=data.get('item',[])or[]
        custom=data.get('custom',[])or []

        try:
            portfolio = Portfolios.objects.get(freelancer_id=freelancer)
        except Portfolios.DoesNotExist:
            return JsonResponse({'success':False,'message': 'Portfolio not found'}, status=404)

        with transaction.atomic():
            if name:
                freelancer.full_name = name
            if avatar:
                freelancer.avatar = avatar
            if phone:
                freelancer.phone_number = phone
            freelancer.save()

            if description is not None:
                portfolio.description = description
            portfolio.save()

            try:
                skill_ids = [int(x) for x in listskill]
            except (TypeError, ValueError):
                return JsonResponse({'success':False,'message': 'listskill must be list of int'}, status=400)

            Portfolio_Skills.objects.filter(portfolio_id=portfolio).exclude(skill_id__in=skill_ids).delete()

            existing_skill_ids = set(Portfolio_Skills.objects.filter(portfolio_id=portfolio).values_list('skill_id', flat=True))
            for sid in skill_ids:
                if sid not in existing_skill_ids:
                    Portfolio_Skills.objects.create(portfolio_id=portfolio, skill_id_id=sid)

            try:
                keep_ids = [int(i['id']) for i in listitem]
            except Exception:
                return JsonResponse({'success':False,'message': 'listitem must be list of dicts with id'}, status=400)

            Portfolio_Items.objects.filter(portfolio_id=portfolio).exclude(item_id__in=keep_ids).delete()
            for i in listitem:
                try:
                    iid = int(i['id'])
                    start_year = int(i.get('start_year', 0))
                    end_year = int(i.get('end_year', 0))
                except Exception:
                    continue
                Portfolio_Items.objects.update_or_create(
                    portfolio_id=portfolio,
                    item_id_id=iid,
                    defaults={'start_year': start_year, 'end_year': end_year}
                )

            Custom_items.objects.filter(portfolio_id=portfolio).delete()
            new_custom_objs = []
            for c in custom_items:
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

        resuilt = []
        list_jobs = list(Jobs.objects.filter(status=True,employer_id=employer).order_by('-created_at')[:20])
        for job in list_jobs:
            resuilt.append({
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
            })
        return JsonResponse({'success':True,'jobs': resuilt}, status=200)

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
            deadline = datetime.strptime(deadline_str, "%Y-%m-%d %H:%M:%S") if deadline_str else None
            end_date = datetime.strptime(end_date_str, "%Y-%m-%d %H:%M:%S") if end_date_str else None
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
        requirements=data.get('requirements',[])
        for req in requirements:
            skill_obj=Skills.objects.filter(id=req).first()
            if skill_obj:
                job_req=Job_Requirement_Skills(
                    job_id=new_job,
                    skill_id=skill_obj
                )
                job_req.save()
        print(new_job, requirements)
        return JsonResponse({'success':True,'message': 'Job created successfully.'}, status=200)

        

