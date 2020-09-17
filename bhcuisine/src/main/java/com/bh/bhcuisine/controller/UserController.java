package com.bh.bhcuisine.controller;

import com.bh.bhcuisine.config.SaltUtil;
import com.bh.bhcuisine.config.ShiroUtil;
import com.bh.bhcuisine.dao.MaterialsDao;
import com.bh.bhcuisine.entity.Cast;
import com.bh.bhcuisine.entity.Materials;
import com.bh.bhcuisine.entity.User;
import com.bh.bhcuisine.result.Result;
import com.bh.bhcuisine.result.ResultFactory;
import com.bh.bhcuisine.service.CastService;
import com.bh.bhcuisine.service.MaterialsService;
import com.bh.bhcuisine.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

/**
 * 用户Controller
 */
@RestController
public class UserController {

    /**
     * 注入材料服务service层
     */
    @Autowired
    private MaterialsService materialsService;

    /**
     * 注入用户服务service层
     */
    @Autowired
    private UserService userService;
    /**
     * 注入成本盈利服务service层
     */
    @Autowired
    private CastService castService;

    /**--注释掉username参数改用shiroSession获取用户名
     * 根据前端自主调整传入对象是requestBody还是requestParam
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
                                @RequestParam(required = false) String username,
//                                @RequestBody User user,
                                @RequestParam(required = false) String branchName,
                                @RequestParam(required = false) String addTime) {
//        User user=(User)SecurityUtils.getSubject().getSession().getAttribute("user");
//        String username=user.getUsername();
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pagesize);
        Page<Cast> cast=castService.findAllByRAndBranchNameAndRenTime(addTime,branchName,username,pageRequest);
        return ResultFactory.buildSuccessResult(cast);
    }

    /**
     * 添加绩效率
     * @param performance
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量保存绩效" ,  notes="批量保存绩效")
    @PostMapping("/api/savePerformance")
    public Result savePerformance(@RequestParam Integer performance,@RequestParam List<Integer> ids){
        for (Integer id:
                ids) {
                castService.updatePerformanceByBranchNameIn(performance,id);
    public Result savePerformance(@RequestParam Integer performance,@RequestParam List<String> branchName,@RequestParam String time){
        for (String b: branchName) {
                castService.updatePerformanceByBranchNameIn(performance,b,time);
        }
        System.out.println("保存成功");
        return ResultFactory.buildSuccessResult(performance);
    }

    @ApiOperation(value = "插入数据" ,  notes="插入数据")
    @PostMapping("/api/addCast")
    public Result addCast(@RequestParam String username){
        //设置利润默认值
        Double profitTotal=1257888.8999;
        //将利润转换为bigDecimal对象
        BigDecimal profit=new BigDecimal(profitTotal);
        //得到int型绩效率
        int performance=3;
        try{
            //将绩效率转换为0.00后几位
            Double per = (Double) NumberFormat.getPercentInstance().parse(performance+"%");
            //将绩效率转换为bigDecimal
            BigDecimal newPer =new BigDecimal(per);
            //利润乘以绩效率
            BigDecimal newProfit=profit.multiply(newPer);
            //设置保留小数点后2位
            BigDecimal performance_total = newProfit.setScale(2,BigDecimal.ROUND_HALF_DOWN);
            //将最终绩效转换为double保存数据库
            double performanceTotal=performance_total.doubleValue();
            System.out.println("最终绩效"+performanceTotal);
            return ResultFactory.buildSuccessResult(performanceTotal);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultFactory.buildFailResult("失败");
    }
    /**
     * 添加新店
     * @param username 用户名
     * @param password 密码
     * @param enabled 使能状态
     * @param branchName 店名
     * @param branchLocation 店位置
     * @param performance 绩效
     * @return
     */
    @ApiOperation(value = "添加新店" ,  notes="添加新店")
    @GetMapping(value = "/api/saveUser")
    public Result addUser(@RequestParam String username,
                          @RequestParam(required = false) String password,
                          @RequestParam(required = false,defaultValue = "1") Integer enabled,
                          @RequestParam String branchName,
                          @RequestParam String branchLocation
                          ){
        User user =new User();
        user.setUsername(username);
        //保存密码
        String salt= SaltUtil.getSalt();
        user.setPassword(ShiroUtil.sha256(password, salt));
        user.setSalt(salt);
        user.setBranchLocation(branchLocation);
        user.setBranchName(branchName);
        user.setEnabled(enabled);
        user.setStatus(2);
        userService.addUser(user);
//        System.out.println("保存成功"+user.toString());
        return ResultFactory.buildSuccessResult("成功");
    }



}

