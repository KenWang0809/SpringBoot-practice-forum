package com.wcw.forum.web.admin;

import com.wcw.forum.dao.ArticleRepository;
import com.wcw.forum.po.Article;
import com.wcw.forum.po.User;
import com.wcw.forum.service.ArticleService;
import com.wcw.forum.service.TagService;
import com.wcw.forum.service.TypeService;
import com.wcw.forum.vo.ArticleQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class ArticleController {

    private static final String INPUT = "admin/article_input" ;
    private static final String LIST = "admin/articles";
    private static final String REDIRECT = "redirect:/admin/articles";



    @Autowired
    private ArticleService articleService;
    @Autowired
    private TagService tagService;
    @Autowired
    private TypeService typeService;

    @GetMapping("/articles")
    public String articles(Model model, @PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, ArticleQuery article){
        model.addAttribute("types", typeService.listType());
//        model.addAttribute("tages", tagService.listTag());
        model.addAttribute("page", articleService.listArticle(pageable, article));
       return "admin/articles";
    }

    @PostMapping("/articles/search")
    public String search(Model model, @PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, ArticleQuery article){
        model.addAttribute("page", articleService.listArticle(pageable, article));
        return "admin/articles :: articleList";
    }

    @GetMapping("/articles/input")
    public String input(Model model){
        this.setTypeAndTag(model);
        model.addAttribute("article", new Article());
        return "admin/article_input";
    }

    @GetMapping("/articles/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        this.setTypeAndTag(model);
        Article article = articleService.getArticle(id);
        article.init();
        model.addAttribute("article", article);
        return "admin/article_input";
    }

    private void setTypeAndTag(Model model){
        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("types", typeService.listType());
    }



    @PostMapping("/articles")
    public String post(Article article, RedirectAttributes attributes, HttpSession session){
        article.setUser((User) session.getAttribute("user"));
//        article.setType(article.getType());
        article.setType(typeService.getType(article.getType().getId()));
        article.setTags(tagService.listTag(article.getTagIds()));

        Article a;
        if (article.getId() == null){
           a =  articleService.saveArticle(article);
        } else {
           a = articleService.updateArticle(article.getId(), article);
        }

        if (a == null) {
            attributes.addFlashAttribute("message", "操作失敗！");
        } else {
            attributes.addFlashAttribute("message", "操作成功！");
        }
        return REDIRECT;
    }

    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attribute){
        articleService.deleteArticle(id);
        attribute.addFlashAttribute("message", "刪除成功！");
        return REDIRECT;
    }


}
