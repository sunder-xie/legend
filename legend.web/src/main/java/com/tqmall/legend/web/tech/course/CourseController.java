package com.tqmall.legend.web.tech.course;

import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.course.CourseCatService;
import com.tqmall.legend.biz.course.CourseDetailService;
import com.tqmall.legend.biz.course.CourseService;
import com.tqmall.legend.biz.course.CourseShopRelService;
import com.tqmall.legend.biz.tech.TechProductService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.course.*;
import com.tqmall.legend.web.utils.ServletUtils;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Created by zsy on 2015/5/8.
 */
@Controller
@RequestMapping("shop/tech/course")
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    CourseCatService courseCatService;
    @Autowired
    CourseDetailService courseDetailService;
    @Autowired
    CourseShopRelService courseShopRelService;
    @Autowired
    TechProductService techProductService;

    Logger logger = LoggerFactory.getLogger(CourseController.class);

    @RequestMapping
    public String index(Model model,@RequestParam(value = "flag",required = false) Integer flag){
        if(flag==null){
            flag =1;
        }
        if(flag==1){
            //按日期查询
            model.addAttribute("dateList",getDateList());
            model.addAttribute("courseTab","time");
        }else{
            //按课程类型查询
            model.addAttribute("catList",getCourseCatList());
            model.addAttribute("courseTab","cat");
        }
        model.addAttribute("flag",flag);
        model.addAttribute("moduleUrl", "tech");
        model.addAttribute("techTab","course");
        return "tech/course/course_list";
    }


    @RequestMapping(value = "list",method = RequestMethod.GET)
    @ResponseBody
    public Result getCourseList(@PageableDefault(page = 1, value = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                HttpServletRequest request){
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        DefaultPage<Course> page =null;
        if(searchParams.containsKey("searchTime")){
            page = (DefaultPage<Course>) courseService.getPageByDate(pageable, searchParams);
        }else{
            page = (DefaultPage<Course>) courseService.getPageByCat(pageable, searchParams);
        }
        page.setPageUri(request.getRequestURI());
        page.setSearchParam(ServletUtils.getParametersStringStartWith(request));
        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 课程详情页面
     * @return
     */
    @RequestMapping("detail")
    public String courseIndex(@RequestParam(value = "id",required = false)Long id,Model model,HttpServletRequest request){
        Map<String,Object> courseMap = new HashMap<>();
        courseMap.put("id",id);
        Course course = courseService.selectById(id);
        if(id == null || id < 0
           || course==null || !course.getStatus().equals(CourseStatusEnum.PUBLISH.getCode())){
            return "redirect:";
        }
        //获取课次对应的报名情况
        List<CourseDetail> courseDetailList = course.getCourseDetailList();
        if(!CollectionUtils.isEmpty(courseDetailList)){
            UserInfo userInfo = UserUtils.getUserInfo(request);
            if (null == userInfo) {
                logger.error("用户信息错误");
                return "redirect:";
            }
            Map<String,Object> courseShopRelMap = new HashMap<>();
            courseShopRelMap.put("managerLoginId",userInfo.getManagerLoginId());
            List<CourseShopRel> courseShopRelList = courseShopRelService.select(courseShopRelMap);
            if(!CollectionUtils.isEmpty(courseShopRelList)){
                Map<Long,CourseShopRel> courseShopRelMaps = new HashMap<>();
                for(CourseShopRel courseShopRel:courseShopRelList){
                    courseShopRelMaps.put(courseShopRel.getCourseDetailId(),courseShopRel);
                }
                for(CourseDetail courseDetail:courseDetailList){
                    if(courseShopRelMaps.containsKey(courseDetail.getId())){
                        courseDetail.setCourseShopRel(courseShopRelMaps.get(courseDetail.getId()));
                    }
                }
            }
        }
        model.addAttribute("course",course);
        model.addAttribute("moduleUrl","tech");
        model.addAttribute("techTab","course");
        return "tech/course/course_detail";
    }


    @RequestMapping(value = "join",method = RequestMethod.POST)
    @ResponseBody
    public Result join(HttpServletRequest request,CourseDetail courseDetail){
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (null == userInfo) {
            logger.error("用户信息错误");
            return Result.wrapErrorResult("", "用户信息错误");
        }
        if(courseDetail==null || courseDetail.getJoinCount()==null||courseDetail.getJoinCount()<0L){
            logger.error("数据为空");
            return Result.wrapErrorResult("","数据为空");
        }
        //课次门店关联
        CourseDetail courseDetailItem = courseDetailService.selectById(courseDetail.getId());
        if(courseDetailItem==null){
            return Result.wrapErrorResult("","课次id不存在");
        }
        Map<String,Object> courseShopRelMap = new HashMap<>();
        courseShopRelMap.put("managerLoginId",userInfo.getManagerLoginId());
        courseShopRelMap.put("courseDetailId",courseDetail.getId());
        List<CourseShopRel> courseShopRelList = courseShopRelService.select(courseShopRelMap);
        //判断报名课次是否已经过期
        if(courseDetailItem.getTradeTime().compareTo(new Date())<1){
            return Result.wrapErrorResult("","此课次已经过期，无法报名");
        }
        //判断报名人数是否超出范围
        Long count;
        if(CollectionUtils.isEmpty(courseShopRelList)){
            count= courseDetailItem.getJoinCount()+courseDetail.getJoinCount();
        }else{
            count = courseDetailItem.getJoinCount() + courseDetail.getJoinCount()-courseShopRelList.get(0).getJoinCount();
        }
        if(courseDetailItem.getLimitCount()<count){
            return Result.wrapErrorResult("","报名人数超额");
        }
        if(courseDetail.getJoinCount()<=0){
            return Result.wrapErrorResult("","报名人数不符");
        }
        if(courseDetail.getJoinCount()>5){
            return Result.wrapErrorResult("","一个账号最多报名5人");
        }
        //处理门店报名表
        CourseShopRel courseShopRel = new CourseShopRel();
        courseShopRel.setShopId(userInfo.getShopId());
        courseShopRel.setManagerLoginId(userInfo.getManagerLoginId());
        courseShopRel.setJoinCount(courseDetail.getJoinCount());
        courseShopRel.setCourseId(courseDetail.getCourseId());
        courseShopRel.setCourseDetailId(courseDetail.getId());

        Result result;
        courseDetailItem.setId(courseDetail.getId());
        if(CollectionUtils.isEmpty(courseShopRelList)){
            //没有数据，则添加
            result = courseShopRelService.insert(courseShopRel);
            logger.info("用户id"+courseShopRel.getManagerLoginId()+"添加了报名信息");
            //更新人数
            courseDetailItem.setJoinCount(courseDetailItem.getJoinCount() + courseDetail.getJoinCount());
            courseDetailService.updateById(courseDetailItem);
        }else{
            //更新
            courseShopRel.setId(courseShopRelList.get(0).getId());
            result = courseShopRelService.update(courseShopRel);
            logger.info("用户id"+courseShopRel.getManagerLoginId()+"更新了报名信息");
            //更新人数,原报名人数+目前报名人数-旧门店报名人数
            courseDetailItem.setJoinCount(courseDetailItem.getJoinCount() + courseDetail.getJoinCount()-courseShopRelList.get(0).getJoinCount());
            courseDetailService.updateById(courseDetailItem);
        }

        return Result.wrapSuccessfulResult(result);
    }

    /**
     * 获取近两周内的日期信息
     * @return
     */
    @RequestMapping("get_date")
    @ResponseBody
    public List<CourseDateBo> getDateList(){
        List<CourseDateBo> dateList = new ArrayList<>();
        for(int i=0;i<14;i++){
            Date date = new Date();
            Date day = DateUtils.addDays(date, i);
            String week = DateUtil.getWeekOfDate(day);
            String monthDay="";
            String dayStr="";
            try{
                SimpleDateFormat df = new SimpleDateFormat("MM月dd日");
                SimpleDateFormat dfStr = new SimpleDateFormat("yyyy-MM-dd");
                monthDay = df.format(day);
                dayStr = dfStr.format(day);
            }catch (Exception e){
                logger.error("日期转换有误");
            }
            StringBuffer time = new StringBuffer();
            time.append(week+"("+monthDay+")");
            CourseDateBo courseDateBo = new CourseDateBo();
            courseDateBo.setDateStr(time.toString());
            courseDateBo.setDate(dayStr);
            dateList.add(courseDateBo);
        }
        return dateList;
    }

    /**
     * 获取课程类别
     * @return
     */
    @RequestMapping("get_cat")
    @ResponseBody
    public List<CourseCat> getCourseCatList(){
        Map<String,Object> courseCatMap = new HashMap<>();
        courseCatMap.put("status", CourseStatusEnum.PUBLISH.getCode());
        List<CourseCat> courseCatList = courseCatService.select(courseCatMap);
        courseCatMap.put("parentId",0);
        List<CourseCat> parentCourseCatList= courseCatService.select(courseCatMap);
        for(CourseCat courseCat:parentCourseCatList){
            List<CourseCat> childCat = new ArrayList<>();
            for(CourseCat item:courseCatList){
                if(item.getParentId().equals(courseCat.getId())){
                    childCat.add(item);
                }
            }
            courseCat.setChildCatList(childCat);
        }
        return parentCourseCatList;
    }
}
