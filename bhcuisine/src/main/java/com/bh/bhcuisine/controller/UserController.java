package com.bh.bhcuisine.controller;

import com.bh.bhcuisine.dao.MaterialsDao;
import com.bh.bhcuisine.dto.MaterialsDto;
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
import java.util.List;
import java.util.ArrayList;
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

    /**--注释掉username参数改用shiroSession获取用户名
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
                                @RequestParam String username,
                                @RequestParam(required = false) String branchName,
                                @RequestParam(required = false) String addTime) {
//        User user=(User)SecurityUtils.getSubject().getSession().getAttribute("user");
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pagesize);
        Page<Materials> list= materialsDao.getAllByUserNameAndBranchNameAndAddTime(username,branchName,addTime,pageRequest);
        MaterialsDto materialsDto=new MaterialsDto();
        List<Integer> newtotal=new ArrayList();
        int allPrice=0;
        for (Materials materials:list.getContent()) {
           int price=materials.getPrice();
           int quanty=materials.getQuanty();
           allPrice=price*quanty;
           newtotal.add(allPrice);
        }
        int total=0;
        for (Integer i:
             newtotal) {
            total+=i;
        }
        materialsDto.setTotal(total);
        System.out.println("总成本是"+materialsDto.getTotal());
        return ResultFactory.buildSuccessResult(materialsDto);
    }
}

