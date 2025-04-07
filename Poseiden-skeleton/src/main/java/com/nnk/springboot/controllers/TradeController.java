package com.nnk.springboot.controllers;

import com.nnk.springboot.controllers.DTO.TradeRequest;
import com.nnk.springboot.domain.Trade;
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
public class TradeController {
    private final CrudService<Trade> tradeService;

    @RequestMapping("/trade/list")
    public String home(Model model)
    {
        List<Trade> trades = tradeService.findAll();
        model.addAttribute("trades", trades);
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(TradeRequest tradeRequest) {
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Validated TradeRequest tradeRequest, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "trade/add";
        }
        try {


            Trade newTrade = new Trade(
                    tradeRequest.getAccount(),
                    tradeRequest.getType(),
                    tradeRequest.getBuyQuantity(),
                    tradeRequest.getBuyPrice(),
                    tradeRequest.getSellPrice(),
                    tradeRequest.getSellQuantity()
            );
            tradeService.save(newTrade);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "new ruleName added successfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "ERROR : new ruleName not added because of error : " + e);
        }
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer tradeId, Model model) {
        // TODO: get Trade by Id and to model then show to the form
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Validated TradeRequest tradeRequest,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Trade and return Trade list
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        // TODO: Find Trade by Id and delete the Trade, return to Trade list
        return "redirect:/trade/list";
    }
}
