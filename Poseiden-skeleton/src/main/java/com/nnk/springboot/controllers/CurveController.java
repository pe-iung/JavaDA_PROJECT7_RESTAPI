package com.nnk.springboot.controllers;

import com.nnk.springboot.controllers.DTO.CurvePointRequest;
import com.nnk.springboot.domain.CurvePoint;
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
public class CurveController {

    private final CrudService<CurvePoint> curvePointService;

    @RequestMapping("/curvePoint/list")
    public String home(Model model)
    {
        List<CurvePoint> curvePoints = curvePointService.findAll();
        model.addAttribute("curvePoints", curvePoints);
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurveForm(CurvePointRequest curvePointRequest) {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Validated CurvePointRequest curvePointRequest, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return Curve list
        model.addAttribute("curvePointRequest",curvePointRequest);
        model.addAttribute("result",result);
        if (result.hasErrors()) {
            return "curvePoint/add";
        }
        try {
            CurvePoint newCurvePoint = new CurvePoint(
                    curvePointRequest.getCurveId(),
                    curvePointRequest.getTerm(),
                    curvePointRequest.getValue()
            );
            curvePointService.save(newCurvePoint);


            return "redirect:/curvePoint/list";
        } catch (Exception e) {
            result.rejectValue("global", "error.global", "An error occurred while saving the bid");
            return "curvePoint/add";
        }
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
         CurvePoint curvepoint = curvePointService.getById(id);
         CurvePointRequest curvePointRequest = new CurvePointRequest(
                 curvepoint.getId(),
                 curvepoint.getCurveId(),
                 curvepoint.getTerm(),
                 curvepoint.getValue());

         model.addAttribute("curvePointRequest", curvePointRequest);

        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Validated CurvePointRequest curvePointRequest,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Curve and return Curve list

        if (result.hasErrors()) {
            return "curvePoint/update";
        }
        CurvePoint updatedCurvePoint = curvePointService.getById(id);
        updatedCurvePoint.setCurveId(curvePointRequest.getCurveId());
        updatedCurvePoint.setTerm(curvePointRequest.getTerm());
        updatedCurvePoint.setValue(curvePointRequest.getValue());

        curvePointService.update(updatedCurvePoint);

        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Curve by Id and delete the Curve, return to Curve list
        return "redirect:/curvePoint/list";
    }
}
