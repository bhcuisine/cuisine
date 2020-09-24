package com.bh.bhcuisine.controller;

import com.bh.bhcuisine.dao.CastDao;
import com.bh.bhcuisine.dao.MaterialsDao;
import com.bh.bhcuisine.dao.UserDao;
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

@RestController
public class MaterialsController {
    /**
     * 注入材料service层
     */
    @Autowired
    private MaterialsService materialsService;
    /**
     * 注入数据service层
     */
    @Autowired
    private CastService castService;

    /**
     * 注入用户服务service层
     */
    @Autowired
    private UserService userService;

    /**
     * 注入材料dao层
     */
    @Autowired
    private MaterialsDao materialsDao;
    /**
     * 注入数据实体类dao层
     */
    @Autowired
    private CastDao castDao;
    /**
     * 注入用户dao层
     */
    @Autowired
    private UserDao userDao;

    private static Double lastCost = 0.0;



    @ApiOperation(value = "新增材料", notes = "新增材料")
    @PostMapping("/api/materials")
    public Result addMaterials(@RequestBody Materials materials) {
        System.out.println("添加时间是" + materials.getAddTime());
        System.out.println("uid是" + materials.getUid());
        Materials ma = new Materials();
        ma.setUid(materials.getUid());//设置店的uid属于哪个店
        BigDecimal price = new BigDecimal(materials.getPrice());//把价格转换为bigDecimal对象
        BigDecimal quanty = new BigDecimal(materials.getQuanty());//把数量转换为bigDecimal对象
        BigDecimal cast = price.multiply(quanty);
        System.out.println(materials.getPrice());
        System.out.println(cast);
        BigDecimal cast_total = cast.setScale(2, BigDecimal.ROUND_HALF_DOWN);//保留2位小数
        double castTotal = cast_total.doubleValue();
        ma.setMaterialsTotal(castTotal);//保存算出来一次材料成本
        ma.setAddTime(materials.getAddTime());//设置时间
        ma.setMaterialsName(materials.getMaterialsName());//设置材料名
        ma.setCategoryId(materials.getCategoryId());//设置种类id
        ma.setPrice(materials.getPrice());//设置价格
        ma.setQuanty(materials.getQuanty());//设置数量
        long l = System.currentTimeMillis();//获取当前时间
        Date time = new Date(l);
        ma.setUpdateTime(time);//设置更新时间
        ma.setStatus(1);//设置状态位1可用
        materialsService.addMaterials(ma);//保存对象
        lastCost = ma.getMaterialsTotal();//把一次价格材料赋值给静态变量
        String branchName = userDao.findAllById(materials.getUid()).getBranchName();//根据材料uid得到店名
        Cast checkCast = castDao.getAllByRentTimeAndBranchName(materials.getAddTime(), branchName);//根据添加时间和店名得到店哪个月份记录
        if (checkCast == null) {//如果为空，创立新记录
            Cast cast1 = new Cast();
            cast1.setRentTime(materials.getAddTime());
            cast1.setPerformance(1);//绩效率
            cast1.setRentTotal(0.0);//租金
            cast1.setEmployeeTotal(0.0);//人工成本
            cast1.setMonthTotal(0.0);//营业额
            cast1.setCostTotal(lastCost);//原材料成本
            cast1.setPerformanceTotal(0.0);//绩效
            cast1.setProfitTotal(0.0);
            System.out.println(branchName);
            cast1.setBranchName(branchName);//店名
            castDao.save(cast1);
            return ResultFactory.buildSuccessResult("成功");
        } else {
            //如果不为空累加存入价格
            Cast newcast = castDao.getAllByRentTimeAndBranchName(materials.getAddTime(), branchName);//根据时间和店名得到Cast实体类

            lastCost += newcast.getCostTotal();//累加存入
            BigDecimal monthMoney = new BigDecimal(newcast.getMonthTotal());//总金额转为bigDecimal
            BigDecimal costMoney = new BigDecimal(lastCost);//成本转为bigDecimal
            BigDecimal rentMoney = new BigDecimal(newcast.getRentTotal());//租金转为bigDecimal
            BigDecimal employMoney = new BigDecimal(newcast.getEmployeeTotal());//获取人工成本
            BigDecimal profit = monthMoney.subtract(costMoney).subtract(rentMoney).subtract(employMoney);//获取利润值
            double pro = profit.doubleValue();//利润类型转换为double类型
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
    @GetMapping("/api/getMaterials")
    public Result getMaterials(@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                               @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pagesize,
                               @RequestParam(required = false) Integer uid,
                               @RequestParam(required = false) String addTime) {
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pagesize);//封装分页插件
        Page<Materials> materials = materialsDao.getAll(uid, 1, addTime, pageRequest);//返回分页
        return ResultFactory.buildSuccessResult(materials);
    }


    @ApiOperation(value = "修改成本", notes = "修改成本")//材料名称，单价，数量，id
    @PostMapping("/api/updateMaterials")
    public Result updateMaterials(@RequestBody Materials m) {
        long l = System.currentTimeMillis();//获取系统时间
        Date time = new Date(l);//系统时间类型转换为date类型
        BigDecimal price = new BigDecimal(m.getPrice());//把价格转换为bigDecimal对象
        BigDecimal quanty = new BigDecimal(m.getQuanty());//把数量转换为bigDecimal对象
        BigDecimal cast = price.multiply(quanty);//计算一次成本
        BigDecimal cast_total = cast.setScale(2, BigDecimal.ROUND_HALF_DOWN);//保留2位小数
        double castTotal = cast_total.doubleValue();//获取一次材料成本
        //更新原材料需要的前端参数为价格，数量，id，材料名称
        materialsDao.updateMaterials(m.getMaterialsName(), time, castTotal, m.getPrice(), m.getQuanty(), m.getId());//更新原材料
        System.out.println("用户uid是"+m.getUid()+"用户添加时间"+m.getAddTime());
        List<Materials> materials=materialsDao.getMoney(m.getUid(),m.getAddTime());//根据材料uid和添加时间获取材料list集合
        System.out.println(materials.toString());
        double costMoney3=0.0;//定义double类型参数
        BigDecimal cost1=new BigDecimal(costMoney3);//封装成bidDecimal对象
        for (Materials mater:
             materials) {
            BigDecimal cost2=new BigDecimal(mater.getMaterialsTotal());
            cost1=cost1.add(cost2);
        }
        double costMoney2=cost1.doubleValue();
        System.out.println("当前的total"+costMoney2);
        String branchName=userDao.findAllById(m.getUid()).getBranchName();//根据材料uid获取店名
        System.out.println("UID是"+m.getUid()+"店名"+branchName+"添加时间"+m.getAddTime());
        Cast cast1=castDao.getAllByRentTimeAndBranchName(m.getAddTime(),branchName);
        BigDecimal monthMoney = new BigDecimal(cast1.getMonthTotal());//总金额转为bigDecimal
        BigDecimal costMoney = new BigDecimal(costMoney2);//成本转为bigDecimal
        BigDecimal rentMoney = new BigDecimal(cast1.getRentTotal());//租金转为bigDecimal
        BigDecimal employMoney = new BigDecimal(cast1.getEmployeeTotal());//获取人工成本
        BigDecimal profit = monthMoney.subtract(costMoney).subtract(rentMoney).subtract(employMoney);//获取利润值
        double pro = profit.doubleValue();
        try {
            //将绩效率转换为0.00后几位
            Double per = (Double) NumberFormat.getPercentInstance().parse(cast1.getPerformance() + "%");
            //将绩效率转换为bigDecimal
            BigDecimal newPer = new BigDecimal(per);
            //利润乘以绩效率
            BigDecimal newProfit = profit.multiply(newPer);
            //设置保留小数点后2位
            BigDecimal performance_total = newProfit.setScale(2, BigDecimal.ROUND_HALF_DOWN);
            //将最终绩效转换为double保存数据库
            double performanceTotal = performance_total.doubleValue();
            castDao.updateCast2(costMoney2, cast1.getEmployeeTotal(), cast1.getMonthTotal(), cast1.getRentTotal(), pro, performanceTotal, m.getAddTime());
            return ResultFactory.buildSuccessResult("修改成功");
        }
        catch (RuntimeException | ParseException e){
            e.printStackTrace();
        }
        return ResultFactory.buildSuccessResult("失败");
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
     * 根据id得到回显数据
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id得到材料实体", notes = "根据id得到材料实体")
    @GetMapping("/api/getMaterials2")
    public Result getMaterials(@RequestParam Integer id) {
        System.out.println(id);
        Materials materials=materialsDao.findAllById(id);
        return ResultFactory.buildSuccessResult(materials);
    }

    /**
     * 删除材料时候更新  需要uid id 添加时间
     */
    @ApiOperation(value = "删除材料", notes = "删除材料")
    @PostMapping("/api/deleteMaterials")
    public Result deleteMaterials(@RequestBody Materials materials) {
        System.out.println("获取删除uid"+materials.getUid()+"获取删除addTime"+materials.getAddTime()+"获取删除id"+materials.getId());
        int status = 0;
        String branchName = userDao.findAllById(materials.getUid()).getBranchName();//根据材料uid得到店名
        Cast cast = castDao.getAllByRentTimeAndBranchName(materials.getAddTime(), branchName);//根据店名和时间得到具体月份数据实体
        materialsDao.updateById(status, materials.getId());//更新成本状态位
        List<Materials> m=materialsDao.getMoney(materials.getUid(),materials.getAddTime());
        double costMoney3=0.0;
        BigDecimal cost1=new BigDecimal(costMoney3);
        for (Materials mater:
                m) {
            BigDecimal cost2=new BigDecimal(mater.getMaterialsTotal());
            cost1=cost1.add(cost2);
        }
        double costMoney2=cost1.doubleValue();//将bigDecimal对象转换为double对象
        System.out.println("当前的total是"+costMoney2);
        BigDecimal monthMoney = new BigDecimal(cast.getMonthTotal());//总金额转为bigDecimal
        BigDecimal costMoney = new BigDecimal(costMoney2);//成本转为bigDecimal
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
            castDao.updateCast2(costMoney2, cast.getEmployeeTotal(), cast.getMonthTotal(), cast.getRentTotal(), pro, performanceTotal, materials.getAddTime());
            return ResultFactory.buildSuccessResult("修改成功");
        } catch (RuntimeException | ParseException e) {
            e.printStackTrace();
        }
        return ResultFactory.buildSuccessResult("失败");
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
        double costTotal = castService.findAllById(c.getId()).getCostTotal();//得到原材料成本
        System.out.println("当前的金额" + costTotal);
        BigDecimal monthMoney = new BigDecimal(c.getMonthTotal());//总金额转为bigDecimal
        BigDecimal costMoney = new BigDecimal(costTotal);//成本转为bigDecimal
        BigDecimal rentMoney = new BigDecimal(c.getRentTotal());//租金转为bigDecimal
        BigDecimal employMoney = new BigDecimal(c.getEmployeeTotal());//获取人工成本
        BigDecimal profit = monthMoney.subtract(costMoney).subtract(rentMoney).subtract(employMoney);
        double pro = profit.doubleValue();
        System.out.println(pro);
        try {
            Double per = (Double) NumberFormat.getPercentInstance().parse(performance + "%");//将绩效率转换为0.00后几位
            BigDecimal newPer = new BigDecimal(per); //将绩效率转换为bigDecimal
            BigDecimal newProfit = profit.multiply(newPer);//利润乘以绩效率
            BigDecimal performance_total = newProfit.setScale(2, BigDecimal.ROUND_HALF_DOWN);//设置保留小数点后2位
            //将最终绩效转换为double保存数据库
            double performanceTotal = performance_total.doubleValue();
            castDao.updateCast(costTotal, c.getEmployeeTotal(), c.getMonthTotal(), c.getRentTotal(), pro, performanceTotal, c.getId());//修改数据
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
//        User user=(User) SecurityUtils.getSubject().getSession().getAttribute("user");
//        String username=user.getUsername();
//        String branchName=userDao.getByUsername(username).getBranchName();
        System.out.println("店名"+c.getBranchName());
        System.out.println("店名时间"+c.getRentTime());
        Cast cast1 = castDao.getAllByRentTimeAndBranchName(c.getRentTime(),c.getBranchName());
        if (cast1 == null) {//
            Cast cast = new Cast();
            cast.setBranchName(c.getBranchName());//店名
            cast.setPerformance(1);//插入默认绩效率是1
            cast.setMonthTotal(c.getMonthTotal());//总金额
            cast.setRentTotal(c.getRentTotal());
            cast.setEmployeeTotal(c.getEmployeeTotal());//人工成本
            double cost = 0.0;
            cast.setCostTotal(cost);//成本
            cast.setRentTime(c.getRentTime());//
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
        } else {
            return ResultFactory.buildFailResult("已经存在");
        }
        return ResultFactory.buildFailResult("失败");
    }

}
