package com.nnk.springboot.controllers;

import com.nnk.springboot.configuration.CustomUserDetailsService;
import com.nnk.springboot.controllers.DTO.UserEditRequest;
import com.nnk.springboot.controllers.DTO.UserRoleEditRequest;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.CrudService;
import com.nnk.springboot.services.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final CrudService<User> userService;
    private final CustomUserDetailsService customUserDetailsService;


    @RequestMapping("admin/users")
    public String home(Model model)
    {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }


    @RequestMapping("403")
    public String error403(Model model)
    {
        model.addAttribute("errorMsg",
                "sorry, you do not have the authorization to reach this resource");

        return "403";
    }

    @GetMapping("/user/add")
    public String addUser() {
        return "user/add";
    }

    @GetMapping("/admin/updateUserRole")
    public String editUserRole() {
        return "admin/updateUserRole";
    }

    @PostMapping("/user/validate")
    public String validate(@Validated UserEditRequest userEditRequest, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User newUser = new User(
                    userEditRequest.getUsername(),
                    userEditRequest.getFullname(),
                    encoder.encode(userEditRequest.getPassword())
            );


            userService.save(newUser);
            return "redirect:/login";
        }
        return "user/add";
    }
    @GetMapping("admin/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        User userToEdit = userService.getById(id);
        UserRoleEditRequest userRoleEditRequest = new UserRoleEditRequest(
                userToEdit.getUsername(),
                userToEdit.getFullname(),
                userToEdit.getRole()
        );

        model.addAttribute("userRoleEditRequest", userRoleEditRequest);
        model.addAttribute("userId", id);
        return "admin/updateUserRole";
    }

    @PostMapping("admin/user/update/{id}")
    public String UpdateUserRoleForm(@Validated UserRoleEditRequest userRoleEditRequest,
                                     BindingResult result,
                                     @PathVariable("id") Integer id,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        model.addAttribute("userRoleEditRequest" , userRoleEditRequest);
        model.addAttribute("userId", id);
        model.addAttribute("result", result);
        if (result.hasErrors()) {
            return "/admin/updateUserRole" ;
        }
        try {


            User userToEdit = userService.getById(id);
            userToEdit.setRole(userRoleEditRequest.getRole());
            userToEdit.setUsername(userRoleEditRequest.getUsername());
            userToEdit.setFullname(userRoleEditRequest.getFullname());
            userService.update(userToEdit);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "user has been edited successfully");
        }

        catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "user has not been edited because of error :" + e);

        }
        return "redirect:/admin/users";
    }

    @GetMapping("/user/myself")
    public String showUpdateForm(Model model) {
        User connectedUser = SecurityHelper.getConnectedUser();
        log.info("my connected user is {}",connectedUser);
        Integer userId = connectedUser.getId();
        log.info("my connected userID is {}",userId);
        UserEditRequest userEditRequest = new UserEditRequest(
                connectedUser.getUsername(),
                connectedUser.getFullname()
        );
        model.addAttribute("userEditRequest", userEditRequest);
        model.addAttribute("id", userId);
        return "user/myself";
    }

    @PostMapping("/user/myself/validate")
    public String updateMyself(@Validated UserEditRequest userEditRequest,
                             BindingResult result, Model model) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User connectedUser = SecurityHelper.getConnectedUser();
        Integer userId = connectedUser.getId();
        model.addAttribute("userEditRequest", userEditRequest);
        model.addAttribute("result",result);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEditRequest.getUsername());

        String encodedPassword = encoder.encode(userEditRequest.getPassword());
        User updatedUser = userService.getById(userId);


        if (result.hasErrors()) {
            return "user/update";
        }

        updatedUser.setUsername(userEditRequest.getUsername());
        updatedUser.setFullname(userEditRequest.getFullname());
        updatedUser.setPassword(encodedPassword);
        userService.update(updatedUser);
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {

        UserEditRequest userEditRequest = new UserEditRequest(
        );
        model.addAttribute("userEditRequest", userEditRequest);

        return "user/add";
    }

    @GetMapping("admin/user/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            userService.getById(id);
            userService.delete(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "the user with id = " + id + " has been deleted succesfully");

        }
        catch (Exception e)
        {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "ERROR : the user with id = " + id + " has not been deleted");


        }        return "redirect:/admin/users";
    }


}
