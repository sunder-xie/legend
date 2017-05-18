package com.tqmall.legend.biz.course.impl;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.course.CourseService;
import com.tqmall.legend.dao.course.CourseCatDao;
import com.tqmall.legend.dao.course.CourseDao;
import com.tqmall.legend.dao.course.CourseDetailDao;
import com.tqmall.legend.entity.course.Course;
import com.tqmall.legend.entity.course.CourseCat;
import com.tqmall.legend.entity.course.CourseDetail;
import com.tqmall.legend.entity.course.CourseStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by zsy on 15-5-7.
 */
@Service
public class CourseServiceImpl extends BaseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDao;
    @Autowired
    private CourseCatDao courseCatDao;
    @Autowired
    private CourseDetailDao courseDetailDao;

    @Override
    public int updateById(Course course) {
        return courseDao.updateById(course);
    }

    /**
     * 按类别获取课程分页
     * @param pageable
     * @param searchParams
     * @return
     */
    @Override
    public Page<Course> getPageByCat(Pageable pageable, Map<String, Object> searchParams) {
        searchParams.put("status", CourseStatusEnum.PUBLISH.getCode());
        Page<Course> page = super.getPage(courseDao, pageable, searchParams);
        if(page!=null&&page.getContent()!=null){
            Map<Long,List<CourseDetail>> itemMap = getDetailMap();
            for(Course course:page.getContent()){
                if(itemMap.containsKey(course.getId())){
                    course.setCourseDetailList(itemMap.get(course.getId()));
                }
            }
        }
        return page;
    }

    /**
     * 按时间获取课程分页
     * @param pageable
     * @param searchParams
     * @return
     */
    @Override
    public Page<Course> getPageByDate(Pageable pageable, Map<String, Object> searchParams) {
        if(pageable.getSort() != null){
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            ArrayList<String> sorts = new ArrayList<String>();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sorts.add(order.getProperty() + " " + order.getDirection().name());
            }
            searchParams.put("sorts", sorts);
        }
        Map<String,Object> courseDetailMap = new HashMap<>();
        courseDetailMap.put("status",CourseStatusEnum.PUBLISH.getCode());
        if(searchParams.containsKey("searchTime")){
            courseDetailMap.put("tradeTimeStart",searchParams.get("searchTime")+" 00:00:00");
            courseDetailMap.put("tradeTimeEnd",searchParams.get("searchTime")+" 23:59:59");
        }
        //获取指定日期的课次
        List<CourseDetail> courseDetailTempList = courseDetailDao.select(courseDetailMap);
        Set<Long> courseIdSet = new HashSet<>();
        for(CourseDetail courseDetail:courseDetailTempList){
            courseIdSet.add(courseDetail.getCourseId());
        }
        Integer totalSize=0;
        if(!CollectionUtils.isEmpty(courseIdSet)){
            searchParams.put("ids",courseIdSet);
        }else{
            courseIdSet.add(0L);
            searchParams.put("ids",courseIdSet);
        }
        searchParams.put("status",CourseStatusEnum.PUBLISH.getCode());

        //计算指定日期的对应课程总数
        totalSize = courseDao.selectCount(searchParams);

        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());

        searchParams.put("limit", pageRequest.getPageSize());
        searchParams.put("offset", pageRequest.getOffset());
        //获取指定日期的课次对应的所有课程
        List<Course> data = courseDao.select(searchParams);
        DefaultPage<Course> page = new DefaultPage<Course>(data, pageRequest, totalSize);
        if(page!=null && page.getContent()!=null){
            Map<Long,List<CourseDetail>> itemMap = getDetailMap();
            for(Course course:page.getContent()){
                if(itemMap.containsKey(course.getId())){
                    course.setCourseDetailList(itemMap.get(course.getId()));
                }
            }
        }
        return page;
    }

    /**
     * 获取courseId所对应的发布课次
     * @return
     */
    private Map<Long,List<CourseDetail>> getDetailMap() {
        Map<String,Object> courseDetailMap = new HashMap<>();
        courseDetailMap.put("status",CourseStatusEnum.PUBLISH.getCode());
        List<String> sorts = new ArrayList<>();
        sorts.add("trade_time asc");
        courseDetailMap.put("sorts",sorts);
        List<CourseDetail> courseDetailList = courseDetailDao.select(courseDetailMap);
        Map<Long,List<CourseDetail>> itemMap = new HashMap<>();
        for(CourseDetail courseDetail:courseDetailList){
            if(courseDetail.getTradeTime().compareTo(new Date())<1) {
               courseDetail.setOutDate(true);
            }
            Long tradeTime = DateUtil.convertStringToDateYMD(DateUtil.convertDateToYMD(courseDetail.getTradeTime())).getTime();
            Long todayTime = DateUtil.convertStringToDateYMD(DateUtil.convertDateToYMD(new Date())).getTime();
            int checkTime = tradeTime.compareTo(todayTime);
            if(checkTime==-1){
                courseDetail.setCheckDate(-1);//过期
            }else if(checkTime==0){
                courseDetail.setCheckDate(0);//今天
            }else if(checkTime==1){
                courseDetail.setCheckDate(1);//未过期
            }
            if(itemMap.containsKey(courseDetail.getCourseId())){
                itemMap.get(courseDetail.getCourseId()).add(courseDetail);
            }else{
                List<CourseDetail> itemList = new ArrayList<>();
                itemList.add(courseDetail);
                itemMap.put(courseDetail.getCourseId(),itemList);
            }
        }
        return itemMap;
    }

    @Override
    public Course selectById(Long id) {
        Course course =  courseDao.selectById(id);
        if(course!=null){
            Map<String,Object> catMap = new HashMap<>();
            catMap.put("status",CourseStatusEnum.PUBLISH.getCode());
            List<CourseCat> courseCatList = courseCatDao.select(catMap);
            for(CourseCat courseCat:courseCatList){
                if(courseCat.getId().equals(course.getCatId())){
                    course.setCatName(courseCat.getName());
                }
            }
            Map<Long,List<CourseDetail>> itemMap = getDetailMap();
            if(itemMap.containsKey(course.getId())){
                course.setCourseDetailList(itemMap.get(course.getId()));
            }
        }
        return course;
    }

    @Override
    public List<Course> select(Map<String, Object> searchParams) {
        return courseDao.select(searchParams);
    }

}
