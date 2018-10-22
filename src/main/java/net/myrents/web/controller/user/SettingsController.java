package net.myrents.web.controller.user;

import net.myrents.model.ConstantsHe;
import net.myrents.model.Item;
import net.myrents.model.LoginUser;
import net.myrents.model.User;
import net.myrents.service.ItemService;
import net.myrents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class SettingsController {
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    // user panel
    @RequestMapping(value={"/personalArea","/bookMarks"})
    public String mainPage(Model model, HttpSession httpSession, HttpServletRequest request) {
        if(!model.containsAttribute("constants")) {
            model.addAttribute("constants" , new ConstantsHe());
        }
        // Check if user logged in or return new user model
        if(!model.containsAttribute(ConstantsHe.USER)) {
            LoginUser loginUser = new LoginUser();
            User user = loginUser.checkLogin(httpSession,request, userService);
            if (!loginUser.isLoginSuccess()){
                return "redirect:/";
            }
            model.addAttribute(ConstantsHe.USER , user);
        }
        model.addAttribute("pageTitle",ConstantsHe.BOOK_MARKS);

        return "user/bookMarks";
    }

    // user info
    @RequestMapping("/personalInfo")
    public String infoPage(Model model, HttpSession httpSession, HttpServletRequest request) {
        if(!model.containsAttribute("constants")) {
            model.addAttribute("constants" , new ConstantsHe());
        }
        // Check if user logged in or return new user model
        if(!model.containsAttribute(ConstantsHe.USER)) {
            LoginUser loginUser = new LoginUser();
            User user = loginUser.checkLogin(httpSession,request, userService);
            if (!loginUser.isLoginSuccess()){
                return "redirect:/";
            }
            model.addAttribute(ConstantsHe.USER , user);
            if(!model.containsAttribute(ConstantsHe.TEMP_USER)) {
                model.addAttribute(ConstantsHe.TEMP_USER, user);
            }
        }
        model.addAttribute("changeInfo" , "/personalInfo");
        model.addAttribute("pageTitle",ConstantsHe.USER_INFO);

        return "user/info";
    }

    // Edit user
    @RequestMapping(value = "/personalInfo", method = RequestMethod.POST)
    public String editInfo(@Valid User userNewData, BindingResult result, @RequestParam MultipartFile userImage, HttpServletRequest request, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        if (userNewData.getLat() == null || userNewData.getLng() == null || userNewData.getLat() == 0 || userNewData.getLng() == 0) {
            result.addError(new FieldError("item", "lat", userNewData.getLat(), false, new String[1], null, "כתובת אינה תקינה"));
        }
        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.tempUser",result);
            // Add  category if invalid was received
            redirectAttributes.addFlashAttribute(ConstantsHe.TEMP_USER,userNewData);
            return "redirect:/personalInfo";
        }
        // Check if user logged in or return new user model
        LoginUser loginUser = new LoginUser();
        User user = loginUser.checkLogin(httpSession, request, userService);
        if (!loginUser.isLoginSuccess()) {
            return "redirect:/";
        }
        user.setFirstName(userNewData.getFirstName());
        user.setLastName(userNewData.getLastName());
        user.setEmail(userNewData.getEmail());
        user.setAddress(userNewData.getAddress());
        user.setPhone(userNewData.getPhone());
        user.setLat(userNewData.getLat());
        user.setLng(userNewData.getLng());
        if(userImage != null && !userImage.isEmpty()) {
            try {
                user.setImage(userImage.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userService.save(user);
        httpSession.setAttribute(ConstantsHe.LOG_IN_USER, user);
        redirectAttributes.addFlashAttribute("success","success");
        return "redirect:/personalInfo";
    }

}
