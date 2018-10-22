package net.myrents.web.controller;

import net.myrents.model.*;
import net.myrents.service.BookMarkService;
import net.myrents.service.ItemService;
import net.myrents.service.UserService;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class UtilController {
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private BookMarkService bookMarkService;
    @Autowired
    ServletContext context;

    // logout user
    @RequestMapping("/logout")
    public String logoutUser(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) {
        httpSession.removeAttribute(ConstantsHe.LOG_IN_USER);
        Cookie[] cookie_jar = request.getCookies();
        if (cookie_jar != null)
        {
            for (int i =0; i< cookie_jar.length; i++)
            {
                if(cookie_jar[i].getName().equals(ConstantsHe.LOGIN_EMAIL)){
                    cookie_jar[i].setMaxAge(0);
                    response.addCookie(cookie_jar[i]);
                }else if(cookie_jar[i].getName().equals(ConstantsHe.LOGIN_PASS)){
                    cookie_jar[i].setMaxAge(0);
                    response.addCookie(cookie_jar[i]);
                }
            }
        }
        return "redirect:/";
    }

    // Change bookmark state
    @RequestMapping(value = "/bookmark/{id}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void userBookMark(HttpSession httpSession, HttpServletRequest request, @PathVariable Long id) {
        // Check if user logged in or return new user model
        LoginUser loginUser = new LoginUser();
        User user = loginUser.checkLogin(httpSession,request, userService);
        if (loginUser.isLoginSuccess()){
            BookMark bookMark = user.getBookMarkById(id);
            if(bookMark == null) {
                bookMark = new BookMark(itemService.findById(id), user);
                bookMarkService.save(bookMark);
                user.getBookMarks().add(bookMark);
            } else{
                bookMarkService.delete(bookMark);
                for(int i = 0 ; i<user.getBookMarks().size();i++){
                    if(user.getBookMarks().get(i).getItem().getId().equals(id)){
                        user.getBookMarks().remove(i);
                        break;
                    }
                }
            }
            httpSession.setAttribute(ConstantsHe.LOG_IN_USER, user);
        }
    }

    // image data
    @RequestMapping("/images/{imageId}.jpg")
    @ResponseBody
    public byte[] image(@PathVariable Long imageId) {
        byte[] image = itemService.findById(imageId).getImage();
        if(image == null || image.toString().isEmpty()) {
            Resource resource = new ClassPathResource("static/materialize/img/no_image.jpg");
            try {
                InputStream resourceInputStream = resource.getInputStream();
                return IOUtils.toByteArray(resourceInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else{
            return image;
        }
    }

    // image data
    @RequestMapping("/smallImages/null.jpg")
    @ResponseBody
    public byte[] smallImage() {
        Resource resource = new ClassPathResource("static/materialize/img/no_image.jpg");
        try {
            InputStream resourceInputStream = resource.getInputStream();
            return Utils.cropImage(IOUtils.toByteArray(resourceInputStream));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // image data
    @RequestMapping("/smallImages/{imageId}.jpg")
    @ResponseBody
    public byte[] smallImage(@PathVariable Long imageId) {
        byte[] image = itemService.findById(imageId).getImage();
        if(image == null || image.toString().isEmpty()) {
            Resource resource = new ClassPathResource("static/materialize/img/no_image.jpg");
            try {
                InputStream resourceInputStream = resource.getInputStream();
                return IOUtils.toByteArray(resourceInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else{
            return Utils.cropImage(image);
        }
    }

    // image data
    @RequestMapping("/images/user/{id}.jpg")
    @ResponseBody
    public byte[] userImage(@PathVariable Long id) {
        byte[] image = userService.findById(id).getImage();
        if(image == null || image.toString().isEmpty()) {
            Resource resource = new ClassPathResource("static/materialize/img/user.jpg");
            try {
                InputStream resourceInputStream = resource.getInputStream();
                return IOUtils.toByteArray(resourceInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else{
            return image;
        }
    }

    // image data
    @RequestMapping("/smallUserImage/{userId}.jpg")
    @ResponseBody
    public byte[] smallUserImage(@PathVariable Long userId) {
        byte[] image = userService.findById(userId).getImage();
        if(image == null || image.toString().isEmpty()) {
            Resource resource = new ClassPathResource("static/materialize/img/user.jpg");
            try {
                InputStream resourceInputStream = resource.getInputStream();
                return IOUtils.toByteArray(resourceInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else{
            return Utils.cropImage(image);
        }
    }

}
