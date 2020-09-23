package com.bh.bhcuisine.controller;

import com.bh.bhcuisine.dao.CastDao;
import com.bh.bhcuisine.dao.MaterialsDao;
import com.bh.bhcuisine.dao.UserDao;
import com.bh.bhcuisine.dto.CastDto;
import com.bh.bhcuisine.dto.DataDto;
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
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@RestController
public class MaterialsController {
    @Autowired
    private MaterialsService materialsService;
    @Autowired
    private CastService castService;

    /**
     * 注入用户服务service层
     */
    @Autowired
    private UserService userService;

    @Autowired
    private MaterialsDao materialsDao;

    @Autowired
    private CastDao castDao;

    @Autowired
    private UserDao userDao;

    private static double total = 0.0;

    private static BigDecimal newCost;

    private static Double cost = 0.0;

    private static BigDecimal cost4;

    private static Double lastCost = 0.0;


    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/api/materials")
    public Result addMaterials(@RequestBody Materials materials) {
        System.out.println("添加时间是" + materials.getAddTime());
        System.out.println("uid是" + materials.getUid());
        Materials ma = new Materials();
        ma.setUid(materials.getUid());
        BigDecimal price = new BigDecimal(materials.getPrice());
        BigDecimal quanty = new BigDecimal(materials.getQuanty());
        BigDecimal cast = price.multiply(quanty);
        System.out.println(materials.getPrice());
        System.out.println(cast);
        BigDecimal cast_total = cast.setScale(2, BigDecimal.ROUND_HALF_DOWN);
        double castTotal = cast_total.doubleValue();
        ma.setMaterialsTotal(castTotal);
        ma.setAddTime(materials.getAddTime());
        ma.setMaterialsName(materials.getMaterialsName());
        ma.setCategoryId(materials.getCategoryId());
        ma.setPrice(materials.getPrice());
        ma.setQuanty(materials.getQuanty());
        long l = System.currentTimeMillis();
        Date time = new Date(l);
        ma.setUpdateTime(time);
        ma.setStatus(0);
        materialsService.addMaterials(ma);
        lastCost = ma.getMaterialsTotal();
        String branchName = userDao.findAllById(materials.getUid()).getBranchName();
        Cast checkCast = castDao.getAllByRentTimeAndBranchName(materials.getAddTime(), branchName);
        if (checkCast == null) {
            Cast cast1 = new Cast();
            cast1.setRentTime(materials.getAddTime());
            cast1.setPerformance(1);//绩效率
            cast1.setRentTotal(0);//租金
            cast1.setEmployeeTotal(0);//人工成本
            cast1.setMonthTotal(0);//营业额
            cast1.setCostTotal(lastCost);//原材料成本
            cast1.setPerformanceTotal(0.0);//绩效
            cast1.setProfitTotal(0.0);
            System.out.println(branchName);
            cast1.setBranchName(branchName);//店名
            castDao.save(cast1);
            return ResultFactory.buildSuccessResult("成功");
        } else {
            Cast newcast = castDao.getAllByRentTimeAndBranchName(materials.getAddTime(), branchName);//根据时间和店名得到Cast实体类
            lastCost += newcast.getCostTotal();//累加存入
            BigDecimal monthMoney = new BigDecimal(newcast.getMonthTotal());//总金额转为bigDecimal
            BigDecimal costMoney = new BigDecimal(lastCost);//成本转为bigDecimal
            BigDecimal rentMoney = new BigDecimal(newcast.getRentTotal());//租金转为bigDecimal
            BigDecimal employMoney = new BigDecimal(newcast.getEmployeeTotal());//获取人工成本
            BigDecimal profit = monthMoney.subtract(costMoney).subtract(rentMoney).subtract(employMoney);//获取利润值
            double pro = profit.doubleValue();
            try {
                //将绩效率转换为0.00后几位
                Double per = (Double) NumberFormat.getPercentInstance().parse(newcast.getPerformance() + "%");
                //将绩效率转换为bigDecimal
                BigDecimal newPer = new BigDecimal(per);
                //利润乘以绩效率
                BigDecimal newProfit = profit.multiply(newPer);
                //设置保留小数点后2位
                BigDecimal performance_total = newProfit.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                //将最终绩效转换为double保存数据库
                double performanceTotal = performance_total.doubleValue();
                System.out.println("当前" + lastCost);
                System.out.println("利润值" + pro);
                castDao.updateCast2(lastCost, newcast.getEmployeeTotal(), newcast.getMonthTotal(), newcast.getRentTotal(), pro, performanceTotal, materials.getAddTime());
                return ResultFactory.buildSuccessResult("修改成功");
            } catch (RuntimeException | ParseException e) {
                e.printStackTrace();
            }
        }
        lastCost = 0.0;
        return ResultFactory.buildFailResult("失败");
    }

