package com.wcw.forum.service;

import com.wcw.forum.po.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByArticleId (Long articleId);

    Comment saveComment(Comment comment);

}
