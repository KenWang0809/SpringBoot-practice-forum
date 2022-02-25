package com.wcw.forum.dao;

import com.wcw.forum.po.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    @Query("select a.firstPicture from Article a where a.recommend = true ")
    List findRecPic(Pageable pageable);

    @Query("select a from Article a where a.views > 0 ")
    List<Article> finMostView(Pageable pageable);


//    一般SQL語句模糊查詢 select * from Article where title like '%內容%'
//    需要額為加上%%做匹配 >>放在controller
    @Query("select a from Article a where a.title like ?1 or a.content like ?1")
    Page<Article> findByQuery(String query, Pageable pageable);

//    修改必須加上    @Modifying 註解
    @Modifying
    @Query("update Article a set a.views = a.views+1 where a.id = ?1")
    int updateViews(Long id);

    @Query("select function('date_format',a.updateTime,'%Y') as year from Article a group by year order by function('date_format',a.updateTime,'%Y') desc")
    List<String> groupByYear();

    @Query("select a from Article a where function('date_format',a.updateTime,'%Y') = ?1 ")
    List<Article> findByYear(String year);

    @Query(value = "select sum(a.views) from Article a")
    Long totalViews();
}
