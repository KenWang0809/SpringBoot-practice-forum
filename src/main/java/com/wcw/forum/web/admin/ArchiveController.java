package com.wcw.forum.web.admin;

import com.wcw.forum.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/archives")
    public String archive(Model model){
        model.addAttribute("archiveMap", articleService.archiveArticle());
        model.addAttribute("articleCount", articleService.articleCount());
        return "archive";
    }
}
