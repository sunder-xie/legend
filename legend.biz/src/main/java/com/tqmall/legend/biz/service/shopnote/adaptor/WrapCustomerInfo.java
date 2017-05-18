package com.tqmall.legend.biz.service.shopnote.adaptor;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import com.tqmall.legend.entity.shop.NoteInfo;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yuchengdu on 16/7/29.
 */
public class WrapCustomerInfo {
    /**
     * 以customerId为维度剔除sourceCustomerInfo列表中的重复项,如果有重复的以最近创建的为准
     *
     * @param sourceCustomerInfo 数据源
     * @return 剔除重复项后的数据
     */
    public static List<CustomerInfo> distinctCustomerInfoByCustomerId(List<CustomerInfo> sourceCustomerInfo){
        if (CollectionUtils.isEmpty(sourceCustomerInfo)){
            return Collections.emptyList();
        }
        //对原始数据按照customerId进行分组
        Multimap<Long, CustomerInfo> sourceCustomerInfoMapList = Multimaps.index(sourceCustomerInfo, new Function<CustomerInfo, Long>() {
            @Override
            public Long apply(CustomerInfo input) {
                return input.getCustomerId();
            }
        });
        return warpCustomerInfoFromMultimap(sourceCustomerInfoMapList);
    }
    /**
     * 以customerCarId为维度剔除sourceCustomerInfo列表中的重复项,如果有重复的以最近创建的为准
     *
     * @param sourceCustomerInfo 数据源
     * @return 剔除重复项后的数据
     */
    public static List<CustomerInfo> distinctCustomerInfoByCustomerCarId(List<CustomerInfo> sourceCustomerInfo) {
        if (CollectionUtils.isEmpty(sourceCustomerInfo)){
            return Collections.emptyList();
        }
        //对原始数据按照customerId进行分组
        Multimap<Long, CustomerInfo> sourceCustomerInfoMapList = Multimaps.index(sourceCustomerInfo, new Function<CustomerInfo, Long>() {
            @Override
            public Long apply(CustomerInfo input) {
                return input.getCustomerCarId();
            }
        });
        return warpCustomerInfoFromMultimap(sourceCustomerInfoMapList);
    }

    /**
     * 封装Multimap(基准数据为CustomerInfo)数据如果有重复项只取ID最大的值
     * @param sourceCustomerInfoMapList
     * @return
     */
    private static List<CustomerInfo> warpCustomerInfoFromMultimap(Multimap<Long, CustomerInfo> sourceCustomerInfoMapList){
        if (sourceCustomerInfoMapList.isEmpty()){
            return Collections.emptyList();
        }
        //目标数据链表
        List<CustomerInfo> desCustomerCarInfo = Lists.newArrayList();
        for (Long mapKey : sourceCustomerInfoMapList.keySet()) {
            Collection<CustomerInfo> customerInfoCollection = sourceCustomerInfoMapList.get(mapKey);
            if (customerInfoCollection != null && customerInfoCollection.size() >= 1) {
                List<CustomerInfo> keyMapCustomerCarList = Lists.newArrayList(customerInfoCollection);
                Collections.sort(keyMapCustomerCarList, new Comparator<CustomerInfo>() {
                    @Override
                    public int compare(CustomerInfo o1, CustomerInfo o2) {
                        return o2.getId().compareTo(o1.getId());
                    }
                });
                desCustomerCarInfo.add(keyMapCustomerCarList.get(0));
            }
        }
        return desCustomerCarInfo;
    }
    /**
     * 剔除CustomerInfoList中已经产生了消息提醒的数据
     * @param noteInfoList
     * @param distinctCustomerInfo
     * @param byCarId 是否依据Car进行重复数据剔除
     * @return
     */
    public static List<CustomerInfo> distinctCustomerInfoByNoteInfo(List<NoteInfo> noteInfoList,List<CustomerInfo> distinctCustomerInfo,final boolean byCarId){
        List<CustomerInfo> waitingCreateNoteCustomer;
        if (!CollectionUtils.isEmpty(noteInfoList)) {
            final List<Long> existsRelIds = Lists.transform(noteInfoList, new Function<NoteInfo, Long>() {
                @Override
                public Long apply(NoteInfo input) {
                    return input.getRelId();
                }
            });
            waitingCreateNoteCustomer = Lists.newArrayList(Collections2.filter(distinctCustomerInfo, new Predicate<CustomerInfo>() {
                @Override
                public boolean apply(CustomerInfo input) {
                    return byCarId?!existsRelIds.contains(input.getCustomerCarId()): !existsRelIds.contains(input.getCustomerId());
                }
            }));
        }else {
            waitingCreateNoteCustomer = distinctCustomerInfo;
        }
        return waitingCreateNoteCustomer;
    }
}
