package com.bhsoftware.projectserver.dao;

import com.bhsoftware.projectserver.entity.Book;
import com.bhsoftware.projectserver.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/5
 */
public interface BookDao extends JpaRepository<Book,Integer> {

    List<Book> findAllByCategory(Category category);

    List<Book> findAllByTitleLikeOrAuthorLike(String keyword1,String keyword2);
}
