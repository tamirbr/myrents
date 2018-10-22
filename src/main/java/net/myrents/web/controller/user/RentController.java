package net.myrents.web.controller.user;

import com.github.mkopylec.recaptcha.validation.ValidationResult;
import net.myrents.model.*;
import net.myrents.service.ItemService;
import net.myrents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
public class RentController {
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    // user new rent
    @RequestMapping("/newRent")
    public String newRentPage(Model model, HttpSession httpSession, HttpServletRequest request) {
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
        if(!model.containsAttribute(ConstantsHe.ITEM)) {
            model.addAttribute(ConstantsHe.ITEM , new Item());
        }

        model.addAttribute("newRent","/newRent");
        model.addAttribute("pageTitle",ConstantsHe.NEW_RENT);

        return "user/newRent";
    }

    // user edit rent
    @RequestMapping("/editRent/{id}")
    public String editRentPage(Model model, HttpSession httpSession, HttpServletRequest request, @PathVariable Long id) {
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
            if(!model.containsAttribute(ConstantsHe.ITEM)) {
                for (int i= 0;i<user.getItems().size();i++) {
                    if (user.getItems().get(i).getId().equals(id)) {
                        model.addAttribute(ConstantsHe.ITEM, user.getItems().get(i));
                        break;
                    }
                }
                if(!model.containsAttribute(ConstantsHe.ITEM)){
                    return "redirect:/myRents";
                }
            }
        }

        model.addAttribute("editRent","/editRent/"+id);
        model.addAttribute("deleteRent","/deleteRent/"+id);
        model.addAttribute("pageTitle",ConstantsHe.EDIT_RENT);

