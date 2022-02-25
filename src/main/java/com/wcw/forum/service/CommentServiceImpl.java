package com.wcw.forum.service;

import com.wcw.forum.dao.CommentRepository;
import com.wcw.forum.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;



    @Override
    public List<Comment> listCommentByArticleId(Long articleId) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        List<Comment> comments = commentRepository.findByArticleIdAndParentCommentNull(articleId,sort);
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1){
            comment.setParentComment(commentRepository.getById(parentCommentId));
        } else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }



    //    裝所有回覆留言的list
    private List<Comment> tempList = new ArrayList<>();



    private List<Comment> eachComment (List<Comment> comments){
//        新增一個List
        List<Comment> commentView = new ArrayList<>();

        for (Comment comment : comments){
//            將comment複製一份，避免資料庫異動
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
//            1. 將parentComments放入List
            commentView.add(c);
        }
//        2. 將childrenList放入
        combineChildren(commentView);
        return commentView;
    }


    private void combineChildren(List<Comment> parentComments){
        for (Comment comment: parentComments){
            List<Comment> replyList1 = comment.getReplyComments();
            for (Comment reply1: replyList1){
                recursively(reply1);
            }
            comment.setReplyComments(tempList);
            tempList = new ArrayList<>();
//            tempList.clear();
        }
    }


    private void recursively(Comment reply1) {
        tempList.add(reply1);
        if (reply1.getReplyComments().size() > 0){
            for(Comment reply2: reply1.getReplyComments()){
                tempList.add(reply2);
                if(reply2.getReplyComments().size() > 0){
                    recursively(reply2);
                    }
                }
            }
        }
    }
