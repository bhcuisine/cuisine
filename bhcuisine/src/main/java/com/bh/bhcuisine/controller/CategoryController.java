package com.bh.bhcuisine.controller;

import com.bh.bhcuisine.entity.Category;
import com.bh.bhcuisine.result.Result;
import com.bh.bhcuisine.result.ResultFactory;
import com.bh.bhcuisine.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 种类controller
 */
@RestController
public class CategoryController {

    /**
     * 注入种类service层
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * 添加分类
     * @param c
     * @return
     */
    @ApiOperation(value = "分类", notes = "分类")
    @PostMapping("/api/addcategory")
    public Result addCategory(@RequestBody Category c) {
        Category searchC = categoryService.findAllByCategoryName(c.getCategoryName());
        if (searchC == null) {
            Category category = new Category();
            category.setCategoryName(c.getCategoryName());
            categoryService.addCategory(category);
            return ResultFactory.buildSuccessResult("成功");
        } else {
            return ResultFactory.buildFailResult("已经存在");
        }


    }

    /**
     * 查询所有分类
     *
     * @return
     */
    @ApiOperation(value = "查询所有分类", notes = "查询所有分类")
    @GetMapping("/api/category")
    public Result fallAll() {
        return ResultFactory.buildSuccessResult(categoryService.fallAll());
    }
}
