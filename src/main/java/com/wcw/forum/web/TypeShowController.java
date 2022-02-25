package com.wcw.forum.web;

import com.wcw.forum.dao.ArticleRepository;
import com.wcw.forum.po.Article;
import com.wcw.forum.po.Type;
import com.wcw.forum.service.ArticleService;
import com.wcw.forum.service.TypeService;
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
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private ArticleService articleService;

    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 5, direction = Sort.Direction.DESC, sort = {"updateTime"}) Pageable pageable,
                        @PathVariable Long id,
                        Model model){
        List<Type> types = typeService.listTypeTop(10000);
        if (id == -1){
//            從首頁轉進來分類頁面，預設選中集合中的第一個元素
            id = types.get(0).getId();
        }
        ArticleQuery articleQuery = new ArticleQuery();
        articleQuery.setTypeId(id);
        Page<Article> page = articleService.listArticle(pageable, articleQuery);
        model.addAttribute("types", types);
        model.addAttribute("page", page);
        model.addAttribute("activeType", id);
        return "type";
    }

}
