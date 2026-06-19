package com.fashionstore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.fashionstore.model.Admin;
import com.fashionstore.util.DBConnection;
import com.fashionstore.util.PasswordUtil;

public class AdminDAOImpl implements AdminDAO {

    @Override
    public Admin login(String username, String password) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT admin_id, username, password FROM admin WHERE username=?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String stored = rs.getString("password");
                    if (PasswordUtil.verify(password, stored)) {
                        Admin admin = new Admin();
                        admin.setAdminId(rs.getInt("admin_id"));
                        admin.setUsername(rs.getString("username"));
                        if (!PasswordUtil.isHashed(stored)) {
                            try (PreparedStatement up = con.prepareStatement(
                                    "UPDATE admin SET password=? WHERE admin_id=?")) {
                                up.setString(1, PasswordUtil.hash(password));
                                up.setInt(2, admin.getAdminId());
                                up.executeUpdate();
                            }
                        }
                        return admin;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
