package com.wcw.forum.web.admin;

import com.wcw.forum.po.Type;
import com.wcw.forum.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    TypeService typeService;

    //    查詢分頁列表
    @GetMapping("/types")
    public String types(@PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<Type> page = typeService.listType(pageable);
        model.addAttribute("page", page);
        return "admin/types";
    }

    //    轉跳到新增頁面
    @GetMapping("/types/input")
    public String input(Model model) {
        model.addAttribute("type", new Type());
        return "admin/type_input";
    }

    //    轉跳修改頁面 (修改按鈕)
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Type type, Model model) {
        model.addAttribute("type", typeService.getType(id));
        return "admin/type_input"; //新增與修改共用一個頁面
    }

    //    送出新增請求
    @PostMapping("/types")
    public String post(@Valid Type type,
                       BindingResult result,
                       RedirectAttributes attribute) {

        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null) {
            result.rejectValue("name", "nameError", "不能新增重複的分級");
        }
        if (result.hasErrors()) {
            return "admin/type_input";
        }
        Type type2 = typeService.saveType(type);
        if (type2 == null) {
            attribute.addFlashAttribute("message", "分級新增失敗！");
        } else {
            attribute.addFlashAttribute("message", "分級新增成功！");
        }
        return "redirect:/admin/types";
    }

    //    送出修改請求
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type,
                       BindingResult result,
                       @PathVariable Long id,
                       RedirectAttributes attribute) {

        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null) {
            result.rejectValue("name", "nameError", "有重複名稱的分級，請取其他分級名稱");
        }
        if (result.hasErrors()) {
            return "admin/type_input";
        }
        Type type2 = typeService.updateType(id, type);
        if (type2 == null) {
            attribute.addFlashAttribute("message", "分級修改失敗！");
        } else {
            attribute.addFlashAttribute("message", "分級修改成功！");
        }
        return "redirect:/admin/types";
    }


    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "分級刪除成功！");
        return "redirect:/admin/types";
    }
}
