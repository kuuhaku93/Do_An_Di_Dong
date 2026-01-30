from django.db import models
from django.utils import timezone
import datetime
from django.contrib.auth.models import AbstractUser

class Accounts(AbstractUser):
    full_name = models.CharField(max_length=255, blank=True, null=True)
    email = models.EmailField(unique=True, blank=True, null=True)
    employer_description = models.TextField(blank=True, null=True,default=None)
    company_name = models.CharField(max_length=255, blank=True, null=True)
    freelancer_status = models.BooleanField(default=True)
    employer_status = models.BooleanField(default=True)
    phone_number = models.CharField(max_length=20, blank=True, null=True)
    avatar = models.CharField(max_length=255, blank=True, null=True,default="https://static.vecteezy.com/system/resources/previews/026/434/417/original/default-avatar-profile-icon-of-social-media-user-photo-vector.jpg")
    company_logo = models.CharField(max_length=255, blank=True, null=True,default="https://static.vecteezy.com/system/resources/previews/026/434/417/original/default-avatar-profile-icon-of-social-media-user-photo-vector.jpg")
    website = models.CharField(max_length=255, blank=True, null=True,default=None)
    address = models.CharField(max_length=255, blank=True, null=True,default=None)

    def __str__(self):
        return self.username

    class Meta:
        verbose_name = "Account"
        verbose_name_plural = "Accounts"

class Jobs(models.Model):
    employer_id = models.ForeignKey(Accounts, on_delete=models.CASCADE)
    title = models.CharField(max_length=255)
    description = models.TextField()
    salary_min = models.FloatField()
    salary_max = models.FloatField()
    end_date = models.DateTimeField()
    created_at = models.DateTimeField(auto_now_add=True)
    location = models.CharField(max_length=255, null=True, default=None, blank=True)
    deadline = models.DateTimeField()
    max_employee = models.IntegerField()
    current_employee = models.IntegerField(default=0)
    status = models.BooleanField(default=True)# False là inactive, True for active

    class Meta:
        verbose_name = "Job"
        verbose_name_plural = "Jobs"

    def __str__(self):
        return f"{self.title} of {self.employer_id.company_name}"

class Applications(models.Model):
    job_id = models.ForeignKey(Jobs, on_delete=models.CASCADE)
    freelancer_id = models.ForeignKey(Accounts, on_delete=models.CASCADE)
    description = models.CharField(max_length=255)
    apply_status= models.BooleanField(default=False)  # False là withdrawn, True for applied
    status = models.BooleanField(default=True)  # False là inactive, True for active
    wanted_salary = models.FloatField()
    applied_date = models.DateTimeField(auto_now_add=True)

    class Meta:
        verbose_name = "Application"
        verbose_name_plural = "Applications"

    def __str__(self):
        return f"{self.freelancer_id.full_name} applied for {self.job_id}"

class Contacts(models.Model):
    application_id = models.ForeignKey(Applications, on_delete=models.CASCADE)
    start_date = models.DateTimeField()
    end_date = models.DateTimeField()
    current_status = models.BooleanField(default=True)  # False là done, True for doing
    created_at = models.DateTimeField(auto_now_add=True)

    class Meta:
        verbose_name = "Contact"
        verbose_name_plural = "Contacts"

    def __str__(self):
        return f"Contact from {self.application_id.freelancer_id.full_name}  to {self.application_id.job_id.title} for <{self.application_id.job_id.employer_id.company_name}>"

class Employer_Reviews(models.Model):
    contact_id = models.OneToOneField(Contacts, on_delete=models.CASCADE)
    comment = models.TextField(blank=True, null=True)
    score = models.FloatField(default=0.0)
    status = models.BooleanField(default=True)  # False là inactive, True for active
    created_at = models.DateField(auto_now_add=True)

    class Meta:
        verbose_name = "Employer Review"
        verbose_name_plural = "Employer Reviews"

    def __str__(self):
        return f"Review by {self.contact_id.application_id.freelancer_id.full_name} for {self.contact_id.application_id.job_id.title} of {self.contact_id.application_id.job_id.employer_id.company_name}"    

