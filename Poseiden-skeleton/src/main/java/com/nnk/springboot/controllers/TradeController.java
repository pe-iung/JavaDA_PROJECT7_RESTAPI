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

        Trade trade = tradeService.getById(tradeId);
        TradeRequest tradeRequest = new TradeRequest(
                trade.getAccount(),
                trade.getType(),
                trade.getBuyQuantity(),
                trade.getSellQuantity(),
                trade.getBuyPrice(),
                trade.getSellPrice()
        );

        model.addAttribute("tradeId", tradeId);
        model.addAttribute("tradeRequest", tradeRequest);
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id,
                              @Validated TradeRequest tradeRequest,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        // TODO: check required fields, if valid call service to update Trade and return Trade list

        if (result.hasErrors())
        {
            model.addAttribute("tradeId", id);
            return "trade/update";
        }

        model.addAttribute("tradeId", id);
        model.addAttribute("tradeRequest", tradeRequest);

        try {
            Trade updatedTrade = tradeService.getById(id);
            updatedTrade.setAccount(tradeRequest.getAccount());
            updatedTrade.setType(tradeRequest.getType());
            updatedTrade.setBuyQuantity(tradeRequest.getBuyQuantity());
            updatedTrade.setSellQuantity(tradeRequest.getSellQuantity());
            updatedTrade.setBuyPrice(tradeRequest.getBuyPrice());
            updatedTrade.setSellPrice(tradeRequest.getSellPrice());

            tradeService.update(updatedTrade);

            redirectAttributes.addFlashAttribute("successMessage",
                    "trade with id = " + id + " succesfully updated");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "ERROR: trade with id = " + id + " NOT updated because of error : " + e);

        }

        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            tradeService.getById(id);
            tradeService.delete(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "the trade with id = " + id + " has been deleted succesfully");

        }
        catch (Exception e)
        {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "ERROR : the trade with id = " + id + " has not been deleted");


        }        return "redirect:/trade/list";
    }
}
