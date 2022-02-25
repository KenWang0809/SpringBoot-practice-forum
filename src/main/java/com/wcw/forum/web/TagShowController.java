package com.wcw.forum.web;

import com.wcw.forum.po.Article;
import com.wcw.forum.po.Tag;
import com.wcw.forum.service.ArticleService;
import com.wcw.forum.service.TagService;
import com.wcw.forum.vo.ArticleQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleService articleService;

    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(size = 5, direction = Sort.Direction.DESC, sort = {"updateTime"}) Pageable pageable,
                        @PathVariable Long id,
                        Model model){
        List<Tag> tags = tagService.listTagTop(10000);
        if (id == -1){
//            從首頁轉進來分類頁面，預設選中集合中的第一個元素
            id = tags.get(0).getId();
        }
        model.addAttribute("tags", tags);
        model.addAttribute("page", articleService.listArticle(id,pageable));
        model.addAttribute("activeTag", id);
        return "tag";
    }

}
