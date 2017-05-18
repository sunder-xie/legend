package com.tqmall.legend.web.oss;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.common.CommonUtils;
import com.tqmall.legend.common.Result;
import com.tqmall.oss.OSSClientUtil;
import com.tqmall.oss.OSSConstants;
import com.tqmall.oss.ObjectKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixiao on 15-2-6.
 */
@Slf4j
@Controller
@RequestMapping("index/oss")
public class OSSController {

    @Autowired
    private OSSClientUtil ossClientUtil;
    @Value("${tqmall.oss.bucketName}")
    private String tqmallBucketName;
    @Value("${crm.oss.bucketName}")
    private String crmBucketName;
    @Value("${crm.oss.region}")
    private String crmRegion;
    @Value("${tqmall.oss.regionImg}")
    private String regionImg;
    /*
 * Java文件操作 获取文件扩展名
 *
 */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    @RequestMapping(value = "/upload_image", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> uploadImagesEditor(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            if (CollectionUtils.isEmpty(fileMap)) {
                result.put("state", "failed");
                result.put("errorCode", 50010);
                result.put("errorMsg", "列表为空");
                return result;
            }
            Map<String, Map<String, String>> nameUrls = new HashMap<String, Map<String, String>>();
            //开始处理图片组
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                MultipartFile multipartFile = entry.getValue();
                if (multipartFile.isEmpty())
                    nameUrls.put(multipartFile.getOriginalFilename(), null);
                else {
                    String filePath;
                    byte[] bs = multipartFile.getBytes();
                    String fileSuffix = getExtensionName(multipartFile.getOriginalFilename());

                    //上传原图
                    String fileKey = ObjectKeyUtil.generateOrigObjectkey(fileSuffix);
                    //是否测试环境
                    if ("Y".equals(ossClientUtil.getIsTest())){
                        fileKey = OSSConstants.TEST_OSS_URL + fileKey;
                    }else {
                        fileKey = OSSConstants.IMG_OSS_URL + fileKey;
                    }
                    filePath = ossClientUtil.putObject(tqmallBucketName,fileKey,bs);
                    result.put("state", "SUCCESS");
                    result.put("url", filePath);
                    result.put("title", multipartFile.getOriginalFilename());
                    result.put("original", multipartFile.getOriginalFilename());
                    result.put("size", multipartFile.getSize());
                }
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }



    /**
     *  有返回缩略小图
     *  上传图片
     */
    @RequestMapping(value = "/upload_img", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadImages(HttpServletRequest request) {

        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            if (CollectionUtils.isEmpty(fileMap)) {
                return Result.wrapErrorResult("", "图片为空,请上传图片");
            }
            Map<String, String> nameUrls = new HashMap<String, String>();
            // 开始处理图片组
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                MultipartFile multipartFile = entry.getValue();
                if (multipartFile.isEmpty()) {
                    nameUrls.put(multipartFile.getOriginalFilename(), null);
                } else {
                    String fileSuffix = getExtensionName(multipartFile.getOriginalFilename());
                    //上传原图
                    String fileKey = ObjectKeyUtil.generateOrigObjectkey(fileSuffix);
                    //是否测试环境
                    if ("Y".equals(ossClientUtil.getIsTest())){
                        fileKey = OSSConstants.TEST_OSS_URL + fileKey;
                    }else {
                        fileKey = OSSConstants.IMG_OSS_URL + fileKey;
                    }
                    ossClientUtil.putObject(tqmallBucketName,fileKey,multipartFile.getBytes());
                    String filePath = "http://" + tqmallBucketName + "." + this.regionImg + "/" + fileKey;
                    log.info("上传图片到legend云盘成功");
                    Map<String , String> imageMap = new HashMap<>();
                    imageMap.put("original", filePath);
                    imageMap.put("normal", ossClientUtil.getImgZoomFormat(fileKey,600,600));
                    imageMap.put("thumb", ossClientUtil.getImgZoomFormat(fileKey,100,100));
                    return Result.wrapSuccessfulResult(imageMap);
                }
            }
            return Result.wrapSuccessfulResult(nameUrls);
        } catch (Exception e) {
            log.error("上传图片失败:{}", e);
            return Result.wrapErrorResult("-1", "上传图片异常,请重试!");
        }

    }