    /**
     * 根据店查找材料
     *
     * @param m
     * @return
     */
    @ApiOperation(value = "查找材料", notes = "查找材料")
    @PostMapping("/api/getMaterials")
    public Result getMaterials(@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                               @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pagesize,
                               @RequestParam(required = false) Integer uid,
                               @RequestParam(required = false) String addTime) {
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pagesize);
        Page<Materials> materials = materialsDao.getAll(uid, 1, addTime, pageRequest);
        return ResultFactory.buildSuccessResult(materials);
    }


    @ApiOperation(value = "修改成本", notes = "修改成本")//材料名称，单价，数量，id
    @PostMapping("/api/updateMaterials")
    public Result updateMaterials(@RequestBody Materials m) {
        long l = System.currentTimeMillis();
        Date time = new Date(l);
        BigDecimal price = new BigDecimal(m.getPrice());
        BigDecimal quanty = new BigDecimal(m.getQuanty());
        BigDecimal cast = price.multiply(quanty);
        BigDecimal cast_total = cast.setScale(2, BigDecimal.ROUND_HALF_DOWN);
        double castTotal = cast_total.doubleValue();
        materialsDao.updateMaterials(m.getMaterialsName(), time, castTotal, m.getPrice(), m.getQuanty(), m.getId());
        return ResultFactory.buildSuccessResult("成功");
    }

    /**
     * --注释掉username参数改用shiroSession获取用户名
     * 根据前端自主调整传入对象是requestBody还是requestParam
     * 按条件查询：时间、店名
     *
     * @param currentPage 当前页
     * @param pagesize    每页数量
     * @param branchName  店名
     * @param addTime     添加时间
     * @return
     */
    @ApiOperation(value = "用户查询", notes = "用户查询")
    @GetMapping(value = "/api/search")
    public Result getSearchData(@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pagesize,
                                @RequestParam(required = false) String username,
//                                @RequestBody User user,
                                @RequestParam(required = false) String branchName,
                                @RequestParam(required = false) String addTime,
                                @RequestParam(required = false) Integer uid) {
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pagesize);
        Page<Cast> cast = castService.findAllByRAndBranchNameAndRenTime(addTime, branchName, username, pageRequest);
        return ResultFactory.buildSuccessResult(cast);
    }

    /**
     * 删除材料时候更新
     */
    @ApiOperation(value = "删除材料", notes = "删除材料")
    @PostMapping("/api/deleteMaterials")
    public Result deleteMaterials(@RequestBody Materials materials) {
        Double get=0.0;
        List<Double> list=new ArrayList();
        int status = 0;
        String branchName = userDao.findAllById(materials.getUid()).getBranchName();
        Cast cast = castDao.getAllByRentTimeAndBranchName(materials.getAddTime(), branchName);
        Double money=cast.getCostTotal();
        materialsDao.updateById(status, materials.getId());
        List<Materials> materials1=materialsDao.findAllByUidAndAddTime(materials.getUid(),materials.getAddTime());
            for (Materials m:
                    materials1) {
                if(m.getStatus()==0){
                     get=materials.getMaterialsTotal();
                     list.add(get);
                }
            }
        for (Double d:
             list) {
            money-=d;
        }
        BigDecimal monthMoney = new BigDecimal(cast.getMonthTotal());//总金额转为bigDecimal
        BigDecimal costMoney = new BigDecimal(money);//成本转为bigDecimal
        BigDecimal rentMoney = new BigDecimal(cast.getRentTotal());//租金转为bigDecimal
        BigDecimal employMoney = new BigDecimal(cast.getEmployeeTotal());//获取人工成本
        BigDecimal profit = monthMoney.subtract(costMoney).subtract(rentMoney).subtract(employMoney);//获取利润值
        double pro = profit.doubleValue();
        try {
            //将绩效率转换为0.00后几位
            Double per = (Double) NumberFormat.getPercentInstance().parse(cast.getPerformance() + "%");
            //将绩效率转换为bigDecimal
            BigDecimal newPer = new BigDecimal(per);
            //利润乘以绩效率
            BigDecimal newProfit = profit.multiply(newPer);
            //设置保留小数点后2位
            BigDecimal performance_total = newProfit.setScale(2, BigDecimal.ROUND_HALF_DOWN);
            //将最终绩效转换为double保存数据库
            double performanceTotal = performance_total.doubleValue();
            System.out.println("当前" + lastCost);
            System.out.println("利润值" + pro);
            castDao.updateCast2(money, cast.getEmployeeTotal(), cast.getMonthTotal(), cast.getRentTotal(), pro, performanceTotal, materials.getAddTime());
            return ResultFactory.buildSuccessResult("修改成功");
        } catch (RuntimeException | ParseException e) {
            e.printStackTrace();
        }
        return ResultFactory.buildSuccessResult("删除成功");
    }

    /**
     * 插入数据
     *
     * @param username
     * @param monthTotal
     * @param employeeTotal
     * @param rentTotal
     * @param id
     * @return
     */
    @ApiOperation(value = "勾选插入已存在月份数据也就是修改数据", notes = "插入数据也就是修改数据")
    @PostMapping("/api/addCast2")
    public Result addCast2(
            @RequestBody Cast c
    ) {
        Integer performance = castService.findAllById(c.getId()).getPerformance();
//        Double costTotal = castService.findAllById(c.getId()).getCostTotal();//覆盖数据改成不查而是计算第二次累加的值
        Double costTotal = castService.findAllById(c.getId()).getCostTotal();//得到原材料成本
        System.out.println("当前的金额" + costTotal);
        BigDecimal monthMoney = new BigDecimal(c.getMonthTotal());//总金额转为bigDecimal
        BigDecimal costMoney = new BigDecimal(costTotal);//成本转为bigDecimal
        BigDecimal rentMoney = new BigDecimal(c.getRentTotal());//租金转为bigDecimal
        BigDecimal employMoney = new BigDecimal(c.getEmployeeTotal());//获取人工成本
        BigDecimal profit = monthMoney.subtract(costMoney).subtract(rentMoney).subtract(employMoney);
        double pro = profit.doubleValue();
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
            castDao.updateCast(costTotal, c.getEmployeeTotal(), c.getMonthTotal(), c.getRentTotal(), pro, performanceTotal, c.getId());
            return ResultFactory.buildSuccessResult("成功");
        } catch (RuntimeException | ParseException e) {
            e.printStackTrace();
        }
        return ResultFactory.buildFailResult("失败");
    }

    /**
     * 插入数据
     *
     * @param username      用户名
     * @param monthTotal    总营业额
     * @param rentTime      租的月份
     * @param employeeTotal 人工成本
     * @param rentTotal     房租
     * @return
     */
    @ApiOperation(value = "不勾选具体月份插入数据单纯插入数据", notes = "插入数据")
    @PostMapping("/api/addCast")
    public Result addCast(
            @RequestBody Cast c) {
        System.out.println("插入时间为" + c.getRentTime());
        Cast cast1 = castService.findAllByRentTime(c.getRentTime());
        if (cast1 != null) {
            return ResultFactory.buildFailResult("已经存在");
        } else {
            Cast cast = new Cast();
            cast.setBranchName(c.getBranchName());//店名
            cast.setPerformance(1);//插入默认绩效率是1
            cast.setMonthTotal(c.getMonthTotal());//总金额
            cast.setRentTotal(c.getRentTotal());
            cast.setEmployeeTotal(c.getEmployeeTotal());//人工成本
            Double cost = 0.0;
            cost = lastCost;
            cast.setCostTotal(cost);//成本
            cast.setRentTime(c.getRentTime());
            BigDecimal monthMoney = new BigDecimal(c.getMonthTotal());//总金额转为bigDecimal
            BigDecimal costMoney = new BigDecimal(cost);//成本转为bigDecimal
            BigDecimal rentMoney = new BigDecimal(c.getRentTotal());//租金转为bigDecimal
            BigDecimal employMoney = new BigDecimal(c.getEmployeeTotal());
            BigDecimal profit = monthMoney.subtract(costMoney).subtract(rentMoney).subtract(employMoney);
            double pro = profit.doubleValue();
            cast.setProfitTotal(pro);//利润
            cast.setRentTime(c.getRentTime());//当前月份
            //得到int型绩效率
            try {
                //将绩效率转换为0.00后几位
                Double per = (Double) NumberFormat.getPercentInstance().parse(cast.getPerformance() + "%");
                //将绩效率转换为bigDecimal
                BigDecimal newPer = new BigDecimal(per);
                //利润乘以绩效率
                BigDecimal newProfit = profit.multiply(newPer);
                //设置保留小数点后2位
                BigDecimal performance_total = newProfit.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                //将最终绩效转换为double保存数据库
                double performanceTotal = performance_total.doubleValue();
                cast.setPerformanceTotal(performanceTotal);
                castService.addCast(cast);
                return ResultFactory.buildSuccessResult(performanceTotal);
            } catch (RuntimeException | ParseException e) {
                e.printStackTrace();
            }
        }
        return ResultFactory.buildFailResult("失败");
    }

}
