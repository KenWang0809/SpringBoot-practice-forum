package com.wcw.forum.service;

import com.wcw.forum.NotFoundException;
import com.wcw.forum.dao.ArticleRepository;
import com.wcw.forum.po.Article;
import com.wcw.forum.po.Type;
import com.wcw.forum.util.MyBeanUtils;
import com.wcw.forum.vo.ArticleQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class ArticleServiceImp implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
//    @Autowired
//    private TagService tagService;


    @Transactional
    @Override
    public Article getArticle(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Page<Article> listArticle(Pageable pageable, ArticleQuery article) {
        return articleRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(article.getTitle()) && article.getTitle() != null){
                    predicates.add(cb.like(root.<String>get("title"), "%" + article.getTitle() + "%"));
                }
                 if (article.getTypeId() != null){
                        predicates.add(cb.equal(root.<Type>get("type").get("id"), article.getTypeId()));
                    }
                if (article.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), article.isRecommend()));
                }
//                if (article.getTagIds() != null){
//                    predicates.add(cb.equal(root.<List<Tag>>get("tags"), tagService.listTag(article.getTagIds())));
//                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Article> listArticle(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    @Override
    public Page<Article> listArticle(Long tagId, Pageable pageable) {
        return articleRepository.findAll(new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }


    @Transactional
    @Override
    public List<Article> listArticle() {
        return articleRepository.findAll();
    }

    @Override
    public List listPics(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return articleRepository.findRecPic(pageable);
    }

    @Override
    public Map<String, List<Article>> archiveArticle() {
        List<String> years = articleRepository.groupByYear();
        Map<String,List<Article>> map = new TreeMap<>();
        for (String year : years){
            map.put(year, articleRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Page<Article> listArticle(String query, Pageable pageable) {
        return articleRepository.findByQuery(query,pageable);
    }

    @Override
    public List<Article> listMostView(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "views");
        Pageable pageable = PageRequest.of(0, size, sort);
        return articleRepository.finMostView(pageable);
    }

    @Transactional
    @Override
    public Article saveArticle(Article article) {
        if ( article.getId() == null){
            article.setCreateTime(new Date());
            article.setUpdateTime(new Date());
            article.setViews(0);
        }
        return articleRepository.save(article);
    }

    @Transactional
    @Override
    public Article updateArticle(Long id, Article article) {
        Article a = articleRepository.findById(id).orElse(null);
        if (a == null) {
            throw new NotFoundException("找不到該文章");
        }
        BeanUtils.copyProperties(article, a, MyBeanUtils.getNullPropertyNames(article));
        a.setUpdateTime(new Date());
        return articleRepository.save(a);
    }

    @Transactional
    @Override
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    @Transactional
    @Override
    public int updateViews(Long id) {
        return articleRepository.updateViews(id);
    }

    @Override
    public Long articleCount() {
        return articleRepository.count();
    }

    @Override
    public Long totalViews() {
        return articleRepository.totalViews();
    }
}