    /**
     * 有限制的图片上传方法(大小不超过5M,格式需要是.jpg .jpeg .bmp .gif .png其中一种)
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload_img_limit", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadImagesLimit(HttpServletRequest request) {
        try {
            String fileSizeLimitStr= request.getParameter("_fileSizeLimit");
            long fileSizeLimit = 5242880L;//默认单个文件大小限制:1024*1024*5byte,即5M
            if(StringUtils.isNotBlank(fileSizeLimitStr)){
                fileSizeLimit = Long.parseLong(fileSizeLimitStr);
            }
            String fileSizeLimitName = CommonUtils.convertFileSize(fileSizeLimit);
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            if (CollectionUtils.isEmpty(fileMap)) {
                return Result.wrapErrorResult("", "图片为空,请上传图片");
            }
            //文件大小和格式校验
            for(Map.Entry<String, MultipartFile> entry : fileMap.entrySet()){
                MultipartFile multipartFile = entry.getValue();
                String contentType = multipartFile.getContentType();//文件类型
                long fileSize = multipartFile.getSize();
                if(fileSize>fileSizeLimit){
                    log.error("图片文件大小{}Byte超过{},不能上传",fileSize,fileSizeLimitName);
                    return Result.wrapErrorResult("-1", "请选择大小不超过"+fileSizeLimitName+"的图片文件");
                }
                if(!"image/gif".equals(contentType)&&
                        !"image/jpeg".equals(contentType)&&
                        !"image/jpg".equals(contentType)&&
                        !"image/png".equals(contentType)&&
                        !"image/bmp".equals(contentType)){
                    log.error("图片文件格式{}不满足要求,不能上传",contentType);
                    return Result.wrapErrorResult("-1", "请选择.jpg .jpeg .bmp .gif .png格式的图片");
                }

            }
            Map<String, String> nameUrls = new HashMap<String, String>();
            // 开始处理图片组
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                MultipartFile multipartFile = entry.getValue();

                if (multipartFile.isEmpty()) {
                    nameUrls.put(multipartFile.getOriginalFilename(), null);
                } else {
                    String fileSuffix = getExtensionName(multipartFile.getOriginalFilename());
                    //上传原图
                    String fileKey = ObjectKeyUtil.generateOrigObjectkey(fileSuffix);
                    //是否测试环境
                    if ("Y".equals(ossClientUtil.getIsTest())){
                        fileKey = OSSConstants.TEST_OSS_URL + fileKey;
                    }else {
                        fileKey = OSSConstants.IMG_OSS_URL + fileKey;
                    }
                    String filePath = ossClientUtil.putObject(tqmallBucketName,fileKey,multipartFile.getBytes());
                    log.info("上传图片到legend云盘成功");
                    Map<String , String> imageMap = new HashMap<>();
                    imageMap.put("original", filePath);
                    imageMap.put("normal", ossClientUtil.getImgZoomFormat(fileKey,600,600));
                    imageMap.put("thumb", ossClientUtil.getImgZoomFormat(fileKey,100,100));
                    return Result.wrapSuccessfulResult(imageMap);
                }
            }
            return Result.wrapSuccessfulResult(nameUrls);
        } catch (Exception e) {
            log.error("上传图片失败:{}", e);
            return Result.wrapErrorResult("-1", "上传图片异常,请重试!");
        }

    }

    /**
     * 上传到crm空间
     * @param request
     * @return
     */
    @RequestMapping(value = "/crm_upload_image", method = RequestMethod.POST)
    @ResponseBody
    public Result<Map<String, String>> uploadImagesToCrm(HttpServletRequest request) {
        int isAdmin = UserUtils.getUserInfo(request).getUserIsAdmin();
        if (isAdmin == 0) {
             log.error("您不是管理员,请联系管理员,当前用户{}", UserUtils.getUserInfo(request).getName());
            return Result.wrapErrorResult("9999", "您不是管理员,请联系管理员");
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        if (CollectionUtils.isEmpty(fileMap)) {
            return Result.wrapErrorResult("", "图片为空");
        }
        Map<String, String> nameUrls = new HashMap<String, String>();
        // 开始处理图片组
        try {
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                MultipartFile multipartFile = entry.getValue();
                if (multipartFile.isEmpty()) {
                    nameUrls.put(multipartFile.getOriginalFilename(), null);
                } else {
                    String fileSuffix = getExtensionName(multipartFile.getOriginalFilename());

                    //上传原图
                    String fileKey = ObjectKeyUtil.generateOrigObjectkey(fileSuffix);
                    //是否测试环境
                    if ("Y".equals(ossClientUtil.getIsTest())){
                        fileKey = OSSConstants.TEST_OSS_URL + fileKey;
                    }else {
                        fileKey = OSSConstants.IMG_OSS_URL + fileKey;
                    }
                    ossClientUtil.putObject(crmBucketName,fileKey,multipartFile.getBytes());
                    String filePath = "http://" + crmBucketName + "." + this.crmRegion + "/" + fileKey;
                     log.info("上传图片到crm云盘成功");
                    Map<String , String> imageMap = new HashMap<>();
                    imageMap.put("original", filePath);
                    imageMap.put("normal", ossClientUtil.getImgZoomFormat(fileKey,600,600));
                    imageMap.put("thumb", ossClientUtil.getImgZoomFormat(fileKey,100,100));
                    return Result.wrapSuccessfulResult(imageMap);
                }
            }
        } catch (IOException e) {
             log.error("上传图片失败:{}", e);
        }
        return Result.wrapSuccessfulResult(nameUrls);

    }

