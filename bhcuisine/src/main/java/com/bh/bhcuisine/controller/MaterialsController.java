package com.bh.bhcuisine.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.NumberFormat;
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

    private static double total=0.0;

    @ApiOperation(value = "新增" ,  notes="新增")
    @PostMapping("/api/materials")
    public Result addMaterials(@RequestBody Materials materials){
        User user=(User)SecurityUtils.getSubject().getSession().getAttribute("user");
        Integer id=user.getId();
        materials.setUid(id);
        BigDecimal price = new BigDecimal(materials.getPrice());
        BigDecimal quanty = new BigDecimal(materials.getQuanty());
        BigDecimal cast = price.multiply(quanty);
        BigDecimal cast_total = cast.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        cast_total=cast_total.add(cast_total);
        double castTotal=cast_total.doubleValue();
        total=castTotal;
        materials.setAddTime(materials.getAddTime());
        materials.setMaterialsName(materials.getMaterialsName());
        materials.setCategoryId(materials.getCategoryId());
        materials.setPrice(materials.getPrice());
        materials.setQuanty(materials.getQuanty());
        long l = System.currentTimeMillis();
        Date time=new Date(l);
        materials.setUpdateTime(time);
        materialsService.addMaterials(materials);
        return  ResultFactory.buildSuccessResult("成功");
    }
    /**
     * 插入数据
     * @param username 用户名
     * @param monthTotal 总营业额
     * @param rentTime 租的月份
     * @param employeeTotal 人工成本
     * @param rentTotal 房租
     * @return
     */
    @ApiOperation(value = "不勾选具体月份插入数据单纯插入数据" ,  notes="插入数据")
    @PostMapping("/api/addCast")
    public Result addCast(
//    @RequestParam String username,
                          @RequestParam Integer monthTotal,
                          @RequestParam String rentTime,
                          @RequestParam Integer employeeTotal,
                          @RequestParam Integer rentTotal){
        User user=(User)SecurityUtils.getSubject().getSession().getAttribute("user");
        String username=user.getUsername();
        String branchName=userService.getByUsername(username).getBranchName();
        //Integer performance=castService.findAllByBranchName(branchName).getPerformance();
        Cast cast=new Cast();
        cast.setBranchName(branchName);//店名
        cast.setPerformance(3);
        cast.setMonthTotal(monthTotal);//总金额
        cast.setRentTotal(rentTotal);
        cast.setEmployeeTotal(employeeTotal);//人工成本
        Double costTotal=0.0;
        try {
            if(castService.findAllByRentTime(rentTime).getCostTotal()==null){
                costTotal=0.0;
            }
        }catch (Exception e){

        }
        costTotal=total;
        cast.setCostTotal(costTotal);//成本
        BigDecimal monthMoney=new BigDecimal(monthTotal);//总金额转为bigDecimal
        BigDecimal costMoney=new BigDecimal(costTotal);//成本转为bigDecimal
        BigDecimal rentMoney=new BigDecimal(rentTotal);//租金转为bigDecimal
        BigDecimal employMoney=new BigDecimal(employeeTotal);
        BigDecimal profit=monthMoney.subtract(costMoney).subtract(rentMoney).subtract(employMoney);
        double pro=profit.doubleValue();
        cast.setProfitTotal(pro);//利润
        cast.setRentTime(rentTime);//当前月份
        //得到int型绩效率
        try{
            //将绩效率转换为0.00后几位
            Double per = (Double) NumberFormat.getPercentInstance().parse(cast.getPerformance()+"%");
            //将绩效率转换为bigDecimal
            BigDecimal newPer =new BigDecimal(per);
            //利润乘以绩效率
            BigDecimal newProfit=profit.multiply(newPer);
            //设置保留小数点后2位
            BigDecimal performance_total = newProfit.setScale(2,BigDecimal.ROUND_HALF_DOWN);
            //将最终绩效转换为double保存数据库
            double performanceTotal=performance_total.doubleValue();
//            System.out.println("最终绩效"+performanceTotal);
            cast.setPerformanceTotal(performanceTotal);
            castService.addCast(cast);
            return ResultFactory.buildSuccessResult(performanceTotal);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultFactory.buildFailResult("失败");
    }


}
