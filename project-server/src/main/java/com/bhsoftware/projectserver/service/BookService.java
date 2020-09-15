package com.bhsoftware.projectserver.service;

import com.bhsoftware.projectserver.dao.BookDao;
import com.bhsoftware.projectserver.entity.Book;
import com.bhsoftware.projectserver.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/5
 */
@Service
public class BookService {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private CategoryService categoryService;

    public List<Book> list(){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        return bookDao.findAll(sort);
    }

    public void addOrUpdate(Book book){
        bookDao.save(book);
    }

    public void deleteById(int id){
        bookDao.deleteById(id);

    }

    public List<Book> listByCategory(int cid){
        Category category = categoryService.get(cid);
        return bookDao.findAllByCategory(category);
    }

    //搜索栏查询方法
    public List<Book> search(String keywords){
        return bookDao.findAllByTitleLikeOrAuthorLike('%'+keywords+'%','%'+keywords+'%');
    }
}
