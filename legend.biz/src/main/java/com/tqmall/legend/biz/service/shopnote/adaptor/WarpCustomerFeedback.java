package com.tqmall.legend.biz.service.shopnote.adaptor;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.tqmall.legend.entity.customer.CustomerFeedback;
import com.tqmall.legend.entity.shop.NoteInfo;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yuchengdu on 16/7/30.
 */
public class WarpCustomerFeedback {
    /** 按照customerId为维度剔除数据源中的重复项，若果有重复项依照ID进行倒叙排列取第0个
     * @param sourceCustomerFeedback
     * @return
     */
    public static List<CustomerFeedback> distinctCustomerFeedback(List<CustomerFeedback> sourceCustomerFeedback){
        if (CollectionUtils.isEmpty(sourceCustomerFeedback)){
            return Collections.emptyList();
        }
        //对原始数据按照customerId进行分组
        Multimap<Long, CustomerFeedback> sourceCustomerFeedbackMapList = Multimaps.index(sourceCustomerFeedback, new Function<CustomerFeedback, Long>() {
            @Override
            public Long apply(CustomerFeedback input) {
                return input.getCustomerId();
            }
        });
        if (sourceCustomerFeedbackMapList.isEmpty()){
            return Collections.emptyList();
        }
        //目标数据链表
        List<CustomerFeedback> desCustomerCarFeedback = Lists.newArrayList();
        for (Long mapKey : sourceCustomerFeedbackMapList.keySet()) {
            Collection<CustomerFeedback> customerFeedbackCollection = sourceCustomerFeedbackMapList.get(mapKey);
            if (customerFeedbackCollection != null && customerFeedbackCollection.size() >= 1) {
                List<CustomerFeedback> keyMapCustomerCarList = Lists.newArrayList(customerFeedbackCollection);
                Collections.sort(keyMapCustomerCarList, new Comparator<CustomerFeedback>() {
                    @Override
                    public int compare(CustomerFeedback o1, CustomerFeedback o2) {
                        return o2.getId().compareTo(o1.getId());
                    }
                });
                desCustomerCarFeedback.add(keyMapCustomerCarList.get(0));
            }
        }
        return desCustomerCarFeedback;
    }

    /**
     * 剔除依据产生消息的数据源
     * @param noteInfoList
     * @param distinctCustomerFeedback
     * @return
     */
    public static List<CustomerFeedback> distinctCustomerFeedbackByNoteInfo(List<NoteInfo> noteInfoList, List<CustomerFeedback> distinctCustomerFeedback){
        List<CustomerFeedback> waitingCreateNoteCustomerFeedback;
        if (!CollectionUtils.isEmpty(noteInfoList)) {
            final List<Long> existsRelIds = Lists.newArrayList(Lists.transform(noteInfoList, new Function<NoteInfo, Long>() {
                @Override
                public Long apply(NoteInfo input) {
                    return input.getRelId();
                }
            }));
            waitingCreateNoteCustomerFeedback = Lists.newArrayList(Collections2.filter(distinctCustomerFeedback, new Predicate<CustomerFeedback>() {
                @Override
                public boolean apply(CustomerFeedback input) {
                    return !existsRelIds.contains(input.getId());
                }
            }));
        }else {
            waitingCreateNoteCustomerFeedback = distinctCustomerFeedback;
        }
        return waitingCreateNoteCustomerFeedback;
    }
}
