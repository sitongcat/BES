package org.bes.service;

import org.apache.commons.lang3.StringUtils;
import org.bes.model.entity.User;
import org.bes.model.repository.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

	private static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

    public List<User> getList(User condition) {
        return this.userDAO.find(condition, null, 0);
    }

    public List<User> getList(User condition, int page, int pageSize) {
        return this.userDAO.find(condition, (page - 1) * pageSize, pageSize);
    }

    public int getCount(User condition) {
        return this.userDAO.findCount(condition);
    }

    public User get(String id) {
        return this.userDAO.findById(id);
    }

    public User getByUserName(String userName) {
        try {
            User user = this.userDAO.findByUserName(userName);
            return user;
        } catch (Exception e) {
        	log.error("getByUserName", e);
            return null;
        }
    }

    public void save(User user) {
        if (StringUtils.isBlank(user.getId())) {
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setPassword("123456");
            this.userDAO.insert(user);
        } else {
            User _user = this.userDAO.findById(user.getId());
            user.setPassword(_user.getPassword());
            this.userDAO.update(user);
        }
    }

    public void updatePassword(String id, String password) {
	    User user = this.userDAO.findById(id);
	    user.setPassword(password);
	    this.userDAO.update(user);
    }

    public void delete(String id) {
        User User = this.userDAO.findById(id);
        if (User != null) {
            this.userDAO.delete(User);
        }
    }

}
