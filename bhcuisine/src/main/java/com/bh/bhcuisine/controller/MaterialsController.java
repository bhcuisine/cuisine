package com.bh.bhcuisine.controller;

import com.bh.bhcuisine.dao.CastDao;
import com.bh.bhcuisine.dao.MaterialsDao;
import com.bh.bhcuisine.dto.DataDto;
import com.bh.bhcuisine.entity.Cast;
import com.bh.bhcuisine.entity.Materials;
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

    private static double total = 0.0;

    private static double cost = 0.0;

    private static double cost2 = 0.0;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/api/materials")
    public Result addMaterials(@RequestBody DataDto dataDto) {
        Materials materials = new Materials();
        System.out.println(dataDto.getCategoryId());
        materials.setUid(dataDto.getUid());
        BigDecimal price = new BigDecimal(dataDto.getPrice());
        BigDecimal quanty = new BigDecimal(dataDto.getQuanty());
        BigDecimal cast = price.multiply(quanty);
        BigDecimal cast_total = cast.setScale(2, BigDecimal.ROUND_HALF_DOWN);
        double castTotal = cast_total.doubleValue();
        materials.setMaterialsTotal(castTotal);
        materials.setAddTime(dataDto.getAddTime());
        materials.setMaterialsName(dataDto.getMaterialsName());
        materials.setCategoryId(dataDto.getCategoryId());
        materials.setPrice(dataDto.getPrice());
        materials.setQuanty(dataDto.getQuanty());
        long l = System.currentTimeMillis();
        Date time = new Date(l);
        materials.setUpdateTime(time);
        materialsService.addMaterials(materials);
        total += castTotal;
        return ResultFactory.buildSuccessResult("成功");
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
                               @RequestParam(required = false) Integer id) {
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pagesize);
        Page<Materials> materials = materialsDao.getAll(id, pageRequest);
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
        List<Materials> materialsList = materialsDao.findAllByUidAndAddTime(uid, addTime);
        if (materialsList != null) {
            for (Materials m :
                    materialsList) {
                cost2 += m.getMaterialsTotal();
                System.out.println("计算成本" + cost2);
            }
            Cast c = castService.findAllByRentTime(addTime);
            if (c != null) {
                System.out.println(c.toString());
                BigDecimal monthMoney = new BigDecimal(c.getMonthTotal());//总金额转为bigDecimal
                BigDecimal costMoney = new BigDecimal(cost2);//成本转为bigDecimal
                BigDecimal rentMoney = new BigDecimal(c.getRentTotal());//租金转为bigDecimal
                BigDecimal employMoney = new BigDecimal(c.getEmployeeTotal());//获取人工成本
                BigDecimal profit = monthMoney.subtract(costMoney).subtract(rentMoney).subtract(employMoney);
                double pro = profit.doubleValue();
                System.out.println(pro);
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
                    castDao.updateCast(cost2, c.getEmployeeTotal(), c.getMonthTotal(), c.getRentTotal(), pro, performanceTotal, c.getId());
                    PageRequest pageRequest = PageRequest.of(currentPage - 1, pagesize);
                    Page<Cast> cast = castService.findAllByRAndBranchNameAndRenTime(addTime, branchName, username, pageRequest);
                    cost2 = 0.0;
                    return ResultFactory.buildSuccessResult(cast);
                } catch (RuntimeException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResultFactory.buildFailResult("失败");
    }

    /**
     * 删除材料
     */
    @ApiOperation(value = "删除材料", notes = "删除材料")
    @PostMapping("/api/deleteMaterials")
    public Result deleteMaterials(@RequestParam Integer id) {
        materialsDao.deleteById(id);
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
    public Result addCast2(@RequestBody Cast c,
                           @RequestParam String addTime,
                           @RequestParam Integer uid
    ) {
        System.out.println("获取的id是" + c.getId());
        List<Materials> materialsList = materialsDao.findAllByUidAndAddTime(uid, addTime);
        for (Materials m :
                materialsList) {
            cost += m.getMaterialsTotal();
        }
        Integer performance = castService.findAllById(c.getId()).getPerformance();
//        Double costTotal = castService.findAllById(c.getId()).getCostTotal();//覆盖数据改成不查而是计算第二次累加的值
        Double costTotal = cost;
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
        System.out.println(c.getRentTime());
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
            cost = total;
            cast.setCostTotal(cost);//成本
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
//            System.out.println("最终绩效"+performanceTotal);
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