        return "user/editRent";
    }

    // user rent list
    @RequestMapping("/myRents")
    public String myRentsPage(Model model, HttpSession httpSession, HttpServletRequest request) {
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
            // Refresh user info
            User refreshUser = userService.findById(user.getId());
            Collections.sort(refreshUser.getItems());
            model.addAttribute(ConstantsHe.USER , refreshUser);
            httpSession.setAttribute(ConstantsHe.LOG_IN_USER, refreshUser);
        }

        model.addAttribute("newRent","/newRent");
        model.addAttribute("pageTitle",ConstantsHe.MY_RENTS);

        return "user/myRents";
    }

    // Upload rent
    @RequestMapping(value = "/newRent", method = RequestMethod.POST)
    public String uploadRent(@Valid Item item, BindingResult result, @RequestParam MultipartFile rentImage, HttpServletRequest request, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        if(item.getLat() == null || item.getLng() == null || item.getLat() == 0 || item.getLng() == 0){
            result.addError(new FieldError("item", "lat", item.getLat(), false, new String[1], null, "כתובת אינה תקינה"));
        }
        if(Utils.isEmptyOrNull(item.getPriceHour()) && Utils.isEmptyOrNull(item.getPriceDay()) && Utils.isEmptyOrNull(item.getPriceMonth())){
            result.addError(new FieldError("item", "priceHour", item.getPriceHour(), false, new String[1], null, ConstantsHe.SELECT_ONE_PRICE));
        }
        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.item",result);
            // Add  category if invalid was received
            redirectAttributes.addFlashAttribute(ConstantsHe.ITEM,item);
            return "redirect:/newRent";
        }
        LoginUser loginUser = new LoginUser();
        User user = loginUser.checkLogin(httpSession,request, userService);
        item.setUser(user);
        if(rentImage != null && !rentImage.isEmpty()) {
            try {
                item.setImage(rentImage.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        item.setActive(true);
        item.setPostDate(new Date());
        itemService.save(item);
        return "redirect:/myRents";
    }

    // Edit rent
    @RequestMapping(value = "/editRent/{id}", method = RequestMethod.POST)
    public String editRent(@Valid Item item, BindingResult result, @RequestParam MultipartFile rentImage, HttpServletRequest request, RedirectAttributes redirectAttributes, HttpSession httpSession, @PathVariable Long id) {
        if(item.getLat() == null || item.getLng() == null || item.getLat() == 0 || item.getLng() == 0){
            result.addError(new FieldError("item", "lat", item.getLat(), false, new String[1], null, "כתובת אינה תקינה"));
        }
        if(Utils.isEmptyOrNull(item.getPriceHour()) && Utils.isEmptyOrNull(item.getPriceDay()) && Utils.isEmptyOrNull(item.getPriceMonth())){
            result.addError(new FieldError("item", "priceHour", item.getPriceHour(), false, new String[1], null, ConstantsHe.SELECT_ONE_PRICE));
        }
        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.item",result);
            // Add  category if invalid was received
            redirectAttributes.addFlashAttribute(ConstantsHe.ITEM,item);
            return "redirect:/editRent/"+id;
        }
        // Check if user logged in or return new user model
        LoginUser loginUser = new LoginUser();
        User user = loginUser.checkLogin(httpSession, request, userService);
        if (!loginUser.isLoginSuccess()) {
            return "redirect:/";
        } else{
            for(int i = 0; i < user.getItems().size();i++){
                // Take action if item belong to user
                if(user.getItems().get(i).getId().equals(id)){
                    Item originalItem = itemService.findById(id);
                    originalItem.setGroupType(item.getGroupType());
                    originalItem.setCategory_id(item.getCategory_id());
                    originalItem.setSubCategory_id(item.getSubCategory_id());
                    originalItem.setPriceHour(item.getPriceHour());
                    originalItem.setPriceDay(item.getPriceDay());
                    originalItem.setPriceMonth(item.getPriceMonth());
                    originalItem.setName(item.getName());
                    originalItem.setLat(item.getLat());
                    originalItem.setLng(item.getLng());
                    originalItem.setComment(item.getComment());
                    originalItem.setAddress(item.getAddress());
                    originalItem.setActive(item.isActive());
                    if(rentImage != null && !rentImage.isEmpty()) {
                        try {
                            originalItem.setImage(rentImage.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    itemService.save(originalItem);
                }
            }
        }
        return "redirect:/myRents";
    }

    // Delete rent
    @RequestMapping(value = "/deleteRent/{id}", method = RequestMethod.POST)
    public String deleteRent(@PathVariable Long id, HttpServletRequest request, HttpSession httpSession) {
        // Check if user logged in or return new user model
        LoginUser loginUser = new LoginUser();
        User user = loginUser.checkLogin(httpSession, request, userService);
        if (!loginUser.isLoginSuccess()) {
            return "redirect:/";
        } else{
            for(int i = 0; i < user.getItems().size();i++){
                // Take action if item belong to user
                if(user.getItems().get(i).getId().equals(id)){
                    Item originalItem = itemService.findById(id);
                    itemService.delete(originalItem);
                }
            }
        }
        return "redirect:/myRents";
    }

    // Change rent state
    @RequestMapping(value = "/changeRentState/{id}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void changeRentState(@PathVariable Long id, HttpServletRequest request, HttpSession httpSession) {
        // Check if user logged in or return new user model
        LoginUser loginUser = new LoginUser();
        User user = loginUser.checkLogin(httpSession, request, userService);
        if (!loginUser.isLoginSuccess()) {
            //return "redirect:/";
        } else{
            for(int i = 0; i < user.getItems().size();i++){
                // Take action if item belong to user
                if(user.getItems().get(i).getId().equals(id)){
                    Item originalItem = itemService.findById(id);
                    if(originalItem.isActive()){
                        originalItem.setActive(false);
                    } else{
                        originalItem.setActive(true);
                    }
                    user.getItems().get(i).setActive(originalItem.isActive());
                    itemService.save(originalItem);
                    httpSession.setAttribute(ConstantsHe.LOG_IN_USER, user);
                }
            }
        }
        //return "redirect:/myRents";
    }
}
