# Fashion Store

A full-stack fashion e-commerce web application built with **Java Servlets**, **JSP**, and **MySQL**. Customers can browse products, manage a cart, place orders, and track deliveries. Admins can manage products, variants, and order statuses.

---

## Features

### Customer
- User registration and login (BCrypt password hashing)
- Homepage with featured products and category navigation
- Product catalog with search, category filter, price filter, and sorting
- Product detail page with variants (color/size), specs, and image gallery
- Shopping cart with stock validation
- Checkout with Cash on Delivery (COD)
- Order history with tracking timeline
- Cancel pending orders
- Profile management and password change

### Admin
- Secure admin login
- Dashboard with product and order counts
- Add, edit, deactivate, and reactivate products
- Manage product variants (color, size, stock)
- Manage product details (material, fit, care instructions)
- Add product image URLs
- View and update order status (Pending → Delivered / Cancelled)
- Order tracking history

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Java 17, Jakarta Servlet 5.0, JDBC |
| Frontend | JSP, JSTL, HTML, CSS |
| Database | MySQL 8 |
| Build | Maven 3.9+ |
| Security | BCrypt (jBCrypt), Auth Filters |
| Dev Server | Jetty 11 (Maven plugin) |
| Production | Apache Tomcat 10.1+ |

---

## Prerequisites

- **JDK 17** or higher
- **Apache Maven 3.9+**
- **MySQL 8.0+** (running locally or remotely)

---

## Project Structure

```
fashionstore/
├── src/main/java/com/fashionstore/
│   ├── servlet/       # HTTP controllers (@WebServlet)
│   ├── dao/           # Data access interfaces and implementations
│   ├── model/         # Java beans (User, Product, Order, etc.)
│   ├── filter/        # Auth and cart count filters
│   └── util/          # DB connection, password hashing
├── src/main/webapp/
│   ├── customer/      # Login, register, cart, checkout, orders, profile
│   ├── admin/         # Admin panel pages
│   ├── includes/      # Shared header and footer
│   ├── css/           # Stylesheet
│   ├── images/        # Product images (place files here)
│   └── WEB-INF/web.xml
├── src/main/resources/
│   ├── db.properties.example   # Database config template
│   └── db.properties           # Your local DB config (not committed)
├── sql/
│   ├── schema.sql              # Table definitions
│   ├── seed_categories.sql     # Default categories
│   ├── seed_admin.sql          # Default admin account
│   ├── seed_products.sql       # Sample products
│   └── setup.sql               # Full setup script
└── pom.xml
```

---

## Database Setup

### Option 1: Full setup (recommended)

From the project root, run each script in order:

```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS fashionstore;"
mysql -u root -p fashionstore < sql/schema.sql
mysql -u root -p fashionstore < sql/seed_categories.sql
mysql -u root -p fashionstore < sql/seed_admin.sql
mysql -u root -p fashionstore < sql/seed_products.sql
```

### Option 2: Using setup.sql (MySQL client)

```bash
cd sql
mysql -u root -p < setup.sql
```

> **Note:** `setup.sql` uses `SOURCE` commands and must be run from inside the `sql/` directory.

---

## Configuration

1. Copy the example config file:

   ```bash
   cp src/main/resources/db.properties.example src/main/resources/db.properties
   ```

2. Edit `src/main/resources/db.properties`:

   ```properties
   db.url=jdbc:mysql://localhost:3306/fashionstore
   db.username=root
   db.password=your_password
   ```

> Do not commit `db.properties` with real credentials. It is listed in `.gitignore`.

---

## Build and Run

### Development (Jetty)

```bash
mvn clean package
mvn jetty:run
```

Open: **http://localhost:8080/fashionstore/home**

### Production (Tomcat)

1. Build the WAR file:

   ```bash
   mvn clean package
   ```

2. Copy `target/fashionstore.war` to Tomcat's `webapps/` folder.

3. Start Tomcat and visit: **http://localhost:8080/fashionstore/home**

> Use **Tomcat 10.1+** for Jakarta Servlet 5.0 compatibility.

---

## Default Credentials

| Role | URL | Username | Password |
|------|-----|----------|----------|
| Admin | `/fashionstore/admin/login` | `admin` | `admin123` |
| Customer | `/fashionstore/customer/register.jsp` | Register a new account | — |

> Change the admin password after first login. Plain-text passwords in the database are automatically upgraded to BCrypt on login.

---

## Application URLs

### Customer

| Page | URL |
|------|-----|
| Home | `/fashionstore/home` |
| Shop | `/fashionstore/products` |
| Product Detail | `/fashionstore/product?id=1` |
| Cart | `/fashionstore/cart` |
| Checkout | `/fashionstore/checkout` |
| My Orders | `/fashionstore/orders` |
| Profile | `/fashionstore/profile` |
| Login | `/fashionstore/customer/login.jsp` |
| Register | `/fashionstore/customer/register.jsp` |

### Admin

| Page | URL |
|------|-----|
| Login | `/fashionstore/admin/login` |
| Dashboard | `/fashionstore/admin/dashboard` |
| Products | `/fashionstore/admin/products` |
| Edit Product | `/fashionstore/admin/product?id=1` |
| Orders | `/fashionstore/admin/orders` |

---

## Adding Product Images

1. Place image files in `src/main/webapp/images/products/` (e.g. `denim-front.jpg`).
2. In the admin product edit page, add the image URL: `images/products/denim-front.jpg`.

The product detail page displays all images linked in the `product_images` table.

---

## Database Tables

| Table | Purpose |
|-------|---------|
| `users` | Customer accounts |
| `admin` | Admin accounts |
| `categories` | Product categories |
| `products` | Product catalog |
| `product_variants` | Color/size/stock per product |
| `product_details` | Material, fit, care info |
| `product_images` | Product image URLs |
| `cart` | Shopping cart items |
| `orders` | Order headers |
| `order_items` | Order line items |
| `order_tracking` | Order status history |

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| Blank homepage | Use `/home`, not `/index.jsp` directly |
| Database connection failed | Check MySQL is running and `db.properties` credentials |
| Seed products fail | Run `seed_categories.sql` before `seed_products.sql` |
| Cart redirect to login | Log in first — cart requires authentication |
| Product has no variants | Edit product in admin and add variants |
| Port 8080 in use | Stop other services or change port in `pom.xml` Jetty config |

---

## Security Notes

- Passwords are hashed with BCrypt before storage
- Customer routes (`/cart`, `/checkout`, `/orders`, `/profile`) are protected by auth filters
- Admin routes require an admin session
- Cart operations are scoped to the logged-in user
- Session timeout is 30 minutes
- For production: use HTTPS, change default admin password, and restrict admin URL access

---

## License

This project is for educational purposes.
