package com.tqmall.legend.web.serviceInfo;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.shop.impl.ShopServiceCateServiceImpl;
import com.tqmall.legend.biz.shop.impl.ShopServiceInfoServiceImpl;
import com.tqmall.legend.dao.shop.ShopServiceCateDao;
import com.tqmall.legend.dao.shop.ShopServiceInfoDao;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * Created by twg on 16/12/12.
 */
public class ServiceInfoImportTest {

    @InjectMocks
    private ShopServiceInfoServiceImpl shopServiceInfoService;
    @InjectMocks
    private ShopServiceCateServiceImpl shopServiceCateService;

    @Mock
    private ShopServiceInfoDao shopServiceInfoDao;
    @Mock
    private ShopServiceCateDao shopServiceCateDao;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        List<String> names = Lists.newArrayList();

        List<ShopServiceInfo> serviceInfos = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            ShopServiceInfo serviceInfo = new ShopServiceInfo();
            serviceInfo.setShopId(1L);
            serviceInfo.setServiceSn("GYS112211111" + i);
            serviceInfo.setName("测试服务名" + i);
            serviceInfos.add(serviceInfo);
        }
        when(shopServiceInfoService.findServiceInfoByNames(1L,names.toArray(new String[]{}))).thenReturn(serviceInfos);
        List<ShopServiceCate> shopServiceCates = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            ShopServiceCate shopServiceCate = new ShopServiceCate();
            shopServiceCate.setShopId(1L);
            shopServiceCate.setName("小修"+i);
            shopServiceCates.add(shopServiceCate);
        }
        when(shopServiceCateService.findServiceCatesByCatNames(0L, 2, names.toArray(new String[] { }))).thenReturn(shopServiceCates);

    }

    @Test
    public void testServiceInfoImport(){
        List<String> names = Lists.newArrayList();
        List<ShopServiceInfo> shopServiceInfos = shopServiceInfoService.findServiceInfoByNames(1L, names.toArray(new String[] { }));
        Assert.assertEquals(3,shopServiceInfos.size());
        Map<String, ShopServiceInfo> serviceInfoMap = Maps.uniqueIndex(shopServiceInfos, new Function<ShopServiceInfo, String>() {
            @Override
            public String apply(ShopServiceInfo input) {
                return input.getName();
            }
        });

        List<ShopServiceCate> shopServiceCates0 = shopServiceCateService.findServiceCatesByCatNames(0L, 2, names.toArray(new String[] { }));
        Assert.assertEquals(3,shopServiceCates0.size());
        List<ShopServiceCate> shopServiceCates1 = shopServiceCateService.findServiceCatesByCatNames(1L, 0, names.toArray(new String[] { }));
        Assert.assertEquals(3,shopServiceCates1.size());
    }
}
