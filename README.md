# :muscle: Gladiators
Gladiators is monolithic arhitecture application. Our main focus is to combine fitness shop with custom training plans for our clients with personal trainers.


# :hammer_and_wrench: Build with:
- Java 14
- Spring Boot 2.3.3
- Maven 
- JPA Hibernate
- MySQL
- Thymeleaf
- Spring Security
- Cloudinary 1.2.2
- Model Mapper 2.3.2
- Lombok
- JavaX Mail 1.4.7
- Gson 2.8.5
- Hibernate Validators 6.1.5
- PayPal sdk - 1.4.2
- Spring Boot Starter AOP
- JUnit
- Mockito
- HTML
- CSS
- Bootstrap
- JavaScript

# Functionality
- Register
   When you register you must confirm it by email verification. This is done by generating verification token which is valid 24 hours.
   
- Navigation Bar
   The navigation bar includes (Home, Shop, Trainers, About us, Articles) pages, (Cart, Profile) dropdown menus and Logout/Login/Register(according to depends on whatever you are logged in or not). Profile dropdown depends on what role you have. If you are custom user profile dropdown will include (Inbox, Profile, My Orders) pages. In order you are moderator you will see also Moderator Panel. In order you are Admin you will see Admin Panel and Admins Logs. In order you are Trainer you will see Trainer Panel. If you are user with role Root you cannot access profile page and cart.
   
- Home Page
   In home page you have 3 articles. First one is to show you if you have or you don't have training plan. If you have training plan and you already paid for it and it is started you can see your workouts by day. If you didn't pay you will have notification under the nav bar which will tell you to pay. If you didn't pay or your training plan isn't started yet for current date you cannot access it. The second article is for your progress chart which includes some of your body parameters. You can edit them. If you doesn't edit them for two weeks a notification will be shown under the nav bar to tell you to edit your progress chart. The third article is BMI(Body Mass Index) Calculator. You can easily calculate your BMI.
   
- Customer
   When you are registered and you already confirm your email you can log in. In the navigation bar you will not see the cart and the profile dropdowns. They will be replaced by button for Customer Registration. In this form you will have to fill your personal data. Once you fill the form the cart and the profile dropdowns will be shown. In the first article in home page where you have details for training plan you will have button to contact with trainer if you don't have training plan. This button will redirect you to Trainers page where you can choose your trainer and send message to it with details of what kind of training plan you want. When this trainer see your message it will create individual training plan for you and this plan will be shown in the home page. You will have to pay for it before access it so when you click on your cart a training plan will be appended in it and when you try to finish your payment will be redirected to PayPal. In details of your training plan you will firstly see all week days and when you click on any day you can see the exercises for this day.
   
- Trainer
   User with role Admin can choose which user to be created or deleted as trainer. When you log in and admin gave you that role you need to fill a form with your personal data.
 Once you are confirmed trainer you can create exercises, workouts, training plans. You can easily set training plan to your customers and you can see their progress.
 
- Admin
   In Admins Logs section you can see all moderator actions. In Admin Panel you can change roles of users and create or delete trainer.
   
- Moderator
   Moderator can write articles, create categories, create subcategories for them, create products, can see all orders and change their status from pending to active and from active to finished. Once the orders are finished they will be deleted every fifth day of the month. It can also edit or delete products, change their quantity, their price or anything else.
   
- Shop
   In our shop you can buy different products from different categories. You can rate and comment the products. Each category has subcategories in it and if a subcategory doesn't have products it will not be shown in the shop. Every week we have offers for different products. They are choosen randomly and are discounted by 20%.
   
# Summary
:email:
  If you have any questions or you found any bugs we will be glad to send us message on gladiatorsfitnessapp@gmail.com :neckbeard:
  If you like our application please give us star :star:
   
