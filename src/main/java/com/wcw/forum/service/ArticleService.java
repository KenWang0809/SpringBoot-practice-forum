package com.wcw.forum.service;

import com.wcw.forum.po.Article;
import com.wcw.forum.vo.ArticleQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    Article getArticle(Long id);

    Page<Article> listArticle(Pageable pageable, ArticleQuery article);

    Page<Article> listArticle(Pageable pageable);

    Page<Article> listArticle(Long tagId,Pageable pageable);

    List<Article> listArticle();

    List listPics(Integer size);

    Map<String, List<Article>> archiveArticle();

    Page<Article> listArticle(String query, Pageable pageable);

    List<Article> listMostView(Integer size);

    Article saveArticle(Article article);

    Article updateArticle(Long id, Article article);

    void deleteArticle(Long id);

    int updateViews(Long id);

    Long articleCount();

    Long totalViews();

}
