package net.myrents.web.controller.user;

import net.myrents.model.ConstantsHe;
import net.myrents.model.Item;
import net.myrents.model.LoginUser;
import net.myrents.model.User;
import net.myrents.service.ItemService;
import net.myrents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/actions/")
public class RentRestController {
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

}
