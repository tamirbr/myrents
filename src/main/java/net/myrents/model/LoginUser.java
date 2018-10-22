package net.myrents.model;


import net.myrents.service.UserService;
import net.myrents.web.controller.MainController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;


public class LoginUser {
    private boolean loginSuccess = false;

    public LoginUser(){

    }

    public LoginUser(User user, boolean stayLoggedIn, HttpSession httpSession, HttpServletResponse response) {
        httpSession.setAttribute(ConstantsHe.LOG_IN_USER, user);
        loginSuccess = true;
        // Save info in cookies
        if (stayLoggedIn) {
            Cookie cookieUserName = new Cookie(ConstantsHe.LOGIN_EMAIL, user.getEmail());
            cookieUserName.setMaxAge(60 * 60 * 24 * 30);
            Cookie cookiePassword = new Cookie(ConstantsHe.LOGIN_PASS, user.getPassword());
            cookiePassword.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(cookieUserName);
            response.addCookie(cookiePassword);
        }
    }

    public User checkLogin(HttpSession httpSession, HttpServletRequest request, UserService userService) {
        User logInTemp = (User) httpSession.getAttribute (ConstantsHe.LOG_IN_USER);
        if(logInTemp == null){
            Cookie[] cookie_jar = request.getCookies();
            String email = "";
            String pass = "";
            // Check to see if any cookies exists
            if (cookie_jar != null) {
                for (int i = 0; i < cookie_jar.length; i++) {
                    if (cookie_jar[i].getName().equals(ConstantsHe.LOGIN_EMAIL)) {
                        email = cookie_jar[i].getValue();
                    } else if (cookie_jar[i].getName().equals(ConstantsHe.LOGIN_PASS)) {
                        pass = cookie_jar[i].getValue();
                    }
                }
            }
            if(!email.isEmpty() && !pass.isEmpty()) {
                logInTemp = userService.findByEmail(email);
                if(logInTemp != null) {
                    if (logInTemp.getPassword().equals(pass)) {
                        httpSession.setAttribute(ConstantsHe.LOG_IN_USER, logInTemp);
                        loginSuccess = true;
                        System.out.println("logged in cookies: " + logInTemp.getId());
                        return logInTemp;
                    } else {
                        System.out.println("not logged in");
                        return new User();
                    }
                } else{
                    System.out.println("not logged in");
                    return new User();
                }
            } else{
                System.out.println("not logged in");
                return new User();
            }
        } else{
            loginSuccess = true;
            System.out.println("logged in: "+logInTemp.getId());
            return logInTemp;
        }
    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

}
