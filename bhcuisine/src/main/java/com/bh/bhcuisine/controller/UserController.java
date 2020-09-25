package com.bh.bhcuisine.controller;

import com.bh.bhcuisine.config.SaltUtil;
import com.bh.bhcuisine.config.ShiroUtil;
import com.bh.bhcuisine.dao.CastDao;
import com.bh.bhcuisine.dao.MaterialsDao;
import com.bh.bhcuisine.dto.PerformanceDto;
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
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

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


    @ApiOperation(value = "管理员查询", notes = "管理员查询")
    @GetMapping(value = "/api/adminSearch")
    public Result getSearchData(@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pagesize,
                                @RequestParam(required = false) String branchName,
                                @RequestParam(required = false) String addTime) {
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pagesize);
        Page<Cast> cast = castService.findAllByRAndBranchNameAndRenTime2(addTime, branchName, pageRequest);
        return ResultFactory.buildSuccessResult(cast);
    }

    /**
     * 批量获取绩效率
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量获取绩效率", notes = "批量获取绩效率")
    @GetMapping("/api/getListPerformance")
    public Result getListPerformance(@RequestParam List<Integer> ids) {
        List<Cast> casts = castDao.findAllByIdIn(ids);
        Integer performance = casts.get(0).getPerformance();
        Map<String, Object> map = new HashMap();
        map.put("performance", performance);
        map.put("ids", ids);
        return ResultFactory.buildSuccessResult(map);
    }

    /**
     * 批量添加绩效率
     *
     * @param performance
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量修改绩效率", notes = "批量修改绩效率")
    @PostMapping("/api/savePerformance")
    public Result savePerformance(@RequestBody PerformanceDto performanceDto
//            @RequestParam Integer performance,@RequestParam List<Integer> ids
    ) {
        System.out.println(performanceDto.getIds());
        List<Integer> ids = performanceDto.getIds();
        for (Integer id :
                ids) {
            Cast cast = castService.findAllById(id);
            castService.updatePerformanceByBranchNameIn(performanceDto.getPerformance(), id);
            Double monthTotal = cast.getMonthTotal();
            Double employeeTotal = cast.getEmployeeTotal();
            Double rentTotal = cast.getRentTotal();
            Double costTotal = cast.getCostTotal();
            BigDecimal monthMoney = new BigDecimal(monthTotal);//总金额转为bigDecimal
            BigDecimal costMoney = new BigDecimal(costTotal);//成本转为bigDecimal
            BigDecimal rentMoney = new BigDecimal(rentTotal);//租金转为bigDecimal
            BigDecimal employMoney = new BigDecimal(employeeTotal);//获取人工成本
            BigDecimal profit = monthMoney.subtract(costMoney).subtract(rentMoney).subtract(employMoney);
            double pro = profit.doubleValue();
            try {
                //将绩效率转换为0.00后几位
                Double per = (Double) NumberFormat.getPercentInstance().parse(performanceDto.getPerformance() + "%");
                //将绩效率转换为bigDecimal
                BigDecimal newPer = new BigDecimal(per);
                //利润乘以绩效率
                BigDecimal newProfit = profit.multiply(newPer);
                //设置保留小数点后2位
                BigDecimal performance_total = newProfit.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                //将最终绩效转换为double保存数据库
                double performanceTotal = performance_total.doubleValue();
                castDao.updateCast(costTotal, employeeTotal, monthTotal, rentTotal, pro, performanceTotal, id);
            } catch (RuntimeException | ParseException e) {
                e.printStackTrace();
            }
        }
        return ResultFactory.buildSuccessResult("成功");
    }

    /**
     * 获取绩效率
     */
    @ApiOperation(value = "获取绩效率", notes = "获取绩效率")
    @GetMapping("/api/getPerformance")
    public Result getListPerformance(
            @RequestParam Integer id
    ) {
        Cast cast = castDao.findAllById(id);
        Integer performance = cast.getPerformance();
        return ResultFactory.buildSuccessResult(performance);
    }

    /**
     * 修改绩效率
     *
     * @param performance
     * @param id
     * @return
     */
    @ApiOperation(value = "修改绩效率", notes = "修改绩效率")
    @PostMapping("/api/updatePerformance")
    public Result update(
            @RequestBody Cast c
    ) {
        System.out.println(c.getId());
        System.out.println(c.getPerformance());
        Cast cast = castService.findAllById(c.getId());
        castService.updatePerformanceByBranchNameIn(c.getPerformance(), c.getId());
        Double monthTotal =cast.getMonthTotal() ;
        Double employeeTotal = cast.getEmployeeTotal();
        Double rentTotal = cast.getRentTotal();
        Double costTotal = cast.getCostTotal();
        BigDecimal monthMoney = new BigDecimal(monthTotal);//总金额转为bigDecimal
        BigDecimal costMoney = new BigDecimal(costTotal);//成本转为bigDecimal
        BigDecimal rentMoney = new BigDecimal(rentTotal);//租金转为bigDecimal
        BigDecimal employMoney = new BigDecimal(employeeTotal);//获取人工成本
        BigDecimal profit = monthMoney.subtract(costMoney).subtract(rentMoney).subtract(employMoney);
        double pro = profit.doubleValue();
        try {
            //将绩效率转换为0.00后几位
            Double per = (Double) NumberFormat.getPercentInstance().parse(c.getPerformance() + "%");
            //将绩效率转换为bigDecimal
            BigDecimal newPer = new BigDecimal(per);
            //利润乘以绩效率
            BigDecimal newProfit = profit.multiply(newPer);
            //设置保留小数点后2位
            BigDecimal performance_total = newProfit.setScale(2, BigDecimal.ROUND_HALF_DOWN);
            //将最终绩效转换为double保存数据库
            double performanceTotal = performance_total.doubleValue();
            castDao.updateCast(costTotal, employeeTotal, monthTotal, rentTotal, pro, performanceTotal, c.getId());
        } catch (RuntimeException | ParseException e) {
            e.printStackTrace();
        }
        return ResultFactory.buildSuccessResult("成功");
    }

    /**
     * 得到所有店名--去除boss店
     *
     * @return
     */
    @ApiOperation(value = "得到所有店", notes = "得到所有店名")
    @GetMapping("/api/getAllBranchName")
    public Result getAllCast() {
        List<User> users = userService.getAllBranchName();
        System.out.println(users);
        List<User> newUser = new ArrayList();
        Iterator<User> it = users.iterator();
        while (it.hasNext()) {
            User u = it.next();
            if (u.getBranchName().equals("BOSS")) {
                it.remove();
            } else {
                newUser.add(u);
            }
        }
        return ResultFactory.buildSuccessResult(newUser);
    }

    /**
     * 根据id得到盈利数据实体
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id得到盈利实体", notes = "根据id得到盈利实体")
    @GetMapping("/api/getCast")
    public Result getCast(@RequestParam Integer id) {
        System.out.println(id);
        Cast cast = castService.findAllById(id);
        return ResultFactory.buildSuccessResult(cast);
    }


    /**
     * 添加新店
     *
     * @param username       用户名这个是前端传过来不能是shiro
     * @param password       密码
     * @param enabled        使能状态
     * @param branchName     店名
     * @param branchLocation 店位置
     * @param performance    绩效
     * @return
     */
    @ApiOperation(value = "添加新店", notes = "添加新店")
    @PostMapping(value = "/api/saveUser")
    public Result addUser(@RequestBody User reuser
//            @RequestParam String username,
//                          @RequestParam(required = false) String password,
//                          @RequestParam(required = false,defaultValue = "1") Integer enabled,
//                          @RequestParam String branchName,
//                          @RequestParam String branchLocation
    ) {
        User u = userService.getByUsername(reuser.getUsername());
        if (u != null) {
            return ResultFactory.buildFailResult("已存在");
        } else {
            User user = new User();
            user.setUsername(reuser.getUsername());
            //保存密码
            String salt = SaltUtil.getSalt();
            String password = "123";
            user.setPassword(ShiroUtil.sha256(password, salt));
            user.setSalt(salt);
            user.setBranchLocation(reuser.getBranchLocation());
            user.setBranchName(reuser.getBranchName());
            user.setEnabled(1);
            user.setStatus(2);
            userService.addUser(user);
            return ResultFactory.buildSuccessResult("成功");
        }
    }

    /**
     * 重置用户密码
     * @param user
     * @return
     */
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @PostMapping(value = "/api/resetnewPassWord")
    public Result resetnewPassWord(@RequestBody User user){
        User u=userService.getByUsername(user.getUsername());
        String username=u.getUsername();
        if(u!=null){
            String password="123";
            String newpassword=ShiroUtil.sha256(password,u.getSalt());
            userService.UpdateUserPassword(newpassword,username);
            return ResultFactory.buildSuccessResult("修改密码成功");
        }else{
            return ResultFactory.buildFailResult("修改失败");
        }
    }
}

