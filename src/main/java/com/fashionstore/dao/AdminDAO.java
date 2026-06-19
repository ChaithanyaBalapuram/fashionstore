package com.fashionstore.dao;

import com.fashionstore.model.Admin;

public interface AdminDAO {
    Admin login(String username, String password);
}
