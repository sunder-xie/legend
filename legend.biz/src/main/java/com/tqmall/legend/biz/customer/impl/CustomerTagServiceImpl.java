package com.tqmall.legend.biz.customer.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.cube.shop.RpcCustomerInfoService;
import com.tqmall.cube.shop.result.CustomerInfoDTO;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.customer.CustomerTagService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.customer.CustomerTagDao;
import com.tqmall.legend.entity.customer.CustomerTag;
import com.tqmall.legend.enums.customer.CarLevelTagEnum;
import com.tqmall.legend.enums.customer.CustomCarTagEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/4/12.
 */
@Slf4j
@Service
public class CustomerTagServiceImpl extends BaseServiceImpl implements CustomerTagService{
    @Autowired
    private RpcCustomerInfoService rpcCustomerInfoService;
    @Autowired
    private CustomerTagDao customerTagDao;

    /**
     * 获取客户标签，包括未添加的自定义标签
     *
     * @param shopId
     * @param carId
     * @return
     */
    @Override
    public List<CustomerTag> getCustomerTagByCarId(Long shopId, Long carId) {
        List<CustomerTag> customerTagList = Lists.newArrayList();
        Map<String,Object> tagNameMap = Maps.newHashMap();
        //获取客户标签
        getCustomerTagList(shopId, carId, customerTagList, tagNameMap);
        //获取未添加的自定义标签
        getCustomCarTagList(customerTagList, tagNameMap);
        return customerTagList;
    }

    /**
     * 获取客户标签
     *
     * @param shopId
     * @param carId
     * @param customerTagList
     * @param tagNameMap
     */
    private void getCustomerTagList(Long shopId, Long carId, List<CustomerTag> customerTagList, Map<String, Object> tagNameMap) {
        //TODO 系统打标的标签以后需要迁移至客户标签表
        try {
            com.tqmall.core.common.entity.Result<CustomerInfoDTO> result = rpcCustomerInfoService.getCustomerInfo(shopId, carId);
            if(result.isSuccess()){
                CustomerInfoDTO customerInfoDTO = result.getData();
                Integer carLevelTag = customerInfoDTO.getCarLevelTag();
                String tagName;
                if(CarLevelTagEnum.getMesByCode(carLevelTag) != null){
                    tagName = CarLevelTagEnum.getMesByCode(carLevelTag);
                    CustomerTag customerTag = new CustomerTag();
                    customerTag.setTagName(tagName);
                    customerTag.setTagRefer(0);//系统打标
                    customerTagList.add(customerTag);
                    tagNameMap.put(tagName,null);
                }
                Integer totalNumber = customerInfoDTO.getTotalNumber();
                if(totalNumber > 1){
                    tagName = "老客户";
                }else{
                    tagName = "新客户";
                }
                CustomerTag customerTag = new CustomerTag();
                customerTag.setTagName(tagName);
                customerTag.setTagRefer(0);//系统打标
                customerTagList.add(customerTag);
                tagNameMap.put(tagName,null);
            }
        } catch (Exception e) {
            log.error("【dubbo】:调用cube获取客户统计信息异常",e);
        }
        //查询自定义标签
        Map<String,Object> searchCarTagMap = Maps.newHashMap();
        searchCarTagMap.put("customerCarId",carId);
        searchCarTagMap.put("shopId", shopId);
        searchCarTagMap.put("tagRefer", 1);
        List<CustomerTag> tempTag = customerTagDao.select(searchCarTagMap);
        for(CustomerTag customerTag : tempTag){
            customerTagList.add(customerTag);
            tagNameMap.put(customerTag.getTagName(),null);
        }
    }

    /**
     * 获取客户的标签
     *
     * @param shopId
     * @param carId
     * @return
     */
    @Override
    public List<CustomerTag> getNoCustomCarTagByCarId(Long shopId, Long carId) {
        List<CustomerTag> customerTagList = Lists.newArrayList();
        Map<String,Object> tagNameMap = Maps.newHashMap();
        try {
            getCustomerTagList(shopId, carId, customerTagList, tagNameMap);
        } catch (Exception e) {
            log.error("【dubbo】:调用cube获取客户统计信息异常",e);
        }
        return customerTagList;
    }

    /**
     * 获取未添加的自定义标签
     *
     * @param customerTagList
     * @param tagNameMap
     */
    private void getCustomCarTagList(List<CustomerTag> customerTagList, Map<String, Object> tagNameMap) {
        //添加自定义tag
        for(CustomCarTagEnum customCarTagEnum : CustomCarTagEnum.getMessages()){
            String tagName = customCarTagEnum.getMessage();
            if(!tagNameMap.containsKey(tagName)){
                CustomerTag customerTag = new CustomerTag();
                customerTag.setTagName(tagName);
                customerTag.setTagRefer(1);//自定义标签
                customerTag.setChoose(false);//不存在，不选中
                customerTagList.add(customerTag);
            }
        }
    }

    @Override
    public CustomerTag selectById(Long id) {
        return customerTagDao.selectById(id);
    }

    @Override
    @Transactional
    public Result updateById(CustomerTag customerTag) {
        customerTagDao.updateById(customerTag);
        return Result.wrapSuccessfulResult(customerTag.getId());
    }

    @Override
    @Transactional
    public Result deleteById(Long id) {
        customerTagDao.deleteById(id);
        return Result.wrapSuccessfulResult(id);
    }

    @Override
    @Transactional
    public Result insert(CustomerTag customerTag) {
        customerTagDao.insert(customerTag);
        return Result.wrapSuccessfulResult(customerTag.getId());
    }

    @Override
    public List<String> listTag(Long shopId) {
        return customerTagDao.selectTag(shopId);
    }

    /**
     * 查询门店所有自定义标签
     *
     * @param shopId
     * @return
     */
    @Override
    public List<String> listCustomizeTag(Long shopId) {
        return customerTagDao.listCustomizeTag(shopId);
    }
}
