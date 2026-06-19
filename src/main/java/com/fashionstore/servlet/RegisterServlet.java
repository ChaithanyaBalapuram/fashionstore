package com.fashionstore.servlet;

import java.io.IOException;


import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.UserDAOImpl;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String address = request.getParameter("address");

        UserDAO userDAO = new UserDAOImpl();

        if (userDAO.emailExists(email)) {
            response.sendRedirect(request.getContextPath() + "/customer/register.jsp?error=email");
            return;
        }

        if (password == null || password.length() < 6) {
            response.sendRedirect(request.getContextPath() + "/customer/register.jsp?error=password");
            return;
        }

        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setAddress(address);

        boolean status = userDAO.registerUser(user);

        if (status) {

            response.sendRedirect(
                    request.getContextPath() + "/customer/login.jsp?success=registered");

        } else {

            response.sendRedirect(
                    request.getContextPath() + "/customer/register.jsp?error=failed");
        }
    }
}