package com.nnk.springboot.controllers;

import com.nnk.springboot.controllers.DTO.RatingRequest;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class RatingController {


    private final CrudService<Rating> ratingService;

    @RequestMapping("/rating/list")
    public String home(Model model)
    {
        List<Rating> ratings = ratingService.findAll();
        model.addAttribute("ratings", ratings);
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(RatingRequest ratingRequest) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(
            @Validated RatingRequest ratingRequest,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "rating/add";
        }
        try {
            model.addAttribute("ratingRequest", ratingRequest);
            Rating newRating = new Rating(
                    ratingRequest.getMoodysRating(),
                    ratingRequest.getSandPRating(),
                    ratingRequest.getFitchRating(),
                    ratingRequest.getOrderNumber());
            ratingService.save(newRating);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "the new rating has been saved succesfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "ERROR : the new rating has NOT been saved because of errror " + e);

        }
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Rating rating = ratingService.getById(id);
            RatingRequest ratingRequest = new RatingRequest(
                    rating.getSandPRating(),
                    rating.getMoodysRating(),
                    rating.getFitchRating(),
                    rating.getOrderNumber()
            );
            model.addAttribute("ratingRequest", ratingRequest);
            model.addAttribute("ratingId", id);
            return "/rating/update";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage","error updating the id" + id + " with error = " + e);
            return "redirect:rating/update";
        }
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Validated RatingRequest ratingRequest,
                             BindingResult result, Model model, RedirectAttributes redirectAttributes) {

       model.addAttribute("ratingId", id);
       model.addAttribute("ratingRequest", ratingRequest);
       model.addAttribute("result", result);
        if (result.hasErrors())
        {
            return "rating/update";
        }
        try {

            Rating updatedRating = ratingService.getById(id);
            updatedRating.setSandPRating(ratingRequest.getSandPRating());
            updatedRating.setMoodysRating(ratingRequest.getMoodysRating());
            updatedRating.setFitchRating(ratingRequest.getFitchRating());
            updatedRating.setOrderNumber(ratingRequest.getOrderNumber());
            ratingService.update(updatedRating);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Rating updated succesfully for id = " + id);
            return "redirect:/rating/list";


        }

        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "ERROR: rating not updated for id = " + id + " because of error : " + e);
            return "redirect:/rating/list";
        }


    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ratingService.getById(id);
            ratingService.delete(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "the rating with id = " + id + " has been deleted succesfully");
            return "redirect:/rating/list";
        }
        catch (Exception e)
        {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "ERROR : the rating with id = " + id + " has not been deleted");
            return "redirect:/rating/list";

        }

    }
}
