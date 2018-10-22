package net.myrents.web.controller;


import net.myrents.model.*;
import net.myrents.service.ItemService;
import net.myrents.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    // Search page
    @RequestMapping("/search")
    public String searchPage(Model model, HttpSession httpSession, HttpServletRequest request,
                           @ModelAttribute(value="userLat") String userLat,
                           @ModelAttribute(value="userLng") String userLng,
                           @ModelAttribute(value="distance") int userDis,
                           @ModelAttribute(value="groupType") String groupType,
                           @ModelAttribute(value="cat") Long cat,
                           @ModelAttribute(value="subCat") Long subCat,
                           @ModelAttribute(value="search") String userSearch,
                           @ModelAttribute(value="address") String userAddress,
                           @ModelAttribute(value="sort") String sort,
                           @ModelAttribute(value="priceStart") String priceStart,
                           @ModelAttribute(value="priceEnd") String priceEnd,
                           @ModelAttribute(value="priceType") String priceType,
                           @ModelAttribute(value="page") int page){

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

        if((Utils.isEmptyOrNull(userSearch) && Utils.isEmptyOrNull(groupType)) || Utils.isEmptyOrNull(userLat) || Utils.isEmptyOrNull(userLng) || userDis == 0 || page == 0 ||
                !StringUtils.isNumericSpace(priceStart) || !StringUtils.isNumericSpace(priceEnd)){
            model.addAttribute("searchError",ConstantsHe.SEARCH_ERROR);
        } else{
            List<Item> items;
            if(sort.equals("tempDistance")){
                items = itemService.searchAll("",priceStart,priceEnd,priceType,groupType,cat,subCat);
            } else{
                items = itemService.searchAll(sort,priceStart,priceEnd,priceType,groupType,cat,subCat);
            }
            List<Item> searchItems = new ArrayList<>();
            for(int i = 0; i<items.size();i++){
                Double distance = DistanceCalculate.distance(items.get(i).getLat(), items.get(i).getLng(), Double.parseDouble(userLat), Double.parseDouble(userLng));
                if (distance <= userDis && search(userSearch, items.get(i))) {
                    items.get(i).setTempDistance(distance);
                    searchItems.add(items.get(i));
                }
            }

            // Sort by distance
            if(sort.equals("tempDistance")) {
                Collections.sort(searchItems);
            }

            // Cut list to pages
            int pages = (int) (searchItems.size()/ConstantsHe.ITEMS_PER_PAGE);
            if(pages < (((float)searchItems.size())/ConstantsHe.ITEMS_PER_PAGE)){
                pages++;
            }
            if(pages > 0){
                model.addAttribute("pages",pages);
            } else{
                model.addAttribute("pages",1);
            }

            if((page * ConstantsHe.ITEMS_PER_PAGE) <= searchItems.size() && searchItems.size() > ConstantsHe.ITEMS_PER_PAGE){
                searchItems = searchItems.subList((page - 1) * ConstantsHe.ITEMS_PER_PAGE, (page * ConstantsHe.ITEMS_PER_PAGE));
            } else if((page * ConstantsHe.ITEMS_PER_PAGE) > searchItems.size() && searchItems.size() > ConstantsHe.ITEMS_PER_PAGE) {
                searchItems = searchItems.subList((page - 1) * ConstantsHe.ITEMS_PER_PAGE, searchItems.size());
            }

            model.addAttribute(ConstantsHe.ITEMS , searchItems);
        }

        model.addAttribute("userLat",userLat);
        model.addAttribute("userLng",userLng);
        model.addAttribute("distance",userDis);
        model.addAttribute("sort",sort);
        model.addAttribute("groupType",groupType);
        model.addAttribute("cat",cat);
        model.addAttribute("subCat",subCat);
        model.addAttribute("userAddress",userAddress);
        model.addAttribute("userSearch",userSearch);
        model.addAttribute("priceStart",priceStart);
        model.addAttribute("priceType",priceType);
        if(priceEnd.equals("99999999")) {
            model.addAttribute("priceEnd", "");
        }else{
            model.addAttribute("priceEnd", priceEnd);
        }

        model.addAttribute("register","/register");
        model.addAttribute("login","/login");
        //model.addAttribute("mainPages",true);

        return "main/search";
    }

    public boolean search(String search, Item item) {
        if(item.getName().contains(search) || search.contains(item.getName())){
            return true;
        } else{
            String[] searchWords = search.split(" ");
            for(int i = 0; i<searchWords.length;i++){
                if(item.getName().contains(searchWords[i]) || item.getComment().contains(searchWords[i])){
                    return true;
                }
            }
        }
        return false;
    }

}



