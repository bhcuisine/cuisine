package com.bh.bhcuisine.controller;

import com.bh.bhcuisine.entity.Materials;
import com.bh.bhcuisine.entity.User;
import com.bh.bhcuisine.service.MaterialsService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class MaterialsController {
    @Autowired
    private MaterialsService materialsService;

    private static double total=0.0;

    @ApiOperation(value = "新增" ,  notes="新增")
    @PostMapping("/api/materials")
    public Materials addMaterials(@RequestBody Materials materials){
        materialsService.addMaterials(materials);
        User user=(User)SecurityUtils.getSubject().getSession().getAttribute("user");
        Integer id=user.getId();
        materials.setUid(id);
//        System.out.println(materials.getPrice());
//        System.out.println(materials.getQuanty());
        BigDecimal price = new BigDecimal(materials.getPrice());
        BigDecimal quanty = new BigDecimal(materials.getQuanty());
        BigDecimal cast = price.multiply(quanty);
        BigDecimal cast_total = cast.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        double castTotal=cast_total.doubleValue();
         total+=castTotal;

//        System.out.println(cast);
        return materials;
    }
    public void get(){
        System.out.println(total);
    }

}
