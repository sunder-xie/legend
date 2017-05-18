package com.tqmall.legend.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class Constants {
    public static Map<String, Long> precheckItemMapping;
    public static Map<Long, String> ftlItemMapping;
    public static Map<String, String> itemSuggestionMap;
    public static Map<String, String> areaAbbreviationMap;
    public static String SIGN_PRE;
    public static String SIGN_POST;

    //维修综合报表，中存放领料人角色
    public static Map<Long, Object> roleName = new ConcurrentHashMap<>();

    static {
        precheckItemMapping = new HashMap<>();
        precheckItemMapping.put("left_front_wing", 5L);
        precheckItemMapping.put("right_front_wing", 6L);
        precheckItemMapping.put("left_rear_wing", 14L);
        precheckItemMapping.put("right_rear_wing", 15L);
        precheckItemMapping.put("left_front_bumper", 20L);
        precheckItemMapping.put("left_rear_bumper", 21L);
        precheckItemMapping.put("right_front_bumper", 106L);
        precheckItemMapping.put("right_rear_bumper", 107L);
        precheckItemMapping.put("left_front_door", 8L);
        precheckItemMapping.put("right_front_door", 9L);
        precheckItemMapping.put("front_hood", 23L);
        precheckItemMapping.put("rear_hood", 24L);
        precheckItemMapping.put("left_rear_door", 11L);
        precheckItemMapping.put("right_rear_door", 12L);
        precheckItemMapping.put("front_cover", 26L);
        precheckItemMapping.put("rear_cover", 27L);
        precheckItemMapping.put("left_wing_mirror", 29L);
        precheckItemMapping.put("right_wing_mirror", 30L);
        precheckItemMapping.put("left_beam", 17L);
        precheckItemMapping.put("right_beam", 18L);
        precheckItemMapping.put("left_front_pressure", 32L);
        precheckItemMapping.put("right_front_pressure", 33L);
        precheckItemMapping.put("left_rear_pressure", 34L);
        precheckItemMapping.put("right_rear_pressure", 35L);
        precheckItemMapping.put("left_front_tread", 37L);
        precheckItemMapping.put("right_front_tread", 38L);
        precheckItemMapping.put("left_rear_tread", 39L);
        precheckItemMapping.put("right_rear_tread", 40L);
        precheckItemMapping.put("left_front_damage", 42L);
        precheckItemMapping.put("right_front_damage", 43L);
        precheckItemMapping.put("left_rear_damage", 44L);
        precheckItemMapping.put("right_rear_damage", 45L);
        precheckItemMapping.put("three_filters", 46L);
        precheckItemMapping.put("engine_oil", 61L);
        precheckItemMapping.put("spark_plug", 49L);
        precheckItemMapping.put("brake_oil", 64L);
        precheckItemMapping.put("rain_wiper", 52L);
        precheckItemMapping.put("gear_oil", 67L);
        precheckItemMapping.put("brake_pad", 55L);
        precheckItemMapping.put("antifreeze", 70L);
        precheckItemMapping.put("battery", 58L);
        precheckItemMapping.put("steering_fluid", 73L);
        precheckItemMapping.put("key", 78L);
        precheckItemMapping.put("extinguisher", 79L);
        precheckItemMapping.put("jack", 81L);
        precheckItemMapping.put("drive_license", 82L);
        precheckItemMapping.put("car_license", 83L);
        precheckItemMapping.put("tool_box", 80L);
        precheckItemMapping.put("back_tyre", 84L);
        precheckItemMapping.put("file_in_car", 85L);
        precheckItemMapping.put("screen", 86L);
        precheckItemMapping.put("interior", 87L);
        precheckItemMapping.put("audio", 88L);
        precheckItemMapping.put("cigar_lighter", 89L);
        precheckItemMapping.put("glass_lifter", 90L);
        precheckItemMapping.put("auto_door", 91L);
        precheckItemMapping.put("trouble_light", 92L);
        precheckItemMapping.put("dashboard", 93L);
        precheckItemMapping.put("noise", 94L);
        precheckItemMapping.put("func_error", 95L);
        precheckItemMapping.put("front_axle", 96L);
        precheckItemMapping.put("rear_axle", 97L);
        precheckItemMapping.put("power", 98L);
        precheckItemMapping.put("tyre_change", 99L);
        precheckItemMapping.put("tyre_error", 100L);
        precheckItemMapping.put("other_suggestion", 101L);
        precheckItemMapping.put("front_wind_shield", 102L);
        precheckItemMapping.put("rear_wind_shield", 103L);
        precheckItemMapping.put("out_lights", 104L);
        precheckItemMapping.put("oil_meter", 105L);

        precheckItemMapping.put("left_front_hub", 108L);
        precheckItemMapping.put("left_rear_hub", 109L);
        precheckItemMapping.put("right_front_hub", 110L);
        precheckItemMapping.put("right_rear_hub", 111L);

        ftlItemMapping = new HashMap<>();
        ftlItemMapping.put(5L, "left_front_wing");
        ftlItemMapping.put(6L, "right_front_wing");
        ftlItemMapping.put(14L, "left_rear_wing");
        ftlItemMapping.put(15L, "right_rear_wing");
        ftlItemMapping.put(20L, "left_front_bumper");
        ftlItemMapping.put(21L, "left_rear_bumper");
        ftlItemMapping.put(106L, "right_front_bumper");
        ftlItemMapping.put(107L, "right_rear_bumper");
        ftlItemMapping.put(8L, "left_front_door");
        ftlItemMapping.put(9L, "right_front_door");
        ftlItemMapping.put(23L, "front_hood");
        ftlItemMapping.put(24L, "rear_hood");
        ftlItemMapping.put(11L, "left_rear_door");
        ftlItemMapping.put(12L, "right_rear_door");
        ftlItemMapping.put(26L, "front_cover");
        ftlItemMapping.put(27L, "rear_cover");
        ftlItemMapping.put(29L, "left_wing_mirror");
        ftlItemMapping.put(30L, "right_wing_mirror");
        ftlItemMapping.put(17L, "left_beam");
        ftlItemMapping.put(18L, "right_beam");
        ftlItemMapping.put(32L, "left_front_pressure");
        ftlItemMapping.put(33L, "right_front_pressure");
        ftlItemMapping.put(34L, "left_rear_pressure");
        ftlItemMapping.put(35L, "right_rear_pressure");
        ftlItemMapping.put(37L, "left_front_tread");
        ftlItemMapping.put(38L, "right_front_tread");
        ftlItemMapping.put(39L, "left_rear_tread");
        ftlItemMapping.put(40L, "right_rear_tread");
        ftlItemMapping.put(42L, "left_front_damage");
        ftlItemMapping.put(43L, "right_front_damage");
        ftlItemMapping.put(44L, "left_rear_damage");
        ftlItemMapping.put(45L, "right_rear_damage");
        ftlItemMapping.put(46L, "three_filters");
        ftlItemMapping.put(61L, "engine_oil");
        ftlItemMapping.put(49L, "spark_plug");
        ftlItemMapping.put(64L, "brake_oil");
        ftlItemMapping.put(52L, "rain_wiper");
        ftlItemMapping.put(67L, "gear_oil");
        ftlItemMapping.put(55L, "brake_pad");
        ftlItemMapping.put(70L, "antifreeze");
        ftlItemMapping.put(58L, "battery");
        ftlItemMapping.put(73L, "steering_fluid");
        ftlItemMapping.put(78L, "key");
        ftlItemMapping.put(79L, "extinguisher");
        ftlItemMapping.put(81L, "jack");
        ftlItemMapping.put(82L, "drive_license");
        ftlItemMapping.put(83L, "car_license");
        ftlItemMapping.put(80L, "tool_box");
        ftlItemMapping.put(84L, "back_tyre");
        ftlItemMapping.put(85L, "file_in_car");
        ftlItemMapping.put(86L, "screen");
        ftlItemMapping.put(87L, "interior");
        ftlItemMapping.put(88L, "audio");
        ftlItemMapping.put(89L, "cigar_lighter");
        ftlItemMapping.put(90L, "glass_lifter");
        ftlItemMapping.put(91L, "auto_door");
        ftlItemMapping.put(92L, "trouble_light");
        ftlItemMapping.put(93L, "dashboard");
        ftlItemMapping.put(94L, "noise");
        ftlItemMapping.put(95L, "func_error");
        ftlItemMapping.put(96L, "front_axle");
        ftlItemMapping.put(97L, "rear_axle");
        ftlItemMapping.put(98L, "power");
        ftlItemMapping.put(99L, "tyre_change");
        ftlItemMapping.put(100L, "tyre_error");
        ftlItemMapping.put(101L, "other_suggestion");
        ftlItemMapping.put(102L, "front_wind_shield");
        ftlItemMapping.put(103L, "rear_wind_shield");
        ftlItemMapping.put(104L, "out_lights");
        ftlItemMapping.put(105L, "oil_meter");

        ftlItemMapping.put(108L, "left_front_hub");
        ftlItemMapping.put(109L, "left_rear_hub");
        ftlItemMapping.put(110L, "right_front_hub");
        ftlItemMapping.put(111L, "right_rear_hub");

        itemSuggestionMap = new HashMap<>();
        itemSuggestionMap.put("three_filters", "filter_upkeep");
        itemSuggestionMap.put("engine_oil", "engine_oil_upkeep");
        itemSuggestionMap.put("spark_plug", "spark_upkeep");
        itemSuggestionMap.put("brake_oil", "brake_oil_upkeep");
        itemSuggestionMap.put("rain_wiper", "wiper_upkeep");
        itemSuggestionMap.put("gear_oil", "gear_oil_upkeep");
        itemSuggestionMap.put("brake_pad", "brake_pad_upkeep");
        itemSuggestionMap.put("antifreeze", "antifreeze_upkeep");
        itemSuggestionMap.put("battery", "battery_upkeep");
        itemSuggestionMap.put("steering_fluid", "steering_fluid_upkeep");

        areaAbbreviationMap = new HashMap<>();
        areaAbbreviationMap.put("京", "北京");
        areaAbbreviationMap.put("津", "天津");
        areaAbbreviationMap.put("冀", "河北");
        areaAbbreviationMap.put("晋", "山西");
        areaAbbreviationMap.put("蒙", "内蒙古");
        areaAbbreviationMap.put("辽", "辽宁");
        areaAbbreviationMap.put("吉", "吉林");
        areaAbbreviationMap.put("黑", "黑龙江");
        areaAbbreviationMap.put("沪", "上海");
        areaAbbreviationMap.put("苏", "江苏");
        areaAbbreviationMap.put("浙", "浙江");
        areaAbbreviationMap.put("皖", "安徽");
        areaAbbreviationMap.put("闽", "福建");
        areaAbbreviationMap.put("赣", "江西");
        areaAbbreviationMap.put("鲁", "山东");
        areaAbbreviationMap.put("豫", "河南");
        areaAbbreviationMap.put("鄂", "湖北");
        areaAbbreviationMap.put("湘", "湖南");
        areaAbbreviationMap.put("粤", "广东");
        areaAbbreviationMap.put("桂", "广西");
        areaAbbreviationMap.put("琼", "海南");
        areaAbbreviationMap.put("渝", "重庆");
        areaAbbreviationMap.put("川", "四川");//川/蜀
        areaAbbreviationMap.put("贵", "贵州");//贵/黔
        areaAbbreviationMap.put("云", "云南");//云/滇
        areaAbbreviationMap.put("藏", "西藏");
        areaAbbreviationMap.put("陕", "陕西");//陕/秦
        areaAbbreviationMap.put("甘", "甘肃");//甘/陇
        areaAbbreviationMap.put("青", "青海");
        areaAbbreviationMap.put("宁", "宁夏");
        areaAbbreviationMap.put("新", "新疆");
        areaAbbreviationMap.put("港", "香港");
        areaAbbreviationMap.put("澳", "澳门");
        areaAbbreviationMap.put("台", "台湾");

        Properties prop = new Properties();
        //加载properties文件
        String path = Constants.class.getClassLoader().getResource("constants.properties").getPath();
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            prop.load(in);
            //配置常量
            SIGN_PRE = prop.getProperty("SIGN_PRE");
            SIGN_POST = prop.getProperty("SIGN_POST");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    public static String PVG_CTRL = "pvgCtrlObj";
    public final static Integer PRECHECK_OUTLINE_CHECK = 1;
    public final static Integer PRECHECK_TYRE_CHECK = 2;
    public final static Integer PRECHECK_OTHER_CHECK = 3;
    //会员服务的默认服务项目:洗车, 保养, 美容, 检查
    public static String MEMBER_SERVICE_WASH = "洗车";
    public static String MEMBER_SERVICE_UPKEEP = "保养";
    public static String MEMBER_SERVICE_FACIAL = "美容";
    public static String MEMBER_SERVICE_CHECK = "检查";

    //预检单项目类别
    public static String PRECHECK_ITEMS = "PRECHECK_ITEMS";
    //预检单项目值
    public static String PRECHECK_ITEMS_VALUES = "PRECHECK_ITEMS_VALUES";


//    public static String PREFIX_PRECHECK_ORDER = "YJ";
//    public static String PREFIX_REPAIR_ORDER = "WX";
//    public static String PREFIX_UPKEEP_ORDER = "BY";

    public static Date StringToDate(String dateStr, String formatStr) {
        DateFormat dd = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = dd.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
