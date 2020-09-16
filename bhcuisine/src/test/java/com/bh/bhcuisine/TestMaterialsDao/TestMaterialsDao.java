package com.bh.bhcuisine.TestMaterialsDao;

import com.bh.bhcuisine.dao.MaterialsDao;
import com.bh.bhcuisine.entity.Materials;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试得到分页数据测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMaterialsDao {

    /**
     * 注入数据dao层
     */
    @Autowired
    private MaterialsDao materialsDao;

    /**
     *  按条件查询：时间、店名
     *  currentPage 当前页
     *  pagesize 每页大小
     *  username 用户名
     *  branchName 店名
     *  addTime 添加时间
     * @return
     */
    @Test
    public void TestMaterialsDao(){
        String username="康老师";
        String branchName="高新店";
        String addTime="2020-09";
        PageRequest pageRequest = PageRequest.of(0,8);
        Page<Materials> list= materialsDao.getAllByUserNameAndBranchNameAndAddTime(username,branchName,addTime,pageRequest);
        for (Materials u:
             list) {
            System.out.println(u);
        }
    }
}
