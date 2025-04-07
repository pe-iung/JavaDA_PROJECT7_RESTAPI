package com.nnk.springboot.controllers;

import com.nnk.springboot.controllers.DTO.RuleNameRequest;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class RuleNameController {

    private final CrudService<RuleName> ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model)
    {
        List<RuleName> ruleNames = ruleNameService.findAll();
        model.addAttribute("ruleNames", ruleNames);
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleNameRequest ruleNameRequest) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(
            @Validated RuleNameRequest ruleNameRequest,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "rulename/add";
        }
        try {


        RuleName newRuleName = new RuleName(
                ruleNameRequest.getName(),
                ruleNameRequest.getDescription(),
                ruleNameRequest.getJson(),
                ruleNameRequest.getTemplate(),
                ruleNameRequest.getSqlStr(),
                ruleNameRequest.getSqlPart()
        );
        ruleNameService.save(newRuleName);
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "new ruleName added successfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "ERROR : new ruleName not added because of error : " + e);
        }

        return "redirect:/ruleName/list";

    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        RuleName ruleName = ruleNameService.getById(id);
        RuleNameRequest ruleNameRequest = new RuleNameRequest(
                ruleName.getName(),
                ruleName.getDescription(),
                ruleName.getJson(),
                ruleName.getTemplate(),
                ruleName.getSqlStr(),
                ruleName.getSqlPart()
        );
        model.addAttribute("ruleNameRequest", ruleNameRequest);
        model.addAttribute("ruleNameId",id);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(
            @PathVariable("id") Integer id,
            @Validated RuleNameRequest ruleNameRequest,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        // TODO: check required fields, if valid call service to update RuleName and return RuleName list
        if (result.hasErrors()) {
            return "/ruleName/list";
        }

        model.addAttribute("ruleNameId",id);
        model.addAttribute("ruleNameRequest", ruleNameRequest);
        try {
            RuleName updatedRuleName = ruleNameService.getById(id);
            updatedRuleName.setName(ruleNameRequest.getName());
            updatedRuleName.setDescription(ruleNameRequest.getDescription());
            updatedRuleName.setJson(ruleNameRequest.getJson());
            updatedRuleName.setTemplate(ruleNameRequest.getTemplate());
            updatedRuleName.setSqlStr(ruleNameRequest.getSqlStr());
            updatedRuleName.setSqlPart(ruleNameRequest.getSqlPart());
            ruleNameService.update(updatedRuleName);

            redirectAttributes.addFlashAttribute("successMessage",
                    "ruleName with id = " + id + " succesfully updated");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "ERROR: ruleName with id = " + id + " NOT updated because of error : " + e);

        }
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model,RedirectAttributes redirectAttributes) {
        try {
            ruleNameService.delete(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "the ruleName with id = " + id + " has been deleted succesfully");

        }
        catch (Exception e)
        {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "ERROR : the ruleName with id = " + id + " has not been deleted");


        }
        return "redirect:/ruleName/list";
    }
}