    /**
     *  上传图片返回小图,和文件名称
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload_image_name", method = RequestMethod.POST)
    @ResponseBody
    public Result<Map<String, Map<String, String>>> uploadImagesWithName(HttpServletRequest request) {
        // try {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        if (CollectionUtils.isEmpty(fileMap)) {
            return Result.wrapErrorResult("", "商品图片列表为空");
        }
        Map<String, Map<String, String>> nameUrls = new HashMap<String, Map<String, String>>();
        // 开始处理图片组
        try {
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                MultipartFile multipartFile = entry.getValue();
                if (multipartFile.isEmpty())
                    nameUrls.put(multipartFile.getOriginalFilename(), null);
                else {
                    String fileSuffix = getExtensionName(multipartFile.getOriginalFilename());
                    //上传原图
                    String fileKey = ObjectKeyUtil.generateOrigObjectkey(fileSuffix);
                    //是否测试环境
                    if ("Y".equals(ossClientUtil.getIsTest())){
                        fileKey = OSSConstants.TEST_OSS_URL + fileKey;
                    }else {
                        fileKey = OSSConstants.IMG_OSS_URL + fileKey;
                    }
                    String filePath = ossClientUtil.putObject(tqmallBucketName,fileKey,multipartFile.getBytes());
                    log.info("上传图片到legend云盘成功");
                    Map<String , String> imageMap = new HashMap<>();
                    imageMap.put("original", filePath);
                    imageMap.put("normal", ossClientUtil.getImgZoomFormat(fileKey,600,600));
                    imageMap.put("thumb", ossClientUtil.getImgZoomFormat(fileKey,100,100));
                    nameUrls.put(multipartFile.getOriginalFilename(), imageMap);
                }
            }
        } catch (IOException e) {
            log.error("上传图片失败:{}", e);
            return Result.wrapErrorResult("-1", "上传图片异常,请重试!");
        }
        return Result.wrapSuccessfulResult(nameUrls);
    }


}
