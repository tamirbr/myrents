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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;

@Controller
public class StoreController {
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    // user rent store
    @RequestMapping("/store/{id}")
    public String myRentsPage(Model model, HttpSession httpSession, HttpServletRequest request, @PathVariable Long id) {
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

        User user = userService.findByIdActiveItems(id);
        if(user != null){
            model.addAttribute(ConstantsHe.TEMP_USER , user);
        } else{
            return "redirect:/";
        }

        model.addAttribute("register","/register");
        model.addAttribute("login","/login");
        return "user/store";
    }
}
