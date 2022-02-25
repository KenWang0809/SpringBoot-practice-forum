package com.wcw.forum.web;

import com.wcw.forum.NotFoundException;
import com.wcw.forum.po.Article;
import com.wcw.forum.service.ArticleService;
import com.wcw.forum.service.CommentService;
import com.wcw.forum.service.TagService;
import com.wcw.forum.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {
    @Autowired
    ArticleService articleService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;
    @Autowired
    CommentService commentService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model){
        model.addAttribute("page", articleService.listArticle(pageable));
        model.addAttribute("types", typeService.listTypeTop(5));
        model.addAttribute("tags", tagService.listTagTop(8));
        model.addAttribute("mostViewArticles", articleService.listMostView(5));
        model.addAttribute("totalViews", articleService.totalViews());
        return "index";
    }

    @GetMapping("/article/{id}")
    public String article(@PathVariable Long id, Model model){
        Article a = articleService.getArticle(id);
        if (a != null){
            articleService.updateViews(id);
//            a.setViews(a.getViews() + 1);
            model.addAttribute("article", articleService.getArticle(id));
        }else{
            throw new NotFoundException("文章不存在！");
        }
        return "article";
    }


    @PostMapping("/search")
    public String search(@PageableDefault(size = 5, sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable,
                         Model model, String query){
//        模糊查詢 '%內容%' ，內容前後加上%符號
        model.addAttribute("page", articleService.listArticle("%" + query +"%",pageable));
//       為了搜尋後，再搜尋的input內還有原本搜尋的字串，因此傳送到前台
        model.addAttribute("query", query);
        return "search";
    }


    @GetMapping("/footer")
    public String footer(Model model){
        model.addAttribute("mostViewArticles", articleService.listMostView(3));
        return "_fragments :: mostView";
    }





}
