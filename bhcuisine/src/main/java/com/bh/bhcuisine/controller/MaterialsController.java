package com.bh.bhcuisine.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

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

    private static double total = 0.0;

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
