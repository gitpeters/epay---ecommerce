# EPAY Endpoint Documentation

ePay is a commerce API created to support frictionless online purchase. The admin, customer, and vendor sections of the ePay API are segregated into three separate user groups. The Admin is fully empowered to establish and assign responsibilities to customers, view all users and products, delete users and products, update user roles, and perform other operations. Customers may browse the wide selection of goods, easily add items to their shopping carts, and take advantage of a simple checkout process with safe payment choices. Vendor has seamless access to add, update, and delete products. Businesses can offer their clients an extensive and interesting e-commerce platform thanks to ePay. The endpoint utilizes various technologies such as Spring Security, MySQL, Redis for caching, Java Message Service, Paystack for payment gateway integration, and ElasticEmail for messaging service.
![Capture](https://github.com/gitpeters/epay---ecommerce/assets/111524304/cafa0286-950d-44b8-9a35-609ae02fd02b)

## Table of Contents

- [Features](#features)
  - [Admin Model](#admin-model)
  - [User Model](#user-model)
  - [Vendor Model](#vendor-model)
- [Technologies Used](#technologies-used)
- [Payment Gateway](#payment-gateway)
- [Messaging Service](#messaging-service)
- [Documentation](#documentation)
- [How to Use](#how-to-use)

## Features

### Admin Model

The Admin model provides functionality for managing user roles, product categories, and related operations. The following endpoints are available:

- **Creating User Role**: This endpoint allows the creation of user roles.
- **Assigning Roles to Users**: Admins can assign specific roles to users.
- **Removing Users from Role**: Admins can remove users from assigned roles.
- **Deleting Role**: This endpoint enables the deletion of user roles.
- **Creating Product Categories**: Admins can create new product categories.
- **Edit and Delete Product Categories**: This endpoint allows admins to edit and delete existing product categories.

### User Model

The User model focuses on user account management, product browsing, cart operations, and profile management. The following endpoints are available:

- **Register Account**: Users can register for an account.
- **Verify Email**: Email verification for registered users.
- **Login to Account**: Users can log in to their account after email verification.
- **See All Available Products**: Users can view all available products.
- **Add Product to Cart**: Users can add products to their shopping cart.
- **Remove/Clear Product from Cart**: Users can remove or clear products from their cart.
- **Checkout of Cart**: Users can proceed to checkout their cart.
- **Make Payment for Product Ordered**: Authenticated users can make payments for ordered products.
- **Edit Profile**: Users can edit their profile information.
- **Reset Password**: Users can reset their password if forgotten.
- **Change Password**: Users can change their password.
- **Upload Profile Picture**: Users can upload a profile picture.

### Vendor Model

The Vendor model is responsible for managing products, including creation, editing, category assignment, and deletion. The following endpoints are available:

- **Create Product**: Vendors can create new products.
- **Add Images to Product**: Vendors can add images to their products.
- **Edit Product**: Vendors can edit the details of their products.
- **Assigned Categories to Product**: Vendors can assign categories to their products.
- **Delete Product**: Vendors can delete their products.

## Technologies Used

The Ecommerce Endpoint utilizes the following technologies:

- Spring Boot: Provides the framework for developing the ecommerce endpoint.
- Spring Security: Enables secure authentication and authorization.
- MySQL: The database used for storing application data.
- Redis: In-memory caching for improved performance.
- Java Message Service: Facilitates messaging and communication within the application.

## Payment Gateway

The payment gateway integrated with the Ecommerce Endpoint is Paystack. It allows users to make secure online payments for the products they order.

## Messaging Service

ElasticEmail is used as the messaging service for the **ePay Endpoint**. It provides email communication functionalities, including email verification and other notifications.

Please refer to the relevant sections for detailed information on each feature, endpoint, or technology used in the ePay Endpoint.

## Documentation
The APIs were properly documented using Postman.
[Postman Documentation](https://documenter.getpostman.com/view/23480248/2s93z3ekK9)

## How to Use

1. Clone the ePay repository to your local machine:
```git
git clone https://github.com/gitpeters/epay---ecommerce.git
```

2. Ensure you have the following prerequisites installed:
- Java Development Kit (JDK)
- IntelliJ IDE or any of your favourite IDE that supports JVM
- Spring Boot
- MySQL
- Redis server
- Postman (for testing the API)

3. Set up the MySQL database by creating a new database and configuring the database connection properties in the `application.properties` file.
   
4. Make sure the following dependencies in added to your `pom.xml` file:
   - spring-boot-start-data-jpa
   - spring-boot-starter-security
   - webjars-locator
   - jakarta.validation-api
   - spring-boot-starter-data-redis
   - jackson-databind
   - jedis
   - hibernate-validator
   - spring-boot-starter-web
   - springdoc-openapi-starter-webmvc-ui
   - spring-boot-starter-mail
   - mysql-connector-j
   - lomok

5. Configure the Redis connection properties in the `application.properties` file. (See ``config/RedisConfig``)
   
6. Start the ePay application on your local machine.

7. Once the application is up and running, you can use Postman or any other API testing tool to interact with the ePay API. Refer to the [Postman Documentation](https://documenter.getpostman.com/view/23480248/2s93z3ekK9) for detailed API endpoints and their request/response examples.

Feel free to explore the codebase, make modifications, and customize the application to suit your specific requirements.

If you encounter any issues or have any questions, please don't hesitate to reach out. Happy exploring!

#epay #ecommerce #springboot #javadevelopment #documentation


