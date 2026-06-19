package com.fashionstore.servlet;

import java.io.IOException;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.ProductDAOImpl;
import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.UserDAOImpl;
import com.fashionstore.model.User;
import com.fashionstore.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getUser(request);
        UserDAO userDAO = new UserDAOImpl();
        request.setAttribute("profile", userDAO.getUserById(user.getUserId()));
        request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User sessionUser = getUser(request);
        UserDAO userDAO = new UserDAOImpl();

        String action = request.getParameter("action");
        if ("password".equals(action)) {
            String current = request.getParameter("currentPassword");
            String newPass = request.getParameter("newPassword");
            User dbUser = userDAO.loginUser(sessionUser.getEmail(), current);
            if (dbUser != null && newPass != null && newPass.length() >= 6) {
                userDAO.updatePassword(sessionUser.getUserId(), PasswordUtil.hash(newPass));
                response.sendRedirect(request.getContextPath() + "/profile?success=password");
            } else {
                response.sendRedirect(request.getContextPath() + "/profile?error=password");
            }
            return;
        }

        User user = new User();
        user.setUserId(sessionUser.getUserId());
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setPhone(request.getParameter("phone"));
        user.setAddress(request.getParameter("address"));

        if (userDAO.updateUser(user)) {
            User updated = userDAO.getUserById(sessionUser.getUserId());
            HttpSession session = request.getSession();
            session.setAttribute("user", updated);
            response.sendRedirect(request.getContextPath() + "/profile?success=updated");
        } else {
            response.sendRedirect(request.getContextPath() + "/profile?error=failed");
        }
    }

    private User getUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }
}
