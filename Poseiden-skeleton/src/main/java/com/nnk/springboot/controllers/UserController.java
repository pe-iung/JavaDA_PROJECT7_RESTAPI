package com.nnk.springboot.controllers;

import com.nnk.springboot.configuration.CustomUserDetailsService;
import com.nnk.springboot.controllers.DTO.UserEditRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    //private final UserRepository userRepository;
    private final CrudService<User> userService;
    private final CustomUserDetailsService customUserDetailsService;
    //private final SecurityHelper securityHelper;

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
    public String addUser(User bid) {
        return "user/add";
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
            //model.addAttribute("users", userRepository.findAll());
            return "redirect:/login";
        }
        return "user/add";
    }
//    @GetMapping("/user/update/{id}")
//    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
//        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
//        user.setPassword("");
//        model.addAttribute("user", user);
//        return "user/update";
//    }

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

//    @PostMapping("/user/update/{id}")
//    public String updateUser(@PathVariable("id") Integer id, @Validated User user,
//                             BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            return "user/update";
//        }
//
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        user.setPassword(encoder.encode(user.getPassword()));
//        user.setId(id);
//        userRepository.save(user);
//        model.addAttribute("users", userRepository.findAll());
//        return "redirect:/user/list";
//    }

//    @GetMapping("/user/delete/{id}")
//    public String deleteUser(@PathVariable("id") Integer id, Model model) {
//        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
//        userRepository.delete(user);
//        model.addAttribute("users", userRepository.findAll());
//        return "redirect:/user/list";
//    }
}
