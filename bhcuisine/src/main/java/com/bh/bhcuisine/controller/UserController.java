package com.bh.bhcuisine.controller;

import com.bh.bhcuisine.config.SaltUtil;
import com.bh.bhcuisine.config.ShiroUtil;
import com.bh.bhcuisine.dao.CastDao;
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
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * 用户Controller
 */
@RestController
@CrossOrigin
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

    @Autowired
    private CastDao castDao;

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
//                                @RequestParam(required = false) String username,
//                                @RequestBody User user,
                                @RequestParam(required = false) String branchName,
                                @RequestParam(required = false) String addTime) {
        User user=(User) SecurityUtils.getSubject().getSession().getAttribute("user");
        String username=user.getUsername();
        if(username.equals("BOSS")){
            username=null;
        }
        if(branchName.equals("BOSS")){
            branchName=null;
        }
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pagesize);
        Page<Cast> cast=castService.findAllByRAndBranchNameAndRenTime(addTime,branchName,username,pageRequest);
        System.out.println(cast.getContent());
        return ResultFactory.buildSuccessResult(cast);
    }

    /**
     * 批量获取绩效率
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量获取绩效率" ,  notes="批量获取绩效率")
    @GetMapping("/api/getPerformance")
    public Result getPerformance(@RequestParam List<Integer> ids){
        List<Cast> casts=castDao.findAllByIdIn(ids);
        Integer performance=casts.get(0).getPerformance();
        Map<String,Object> map=new HashMap();
        map.put("performance",performance);
        map.put("ids",ids);
        return ResultFactory.buildSuccessResult(map);
    }
    /**
     * 添加绩效率
     * @param performance
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量保存绩效率" ,  notes="批量保存绩效率")
    @PostMapping("/api/savePerformance")
    public Result savePerformance(@RequestParam Integer performance,@RequestParam List<Integer> ids){
        System.out.println(ids);
        for (Integer id:
                ids) {
            Cast cast=castService.findAllById(id);
            castService.updatePerformanceByBranchNameIn(performance, id);
            Integer monthTotal=cast.getMonthTotal();
            Integer employeeTotal=cast.getEmployeeTotal();
            Integer rentTotal=cast.getRentTotal();
            Double costTotal=cast.getCostTotal();
            BigDecimal monthMoney=new BigDecimal(monthTotal);//总金额转为bigDecimal
            BigDecimal costMoney=new BigDecimal(costTotal);//成本转为bigDecimal
            BigDecimal rentMoney=new BigDecimal(rentTotal);//租金转为bigDecimal
            BigDecimal employMoney=new BigDecimal(employeeTotal);//获取人工成本
            BigDecimal profit=monthMoney.subtract(costMoney).subtract(rentMoney).subtract(employMoney);
            double pro=profit.doubleValue();
            try {
                //将绩效率转换为0.00后几位
                Double per = (Double) NumberFormat.getPercentInstance().parse(performance + "%");
                //将绩效率转换为bigDecimal
                BigDecimal newPer = new BigDecimal(per);
                //利润乘以绩效率
                BigDecimal newProfit = profit.multiply(newPer);
                //设置保留小数点后2位
                BigDecimal performance_total = newProfit.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                //将最终绩效转换为double保存数据库
                double performanceTotal = performance_total.doubleValue();
                castDao.updateCast(costTotal, employeeTotal, monthTotal, rentTotal, pro, performanceTotal, id);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("保存成功");
        return ResultFactory.buildSuccessResult("成功");
    }

    /**
     * 修改绩效率
     * @param performance
     * @param id
     * @return
     */
    @ApiOperation(value = "修改绩效率" , notes="修改绩效率")
    @PostMapping("/api/updatePerformance")
    public Result update(@RequestParam Integer performance,@RequestParam Integer id){
        Cast cast=castService.findAllById(id);
        castService.updatePerformanceByBranchNameIn(performance, id);
        Integer monthTotal=cast.getMonthTotal();
        Integer employeeTotal=cast.getEmployeeTotal();
        Integer rentTotal=cast.getRentTotal();
        Double costTotal=cast.getCostTotal();
        BigDecimal monthMoney=new BigDecimal(monthTotal);//总金额转为bigDecimal
        BigDecimal costMoney=new BigDecimal(costTotal);//成本转为bigDecimal
        BigDecimal rentMoney=new BigDecimal(rentTotal);//租金转为bigDecimal
        BigDecimal employMoney=new BigDecimal(employeeTotal);//获取人工成本
        BigDecimal profit=monthMoney.subtract(costMoney).subtract(rentMoney).subtract(employMoney);
        double pro=profit.doubleValue();
        try {
            //将绩效率转换为0.00后几位
            Double per = (Double) NumberFormat.getPercentInstance().parse(performance + "%");
            //将绩效率转换为bigDecimal
            BigDecimal newPer = new BigDecimal(per);
            //利润乘以绩效率
            BigDecimal newProfit = profit.multiply(newPer);
            //设置保留小数点后2位
            BigDecimal performance_total = newProfit.setScale(2, BigDecimal.ROUND_HALF_DOWN);
            //将最终绩效转换为double保存数据库
            double performanceTotal = performance_total.doubleValue();
            castDao.updateCast(costTotal, employeeTotal, monthTotal, rentTotal, pro, performanceTotal, id);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("保存成功");
        return ResultFactory.buildSuccessResult("成功");
    }

    /**
     * 得到所有店名
     * @return
     */
    @ApiOperation(value = "得到所有店" ,  notes="得到所有店名")
    @GetMapping("/api/getAllBranchName")
    public Result getAllCast(){
        List<User> users=userService.getAllBranchName();
        List<String> branchName=new ArrayList();
        for (User u:
             users) {
            branchName.add(u.getBranchName());
        }
        return ResultFactory.buildSuccessResult(branchName);
    }
    /**
     * 根据id得到盈利数据实体
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id得到盈利实体" ,  notes="根据id得到盈利实体")
    @GetMapping("/api/getCast")
    public Result getCast(Integer id){
        Cast cast=castService.findAllById(id);
        return ResultFactory.buildSuccessResult(cast);
    }
    /**
     * 插入数据
     * @param username
     * @param monthTotal
     * @param employeeTotal
     * @param rentTotal
     * @param id
     * @return
     */
    @ApiOperation(value = "勾选插入已存在月份数据也就是修改数据" ,  notes="插入数据也就是修改数据")
    @PostMapping("/api/addCast2")
    public Result addCast2(
//            @RequestParam String username,
                          @RequestParam Integer monthTotal,
                          @RequestParam Integer employeeTotal,
                          @RequestParam Integer rentTotal,
                             @RequestParam Integer id){
        User user=(User)SecurityUtils.getSubject().getSession().getAttribute("user");
        String username=user.getUsername();
        Integer performance=castService.findAllById(id).getPerformance();
        Double costTotal=castService.findAllById(id).getCostTotal();
        BigDecimal monthMoney=new BigDecimal(monthTotal);//总金额转为bigDecimal
        BigDecimal costMoney=new BigDecimal(costTotal);//成本转为bigDecimal
        BigDecimal rentMoney=new BigDecimal(rentTotal);//租金转为bigDecimal
        BigDecimal employMoney=new BigDecimal(employeeTotal);//获取人工成本
        BigDecimal profit=monthMoney.subtract(costMoney).subtract(rentMoney).subtract(employMoney);
        double pro=profit.doubleValue();
        System.out.println(pro);
        try {
            //将绩效率转换为0.00后几位
            Double per = (Double) NumberFormat.getPercentInstance().parse(performance + "%");
            //将绩效率转换为bigDecimal
            BigDecimal newPer = new BigDecimal(per);
            //利润乘以绩效率
            BigDecimal newProfit = profit.multiply(newPer);
            //设置保留小数点后2位
            BigDecimal performance_total = newProfit.setScale(2, BigDecimal.ROUND_HALF_DOWN);
            //将最终绩效转换为double保存数据库
            double performanceTotal = performance_total.doubleValue();
            castDao.updateCast(costTotal,employeeTotal,monthTotal,rentTotal,pro,performanceTotal,id);
            return ResultFactory.buildSuccessResult("成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultFactory.buildFailResult("失败");
    }
    /**
     * 添加新店
     * @param username 用户名这个是前端传过来不能是shiro
     * @param password 密码
     * @param enabled 使能状态
     * @param branchName 店名
     * @param branchLocation 店位置
     * @param performance 绩效
     * @return
     */
    @ApiOperation(value = "添加新店" ,  notes="添加新店")
    @PostMapping(value = "/api/saveUser")
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
        return ResultFactory.buildSuccessResult("成功");
    }



}

