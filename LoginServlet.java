// package com.vpn.servlets;

import com.vpn.utils.DBConnection;
import com.vpn.utils.EncryptionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT password, role FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String role = rs.getString("role");

                if (EncryptionUtil.decrypt(storedPassword, "SecretKey").equals(password)) {
                    request.getSession().setAttribute("username", username);
                    request.getSession().setAttribute("role", role);

                    if ("admin".equals(role)) {
                        response.sendRedirect("admin.html");
                    } else {
                        response.sendRedirect("user.html");
                    }
                } else {
                    response.sendRedirect("login.html?error=Invalid credentials");
                }
            } else {
                response.sendRedirect("login.html?error=User not found");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