class Freelancer_Ratings(models.Model):
    contact_id = models.OneToOneField(Contacts, on_delete=models.CASCADE)
    comment = models.TextField(blank=True, null=True)
    rating = models.FloatField(default=0.0)
    complete=models.BooleanField(default=True) # true là hoàn thành, false là chưa hoàn thành
    status = models.BooleanField(default=True)  # False là inactive, True for active
    created_at = models.DateField(auto_now_add=True)

    class Meta:
        verbose_name = "Freelancer Rating"
        verbose_name_plural = "Freelancer Ratings"

    def __str__(self):
        return f"Rating by {self.contact_id.application_id.job_id.employer_id.company_name} for {self.contact_id.application_id.freelancer_id.full_name} at {self.contact_id.application_id.job_id.title}"

class Portfolios(models.Model):
    freelancer_id = models.OneToOneField(Accounts, on_delete=models.CASCADE)
    description = models.TextField()
    complete=models.FloatField(default=0)
    rating=models.FloatField(default=0)

    class Meta:
        verbose_name = "Portfolio"
        verbose_name_plural = "Portfolios"

    def __str__(self):
        return f"Portfolio of {self.freelancer_id.full_name}"

class Item_types(models.Model):
    name = models.CharField(max_length=100)
    picture = models.CharField(max_length=255,null=True,default=None)

    class Meta:
        verbose_name = "Item Type"
        verbose_name_plural = "Item Types"

    def __str__(self):
        return self.name

class Default_Items(models.Model):
    type_id = models.ForeignKey(Item_types, on_delete=models.CASCADE)
    title = models.CharField(max_length=100)
    description = models.TextField(blank=True, null=True)

    class Meta:
        verbose_name = "Default Item"
        verbose_name_plural = "Default Items"

    def __str__(self):
        return self.title

class Portfolio_Items(models.Model):
    item_id = models.ForeignKey(Default_Items, on_delete=models.CASCADE)
    portfolio_id = models.ForeignKey(Portfolios, on_delete=models.CASCADE)
    start_year = models.IntegerField()
    end_year = models.IntegerField()

    class Meta:
        verbose_name = "Portfolio Item"
        verbose_name_plural = "Portfolio Items"

    def __str__(self):
        return f"Item {self.item_id.title} in Portfolio {self.portfolio_id.id}"

class Custom_items(models.Model):
    portfolio_id = models.ForeignKey(Portfolios, on_delete=models.CASCADE)
    type_id = models.ForeignKey(Item_types, on_delete=models.CASCADE)
    title = models.CharField(max_length=100)
    description = models.TextField(blank=True, null=True)

    class Meta:
        verbose_name = "Custom Item"
        verbose_name_plural = "Custom Items"

    def __str__(self):
        return f"Custom Item {self.title} in Portfolio {self.portfolio_id.id}"

class Skill_Categories(models.Model):
    title = models.CharField(max_length=100, unique=True)

    class Meta:
        verbose_name = "Skill Category"
        verbose_name_plural = "Skill Categories"

    def __str__(self):
        return self.title

class Skills(models.Model):
    category_id = models.ForeignKey(Skill_Categories, on_delete=models.CASCADE)
    skill_name = models.CharField(max_length=100)

    class Meta:
        verbose_name = "Skill"
        verbose_name_plural = "Skills"

    def __str__(self):
        return self.skill_name

class Portfolio_Skills(models.Model):
    portfolio_id = models.ForeignKey(Portfolios, on_delete=models.CASCADE,)
    skill = models.ForeignKey(Skills, on_delete=models.CASCADE)

    class Meta:
        verbose_name = "Portfolio Skill"
        verbose_name_plural = "Portfolio Skills"

    def __str__(self):
        return f"{self.portfolio_id.freelancer_id.full_name} - {self.skill.skill_name}"

class Job_Requirement_Skills(models.Model):
    job_id = models.ForeignKey(Jobs, on_delete=models.CASCADE)
    skill_id = models.ForeignKey(Skills, on_delete=models.CASCADE)

    class Meta:
        verbose_name = "Job Requirement Skill"
        verbose_name_plural = "Job Requirement Skills"

    def __str__(self):
        return f"{self.job_id.title} - {self.skill_id.skill_name}"

class EmailOTP(models.Model):
    user = models.ForeignKey(Accounts, on_delete=models.CASCADE)
    otp_code = models.CharField(max_length=6)
    created_at = models.DateTimeField(auto_now_add=True)

    def is_valid(self):
        return timezone.now() < self.created_at + datetime.timedelta(minutes=5)

    def __str__(self):
        return f"opt of {self.user.full_name}"