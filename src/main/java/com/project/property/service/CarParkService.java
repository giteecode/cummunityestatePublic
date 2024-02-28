package com.project.property.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.property.dao.CarParkMapper;
import com.project.property.dao.PropertyPayVisitMapper;
import com.project.property.entity.CarPark;
import com.project.property.entity.CarParkCharge;
import com.project.property.entity.PropertyPayVisit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class CarParkService {

    @Resource
    private CarParkMapper carParkMapper;
    @Resource
    private PropertyPayVisitMapper payVisitMapper;


    public int deleteByPrimaryKey(String id) {
        // 删除前查询是否有售出的
        List<CarPark> carParks = carParkMapper.selectInfoByIds(id);
        for (CarPark carPark : carParks) {
            if ("1".equals(carPark.getParkState())) {
                return -500;
            }
        }
        return carParkMapper.deleteByPrimaryKey(id);
    }


    public int insert(CarPark record) {
        return carParkMapper.insert(record);
    }


    public int insertSelective(CarPark record) {
        return carParkMapper.insertSelective(record);
    }


    public CarPark selectByPrimaryKey(Integer id) {
        return carParkMapper.selectByPrimaryKey(id);
    }


    public int updateByPrimaryKeySelective(CarPark record) {
        return carParkMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(CarPark record) {
        return carParkMapper.updateByPrimaryKey(record);
    }

    /**
     * 分页查询数据
     *
     * @param carPark 查询条件
     * @param page    当前页
     * @param limit   每页显示的数据量
     * @return
     */
    public List<CarPark> selectDataByPage(CarPark carPark, Integer page, Integer limit) {
        // 开启分页插件
        PageHelper.startPage(page, limit);
        // 查询数据
        PageInfo<CarPark> pageInfo = new PageInfo<CarPark>(carParkMapper.selectDataByParam(carPark));
        // 返回分页查询后的数据
        return pageInfo.getList();
    }

    /**
     * 查询数据总数
     *
     * @param carPark 查询条件
     * @return
     */
    public Integer selectDataCount(CarPark carPark) {
        return carParkMapper.selectDataCount(carPark);
    }

    /**
     * 增加车位收费信息
     *
     * @param record
     * @return
     */
    public int insertCharge(CarParkCharge record) {
        return carParkMapper.insertCarParkCharge(record);
    }

    /**
     * 分页查询数据（车位收费信息）
     *
     * @param entity 查询条件
     * @param page   当前页
     * @param limit  每页显示的数据量
     * @return
     */
    public List<CarParkCharge> selectChargeDataByPage(CarParkCharge entity, Integer page, Integer limit) {
        // 开启分页插件
        PageHelper.startPage(page, limit);
        // 查询数据
        PageInfo<CarParkCharge> pageInfo = new PageInfo<CarParkCharge>(carParkMapper.selectCarParkCharge(entity));
        // 返回分页查询后的数据
        return pageInfo.getList();
    }

    /**
     * 查询数据总数
     *
     * @param carPark 查询条件
     * @return
     */
    public Integer selectDataCount2(CarParkCharge carPark) {
        return carParkMapper.selectCarParkDataCount(carPark);
    }

    /**
     * 统计分析
     *
     * @return
     */
    public Map statisticAnalysis() {
        // 获取要分析的数据
        List<CarParkCharge> list = carParkMapper.selectCarParkCharge(new CarParkCharge());
        List<PropertyPayVisit> list2 = payVisitMapper.selectDataByParam(new PropertyPayVisit());

        // 按【车位类型-月度】对产生的费用统计，日期格式为：yyyy-MM
        Map<String, Map<String, BigDecimal>> count1 = list.stream()
                .filter(o -> o.getChargeDate() != null && o.getFee() != null)  // 过滤日期和费用为空的数据
                .collect(Collectors.groupingBy(o -> DateUtil.format(o.getChargeDate(), "yyyy-MM"), Collectors.collectingAndThen(Collectors.toList(),
                        maps -> maps.stream().collect(Collectors.groupingBy(o -> o.getCarParkInfo().getCarParkType(), Collectors.collectingAndThen(Collectors.toList(),
                                maps2 -> maps2.stream().map(CarParkCharge::getFee).reduce(BigDecimal.ZERO, BigDecimal::add)))))));
        // 车位费相关指标计算
        Map<String, Object> map = new HashMap<>();
        map.put("timeX", count1.keySet());
        map.put("costY1", count1.values().stream().map(o -> o.computeIfAbsent("地下停车位", f -> BigDecimal.ZERO)).collect(Collectors.toList()));
        map.put("costY2", count1.values().stream().map(o -> o.computeIfAbsent("地面停车位", f -> BigDecimal.ZERO)).collect(Collectors.toList()));
        map.put("costAvg", count1.values().stream().map(o -> o.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(2))).collect(Collectors.toList()));


        // 二、统计物业费
        Map<String, Integer> count2 = list2.stream()
                .filter(o -> StrUtil.isNotBlank(o.getPayDate()) && o.getPayMoney() != null)  // 过滤日期和费用为空的数据
                .collect(Collectors.groupingBy(o -> DateUtil.format(DateUtil.parse(o.getPayDate()), "yyyy-MM"), Collectors.collectingAndThen(Collectors.toList(),
                        maps -> maps.stream().map(PropertyPayVisit::getPayMoney).mapToInt(Integer::intValue).sum())));
        // 物业费相关指标计算
        Map<String, Object> map2 = new HashMap<>();
        map2.put("timeX", count2.keySet());
        map2.put("costY1", count2.values());

        // 统计结果封装
        Map<String, Object> result = new HashMap<>();
        result.put("chart1", map);
        result.put("chart2", map2);

        return result;
    }
}
