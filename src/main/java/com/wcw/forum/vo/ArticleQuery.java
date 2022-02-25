package com.wcw.forum.vo;

import com.wcw.forum.po.Tag;
import com.wcw.forum.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ArticleQuery {

    String title;
    Long typeId;
//    String tagIds;
    boolean recommend;

    public ArticleQuery() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }



//    public String getTagIds() {
//        return tagIds;
//    }
//
//    public void setTagIds(String tagIds) {
//        this.tagIds = tagIds;
//    }


    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }
}
