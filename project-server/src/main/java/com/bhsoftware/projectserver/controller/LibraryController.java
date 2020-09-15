package com.bhsoftware.projectserver.controller;

import com.bhsoftware.projectserver.entity.Book;
import com.bhsoftware.projectserver.service.BookService;
import com.bhsoftware.projectserver.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/5
 */
@RestController
public class LibraryController {

    @Autowired
    private BookService bookService;

    @GetMapping("/api/books")
    @CrossOrigin
    public List<Book> list() {
        return bookService.list();
    }

    @PostMapping("/api/books")
    @CrossOrigin
    public Book addOrUpdate(@RequestBody Book book) {
        bookService.addOrUpdate(book);
        return book;
    }

    @PostMapping("/api/delete")
    @CrossOrigin
    public void delete(@RequestBody Book book) {
        bookService.deleteById(book.getId());
    }

    @GetMapping("/api/categories/{cid}/books")
    @CrossOrigin
    public List<Book> listByCategory(@PathVariable("cid") int cid) {
        if (cid != 0) {
            return bookService.listByCategory(cid);
        } else {
            return list();
        }
    }

    //搜索栏查询方法
    @CrossOrigin
    @GetMapping("/api/search")
    public List<Book> searchResult(@RequestParam("keywords") String keywords) {
        //关键字为空时查询所有
        if ("".equals(keywords)) {
            return bookService.list();
        } else {
            return bookService.search(keywords);
        }
    }

    //上传
    @CrossOrigin
    @PostMapping("/api/covers")
    public String coversUpload(MultipartFile file) {

        String folder = "d:/vue/img1";
        File imageFolder = new File(folder);
        File f = new File(imageFolder, StringUtils.getRandomString(6) + file.getOriginalFilename().
                substring(file.getOriginalFilename().length() - 4));
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        try {
            file.transferTo(f);
            String imgURL = "http://localhost:8090/api/file/" + f.getName();
            return imgURL;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }
}
