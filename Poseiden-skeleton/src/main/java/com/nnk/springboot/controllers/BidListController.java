package com.nnk.springboot.controllers;

import com.nnk.springboot.configuration.SpringSecurityConfig;
import com.nnk.springboot.controllers.DTO.BidListRequest;
import com.nnk.springboot.domain.BidList;
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

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BidListController {

    private final CrudService<BidList> bidService;
    private final SpringSecurityConfig springSecurityConfig;

    @RequestMapping("/bidList/list")
    public String home(Model model)
    {
        List<BidList> bidLists= bidService.findAll();
        model.addAttribute("bidLists", bidLists);

        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidListRequest bidListRequest) {

        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Validated BidListRequest bidListRequest, BindingResult result, Model model) {

        model.addAttribute("bidListRequest", bidListRequest);
        model.addAttribute("result", result);
        if (result.hasErrors()) {
            // If there are errors, return to the form with error messages
            return "bidList/add";
        }
        try {
            bidService.save(new BidList(
                    bidListRequest.getAccount(),
                    bidListRequest.getType(),
                    bidListRequest.getBidQuantity()
            ));
            // Redirect to list page after successful save
            return "redirect:/bidList/list";
        } catch (Exception e) {
            // Handle any errors during save
            result.rejectValue("global", "error.global", "An error occurred while saving the bid");
            return "bidList/add";
        }

    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Bid by Id and to model then show to the form
        model.addAttribute("bidListId", id);
        BidList bidlist = bidService.getById(id);
        BidListRequest bidListRequest = new BidListRequest(
                bidlist.getAccount(),
                bidlist.getType(),
                bidlist.getBidQuantity());
        model.addAttribute("bidListRequest", bidListRequest);
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Validated BidListRequest bidListRequest,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Bid and return list Bid
        model.addAttribute("bidListId", id);
        model.addAttribute("bidListRequest", bidListRequest);
        model.addAttribute("result", result);

        if (result.hasErrors()) {
            return "bidList/update";
        }

        try {
            bidService.update(new BidList(id,
                    bidListRequest.getAccount(),
                    bidListRequest.getType(),
                    bidListRequest.getBidQuantity()
            ));
            return "redirect:/bidList/list";
        } catch (Exception e) {
            result.rejectValue("global", "error.global", "An error occurred while saving the bid");
            return "bidList/update";
        }
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Bid by Id and delete the bid, return to Bid list
        model.addAttribute("id",id);
        bidService.delete(id);
        return "redirect:/bidList/list";
    }
}
