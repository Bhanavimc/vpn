// package com.vpn.servlets;

import com.vpn.utils.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("listUsers".equals(action)) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "SELECT username, role FROM users";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                StringBuilder users = new StringBuilder();
                while (rs.next()) {
                    users.append("Username: ").append(rs.getString("username"))
                         .append(", Role: ").append(rs.getString("role"))
                         .append("<br>");
                }
                response.getWriter().write(users.toString());
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
    }
}
