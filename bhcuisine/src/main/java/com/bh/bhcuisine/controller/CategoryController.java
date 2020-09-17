package com.bh.bhcuisine.controller;

import com.bh.bhcuisine.entity.Category;
import com.bh.bhcuisine.result.Result;
import com.bh.bhcuisine.result.ResultFactory;
import com.bh.bhcuisine.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "分类" ,  notes="分类")
    @PostMapping("/api/category")
    public Result addCategory(@RequestBody Category category){
        try {
            categoryService.addCategory(category);
            return ResultFactory.buildSuccessResult("成功");
        }catch (Exception e){
            return ResultFactory.buildFailResult("失败");
        }
    }
}
