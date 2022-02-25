package com.wcw.forum.service;

import com.wcw.forum.po.User;

public interface UserService {
    User checkUser(String username, String password);
}
