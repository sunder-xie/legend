package com.tqmall.legend.entity.tqcheck;

/**
 * Created by lifeilong on 2016/4/14.
 */
public enum TqCheckFlagEnum {
    JY(1,1, "", "", ""),
    DC(2,1, "0%", "50%", "100"),
    LT(3,1, "低", "中", "高"),
    DP(4,1, "", "", ""),
    YG(5,1, "","",""),
    YG2(5,2, "","",""),
    PM(6,1, "0", "75", ""),
    KT(7,1, "有异", "无异", "");

    private final Integer cate;
    private final Integer item;
    private final String flagONE;
    private final String flagTWO;
    private final String flagTHREE;


    private TqCheckFlagEnum(Integer cate, Integer item, String flagONE, String flagTWO, String flagTHREE) {
        this.cate = cate;
        this.item = item;
        this.flagONE = flagONE;
        this.flagTWO = flagTWO;
        this.flagTHREE = flagTHREE;
    }

    public Integer getCate(){
        return cate;
    }
    public Integer getItem(){
        return item;
    }
    public String  getFlagONE(){
        return flagONE;
    }
    public String  getFlagTWO(){
        return flagTWO;
    }
    public String  getFlagTHREE(){
        return flagTHREE;
    }

    public static String getFlagByCateAndItem(Integer cate, Integer item, Integer flag){
        TqCheckFlagEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            TqCheckFlagEnum tEnum = arr[i];
            if(tEnum.getCate().equals(cate) && tEnum.getItem().equals(item)){
                if (flag.equals(1)) {
                    return tEnum.getFlagONE();
                }
                if(flag.equals(2)) {
                    return tEnum.getFlagTWO();
                }
                if(flag.equals(3)) {
                    return tEnum.getFlagTHREE();
                }
            }
        }
        return null;
    }
}
