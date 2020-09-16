package com.bh.bhcuisine.controller;

import com.bh.bhcuisine.dao.MaterialsDao;
import com.bh.bhcuisine.entity.Materials;
import com.bh.bhcuisine.entity.User;
import com.bh.bhcuisine.result.Result;
import com.bh.bhcuisine.result.ResultFactory;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户Controller
 */
@RestController
public class UserController {

    /**
     * 注入数据dao层
     */
    @Autowired
    private MaterialsDao materialsDao;

    /**已经测试成功了--所以注释掉username参数改用shiroSession获取用户名
     *  按条件查询：时间、店名
     * @param currentPage 当前页
     * @param pagesize 每页数量
     * @param branchName 店名
     * @param addTime 添加时间
     * @return
     */
    @ApiOperation(value = "用户查询" ,  notes="用户查询")
    @GetMapping(value = "/api/serach")
    public Result getSearchData(@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                @RequestParam(value = "pagesize", required = false, defaultValue = "8") Integer pagesize,
                                //@RequestParam String username,
                                @RequestParam(required = false) String branchName,
                                @RequestParam(required = false) String addTime) {
        User user=(User)SecurityUtils.getSubject().getSession().getAttribute("user");
        String username=user.getUsername();
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pagesize);
        Page<Materials> list= materialsDao.getAllByUserNameAndBranchNameAndAddTime(username,branchName,addTime,pageRequest);
       System.out.println(list);
        return ResultFactory.buildSuccessResult(list);
    }
}

