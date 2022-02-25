package com.wcw.forum.web.admin;

import com.wcw.forum.po.Tag;
import com.wcw.forum.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    TagService tagService;

    //    查詢分頁列表
    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<Tag> page = tagService.listTag(pageable);
        model.addAttribute("page", page);
        return "admin/tags";
    }

    //    轉跳到新增頁面
    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tag_input";
    }

    //    轉跳修改頁面 (修改按鈕)
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Tag tag, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tag_input"; //新增與修改共用一個頁面
    }

    //    送出新增請求
    @PostMapping("/tags")
    public String post(@Valid Tag tag,
                       BindingResult result,
                       RedirectAttributes attribute) {

        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name", "nameError", "不能新增重複的分類");
        }
        if (result.hasErrors()) {
            return "admin/tag_input";
        }
        Tag tag2 = tagService.saveTag(tag);
        if (tag2 == null) {
            attribute.addFlashAttribute("message", "分類新增失敗！");
        } else {
            attribute.addFlashAttribute("message", "分類新增成功！");
        }
        return "redirect:/admin/tags";
    }

    //    送出修改請求
    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag,
                       BindingResult result,
                       @PathVariable Long id,
                       RedirectAttributes attribute) {

        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name", "nameError", "有重複名稱的分類，請取其他分類名稱");
        }
        if (result.hasErrors()) {
            return "admin/tag_input";
        }
        Tag tag2 = tagService.updateTag(id, tag);
        if (tag2 == null) {
            attribute.addFlashAttribute("message", "分類修改失敗！");
        } else {
            attribute.addFlashAttribute("message", "分類修改成功！");
        }
        return "redirect:/admin/tags";
    }


    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "分類刪除成功！");
        return "redirect:/admin/tags";
    }
}
