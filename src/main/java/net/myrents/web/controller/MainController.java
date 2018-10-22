package net.myrents.web.controller;

import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
import net.myrents.model.ConstantsHe;
import net.myrents.model.Item;
import net.myrents.model.LoginUser;
import net.myrents.model.User;
import net.myrents.service.CategoryService;
import net.myrents.service.ItemService;
import net.myrents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RecaptchaValidator recaptchaValidator;

    private int showingItems = 8;

    // Main page
    @RequestMapping("/")
    public String mainPage(Model model,HttpSession httpSession, HttpServletRequest request) {
        if(!model.containsAttribute("constants")) {
            model.addAttribute("constants" , new ConstantsHe());
        }

        // Check if user logged in or return new user model
        if(!model.containsAttribute(ConstantsHe.USER)) {
            LoginUser loginUser = new LoginUser();
            User user = loginUser.checkLogin(httpSession,request, userService);
            if (!loginUser.isLoginSuccess()){
                user = new User();
            }
            model.addAttribute(ConstantsHe.USER , user);
        }

        model.addAttribute("register","/register");
        model.addAttribute("login","/login");
        model.addAttribute("mainPages",true);

        return "main/index";
    }

    // Null item page
    @RequestMapping("/item/null")
    public String itemPageNull() {
        return "redirect:/";
    }

    // Item page
    @RequestMapping("/item/{id}")
    public String itemPage(Model model,@PathVariable Long id,HttpSession httpSession, HttpServletRequest request) {
        if(!model.containsAttribute("constants")) {
            model.addAttribute("constants" , new ConstantsHe());
        }
        // Check if user logged in or return new user model
        if(!model.containsAttribute(ConstantsHe.USER)) {
            LoginUser loginUser = new LoginUser();
            User user = loginUser.checkLogin(httpSession,request, userService);
            if (!loginUser.isLoginSuccess()){
                user = new User();
            }
            model.addAttribute(ConstantsHe.USER , user);
        }

        Item item = itemService.findById(id);
        if(item != null){
            model.addAttribute(ConstantsHe.ITEM , item);
        } else{
            return "redirect:/";
        }

        model.addAttribute("register","/register");
        model.addAttribute("login","/login");
        return "main/item";
    }

    // Login page
    @RequestMapping("/login")
    public String loginPage(Model model,HttpSession httpSession, HttpServletRequest request) {
        if(!model.containsAttribute("constants")) {
            model.addAttribute("constants" , new ConstantsHe());
        }
        // Check if user logged in or return new user model
        if(!model.containsAttribute(ConstantsHe.USER)) {
            LoginUser loginUser = new LoginUser();
            model.addAttribute(ConstantsHe.USER , loginUser.checkLogin(httpSession,request,userService));
            if (loginUser.isLoginSuccess()){
                return "redirect:/";
            }
        }
        model.addAttribute("register","/register");
        model.addAttribute("login","/login");
        model.addAttribute("pageTitle",ConstantsHe.LOG_IN);

        return "main/login";
    }

    // Register page
    @RequestMapping("/register")
    public String registerPage(Model model,HttpSession httpSession, HttpServletRequest request) {
        if(!model.containsAttribute("constants")) {
            model.addAttribute("constants" , new ConstantsHe());
        }
        // Check if user logged in or return new user model
        if(!model.containsAttribute(ConstantsHe.USER)) {
            LoginUser loginUser = new LoginUser();
            model.addAttribute(ConstantsHe.USER , loginUser.checkLogin(httpSession,request,userService));
            if (loginUser.isLoginSuccess()){
                return "redirect:/";
            }
        }
        model.addAttribute("register","/register");
        model.addAttribute("login","/login");
        model.addAttribute("pageTitle",ConstantsHe.REGISTER);

        return "main/register";
    }

    // Register new user
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@Valid User user, BindingResult result, @RequestParam MultipartFile userImage, HttpServletRequest request, RedirectAttributes redirectAttributes, HttpSession httpSession, @RequestParam(value = "terms",required = false) String terms) {
        // Check recaptcha
        ValidationResult resultCaptcha = recaptchaValidator.validate(request);
        if(resultCaptcha.isFailure()){
            redirectAttributes.addFlashAttribute("recaptcha",ConstantsHe.NOT_ROBOT);
        }
        // Check terms accepted
        if(terms == null){
            // terms of use
            redirectAttributes.addFlashAttribute("terms","יש לאשר את תנאי השימוש");
        }
        // Check email free
        if(!user.getEmail().isEmpty()) {
            List<User> users = userService.findAll();
            for (int i = 0;i < users.size();i++ ){
                if(users.get(i).getEmail().equals(user.getEmail())){
                    result.rejectValue("email", "error.user", "הדואר האלקטרוני בשימוש");
                }
            }
        }
        if (user.getLat() == null || user.getLng() == null || user.getLat() == 0 || user.getLng() == 0) {
            result.addError(new FieldError("item", "lat", user.getLat(), false, new String[1], null, "כתובת אינה תקינה"));
        }
        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user",result);
            // Add  category if invalid was received
            redirectAttributes.addFlashAttribute(ConstantsHe.USER,user);
            return "redirect:/register";
        } else if(terms == null || resultCaptcha.isFailure()){
            redirectAttributes.addFlashAttribute(ConstantsHe.USER,user);
            return "redirect:/register";

        }
        if(userImage != null && !userImage.isEmpty()) {
            try {
                user.setImage(userImage.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userService.save(user);
        // Log in after register
        User tempUser = userService.findByEmail(user.getEmail());
        if(tempUser != null) {
            if (user.getPassword().equals(tempUser.getPassword())) {
                httpSession.setAttribute(ConstantsHe.LOG_IN_USER, tempUser);
                redirectAttributes.addFlashAttribute(ConstantsHe.USER, tempUser);
            }
        }
        return "redirect:/";
    }

    // Login user
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginUser(@Valid User user, BindingResult result, HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response, HttpSession httpSession, @RequestParam(value = "rememberPass",required = false) String remmberPass) {
        // Check recaptcha
        ValidationResult resultCaptcha = recaptchaValidator.validate(request);
        if(resultCaptcha.isFailure()){
            redirectAttributes.addFlashAttribute("loginRecaptcha",ConstantsHe.NOT_ROBOT);
            redirectAttributes.addFlashAttribute(ConstantsHe.USER,user);
            return "redirect:/login";
        }
        User tempUser = userService.findByEmail(user.getEmail());
        LoginUser loginUser = null;
        if(tempUser != null) {
            if (user.getPassword().equals(tempUser.getPassword())){
                // Check if password need to be remembered
                if (remmberPass == null) {
                    loginUser = new LoginUser(tempUser, false, httpSession, response);
                } else {
                    loginUser = new LoginUser(tempUser, true, httpSession, response);
                }
            }
        }

        if(loginUser != null){
            redirectAttributes.addFlashAttribute(ConstantsHe.USER,tempUser);
            return "redirect:/";
        }else{
            redirectAttributes.addFlashAttribute("badLogin",ConstantsHe.BAD_LOGIN);
            redirectAttributes.addFlashAttribute(ConstantsHe.USER,user);
            return "redirect:/login";
        }
    }

}

