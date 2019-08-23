package com.moyou.activity.util.csv.base;

import com.moyou.activity.util.csv.CsvField;
import com.moyou.activity.util.csv.CsvImportResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wyf 2018/11/5
 */
public class CsvUtilsService {

    /**
     * 日志对象
     **/
    private static final Logger logger = LoggerFactory.getLogger(CsvUtilsService.class);

    /**
     * 错误信息方法
     */
    private static final String ERROR_METHOD_NAME = "setErrMsg";

    /**
     * 非空校验错误信息
     */
    private static final String ERROR_IS_NOT_NULL_MSG = "值不能为空";

    /**
     * 最大长度校验错误信息
     */
    private static final String ERROR_MAX_LEN_MSG = "超过最大长度";

    /**
     * 最小长度校验错误信息
     */
    private static final String ERROR_MIN_LEN_MSG = "不足最小长度";

    /**
     * 正则校验错误信息
     */
    private static final String ERROR_REGEX_MSG = "不符合正则规则";

    /**
     * 设置导出数据属性
     * @param response
     * @param fileName
     */
    public static void setHeader(HttpServletResponse response, String fileName) {
        try{
            response.setContentType("application/csv;charset=GBK");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Expose-Headers", "*");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".csv");
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }



    /**
     * 泛型实体打印输出
     * @param beans 实体类集合
     * @return
     */
    public static <T> void getStringArrayFromBean(List<T> beans, PrintWriter out) {
        Class<? extends Object> cls = beans.get(0).getClass();
        Field[] declaredFields = cls.getDeclaredFields();
        List<Field> annoFields = new ArrayList<>();
        // 筛选出标有注解的字段
        for (Field field : declaredFields) {
            CsvField anno = field.getAnnotation(CsvField.class);
            if (anno != null) {
                annoFields.add(field);
            }
        }
        // 获取注解的值，即内容标题
        StringBuilder title = new StringBuilder();
        for (int i = 0; i < annoFields.size(); i++) {
            title.append(annoFields.get(i).getAnnotation(CsvField.class).name()).append(",");
        }
        title.deleteCharAt(title.length()-1);
        out.println(title);
        try {
            // 获取内容
            for (T t : beans) {
                StringBuilder item = new StringBuilder();
                for (Field field : annoFields) {
                    Class<?> valType = field.getType();
                    String fieldName = field.getName();
                    String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method method = ReflectionUtils.findMethod(t.getClass(), methodName);
                    if (method != null) {
                        Object value = ReflectionUtils.invokeMethod(method, t);
                        item.append(setValues(value, valType)).append(",");
                    }
                }
                item.deleteCharAt(item.length()-1);
                out.println(item);
            }
        } catch (Exception e) {
            logger.error("CSV导出：打印输出PrintWriter失败", e);
        }
        out.close();
    }



    /**
     * 数组转为对象集合
     * @param dataList 集合数据
     * @param bean 类类型
     * @return List<T>
     */
    public static <T> List<T> getBeanFromStringArray(List<String[]> dataList, Class<T> bean) {
        List<T> list = new ArrayList<>();
        List<Map<String, String>> titles = getTitles(dataList);
        Map<String, Field> fields = getFields(bean);
        try {
            for (Map<String, String> map : titles) {
                T t = bean.newInstance();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (fields.containsKey(entry.getKey())) {
                        Field field = fields.get(entry.getKey());
                        Class<?> valType = field.getType();
                        Object value = getType(entry.getValue(), valType);
                        String fieldName = field.getName();
                        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Method method = ReflectionUtils.findMethod(bean, methodName, valType);
                        if (method != null) {
                            ReflectionUtils.invokeMethod(method, t, value);
                        }
                    }
                }
                list.add(t);
            }
        } catch (Exception e) {
            logger.error("CSV导入：解析数据创建实体对象失败", e);
        }
        return list;
    }



    /**
     * 数组转为对象集合
     * @param dataList 集合数据
     * @param bean 类类型
     * @return List<T>
     */
    public static <T> CsvImportResult getBeanFromCheck(List<String[]> dataList, Class<T> bean) {
        CsvImportResult result = new CsvImportResult();
        List<Map<String, String>> titles = getTitles(dataList);
        try {
            Map<String, Field> fields = getFields(bean);
            Map<String, Boolean> notNulls = getNotNull(bean);
            Map<String, Integer> maxLens = getMaxLen(bean);
            Map<String, Integer> minLens = getMinLen(bean);
            Map<String, String> regexs = getRegex(bean);
            for (Map<String, String> map : titles) {
                T t = bean.newInstance();
                boolean flag = false;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (fields.containsKey(entry.getKey())) {
                        Field field = fields.get(entry.getKey());
                        Class<?> valType = field.getType();
                        Object value = getType(entry.getValue(), valType);
                        String fieldName = field.getName();
                        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Method method = ReflectionUtils.findMethod(bean, methodName, valType);
                        if (method != null) {
                            ReflectionUtils.invokeMethod(method, t, value);
                        }
                        boolean isNotNull = notNulls.get(entry.getKey());
                        Integer maxLen = maxLens.get(entry.getKey());
                        Integer minLen = minLens.get(entry.getKey());
                        String regex = regexs.get(entry.getKey());
                        if (!flag){
                            if (isNotNull && ("".equals(value) || value == null)){
                                Method errmethod = ReflectionUtils.findMethod(bean, ERROR_METHOD_NAME, valType);
                                if (errmethod != null){
                                    ReflectionUtils.invokeMethod(errmethod, t, entry.getKey() + ERROR_IS_NOT_NULL_MSG);
                                }
                                flag = true;
                                continue;
                            }
                            if (maxLen >= 1 && maxLen < String.valueOf(value).length()){
                                Method errmethod = ReflectionUtils.findMethod(bean, ERROR_METHOD_NAME, valType);
                                if (errmethod != null){
                                    ReflectionUtils.invokeMethod(errmethod, t, entry.getKey() + ERROR_MAX_LEN_MSG + maxLen);
                                }
                                flag = true;
                                continue;
                            }
                            if (minLen >= 1 && minLen > String.valueOf(value).length()){
                                Method errmethod = ReflectionUtils.findMethod(bean, ERROR_METHOD_NAME, valType);
                                if (errmethod != null){
                                    ReflectionUtils.invokeMethod(errmethod, t, entry.getKey() + ERROR_MIN_LEN_MSG + minLen);
                                }
                                flag = true;
                                continue;
                            }
                            if (StringUtils.isNotBlank(regex) && !match(regex, String.valueOf(value))){
                                Method errmethod = ReflectionUtils.findMethod(bean, ERROR_METHOD_NAME, String.class);
                                if (errmethod != null){
                                    ReflectionUtils.invokeMethod(errmethod, t, entry.getKey() + ERROR_REGEX_MSG);
                                }
                                flag = true;
                            }
                        }
                    }
                }
                if (flag){
                    result.getFailList().add(t);
                }else {
                    result.getSuccessList().add(t);
                }
            }
            result.setSuccessNum(result.getSuccessList().size());
            result.setTotalNum(result.getSuccessList().size() + result.getFailList().size());
        } catch (Exception e) {
            logger.error("CSV导入：解析数据创建实体对象失败", e);
        }
        return result;
    }



    /**
     * 数组标题与值的对应关系
     * @param dataList 集合数据
     * @return List
     */
    private static List<Map<String, String>> getTitles(List<String[]> dataList) {
        List<Map<String, String>> list = new ArrayList<>();
        String[] titles = dataList.get(0);
        dataList.remove(0);
        for (String[] values : dataList) {
            Map<String, String> titleMap = new HashMap<>();
            for (int i = 0; i < values.length; i++) {
                titleMap.put(titles[i], values[i]);
            }
            list.add(titleMap);
        }
        return list;
    }



    /**
     * 注解名称与字段属性的对应关系
     * @param clazz 实体对象类类型
     * @param <T> 泛型类型
     * @return Map
     */
    private static <T> Map<String, Field> getFields(Class<T> clazz) {
        Map<String, Field> annoMap = new HashMap<>();
        Field[] fileds = clazz.getDeclaredFields();
        for (Field filed : fileds) {
            CsvField anno = filed.getAnnotation(CsvField.class);
            if (anno != null && StringUtils.isNotBlank(anno.name())) {
                // 获取name属性值
                annoMap.put(anno.name(), filed);
            }
        }
        return annoMap;
    }



    /**
     * 注解名称与非空校验的对应关系
     * @param clazz 实体对象类类型
     * @param <T> 泛型类型
     * @return Map
     */
    private static <T> Map<String, Boolean> getNotNull(Class<T> clazz) {
        Map<String, Boolean> annoMap = new HashMap<>();
        Field[] fileds = clazz.getDeclaredFields();
        for (Field filed : fileds) {
            CsvField anno = filed.getAnnotation(CsvField.class);
            if (anno != null && StringUtils.isNotBlank(anno.name())) {
                annoMap.put(anno.name(), anno.isNotNull());
            }
        }
        return annoMap;
    }



    /**
     * 注解名称与最大长度的对应关系
     * @param clazz 实体对象类类型
     * @param <T> 泛型类型
     * @return Map
     */
    private static <T> Map<String, Integer> getMaxLen(Class<T> clazz) {
        Map<String, Integer> annoMap = new HashMap<>();
        Field[] fileds = clazz.getDeclaredFields();
        for (Field filed : fileds) {
            CsvField anno = filed.getAnnotation(CsvField.class);
            if (anno != null && StringUtils.isNotBlank(anno.name())) {
                annoMap.put(anno.name(), anno.maxLen());
            }
        }
        return annoMap;
    }



    /**
     * 注解名称与最小长度的对应关系
     * @param clazz 实体对象类类型
     * @param <T> 泛型类型
     * @return Map
     */
    private static <T> Map<String, Integer> getMinLen(Class<T> clazz) {
        Map<String, Integer> annoMap = new HashMap<>();
        Field[] fileds = clazz.getDeclaredFields();
        for (Field filed : fileds) {
            CsvField anno = filed.getAnnotation(CsvField.class);
            if (anno != null && StringUtils.isNotBlank(anno.name())) {
                annoMap.put(anno.name(), anno.minLen());
            }
        }
        return annoMap;
    }



    /**
     * 注解名称与正则的对应关系
     * @param clazz 实体对象类类型
     * @param <T> 泛型类型
     * @return Map
     */
    private static <T> Map<String, String> getRegex(Class<T> clazz) {
        Map<String, String> annoMap = new HashMap<>();
        Field[] fileds = clazz.getDeclaredFields();
        for (Field filed : fileds) {
            CsvField anno = filed.getAnnotation(CsvField.class);
            if (anno != null && StringUtils.isNotBlank(anno.name())) {
                annoMap.put(anno.name(), anno.regex());
            }
        }
        return annoMap;
    }



    /**
     * 转换值
     * @param value 属性值
     * @param valType 属性类型
     * @return String
     */
    private static String setValues(Object value, Class<?> valType) {
        if (value == null) {
            return "";
        } else if (valType == Date.class) {
            // 默认日期类型格式：yyyy-MM-dd HH:mm:ss
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(((Date) value).getTime());
        } else {
            // 字符串
            return String.valueOf(value);
        }
    }



    /**
     * 转换成实体属性对应的类型
     * @param value 每一格的数值
     * @param valType 实体属性类型
     * @return Object 转换为对应类型以obj返回
     */
    private static <T> Object getType(String value, Class<T> valType) {
        try {
            if (valType == Date.class) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return sdf.parse(value);
            } else if (valType == Double.class) {
                return Double.parseDouble(value);
            } else if (valType == BigDecimal.class) {
                return new BigDecimal(value);
            } else if (valType == Integer.class) {
                return Integer.parseInt(value);
            } else if (valType == Long.class) {
                return Long.parseLong(value);
            } else if (valType == Boolean.class) {
                return Boolean.parseBoolean(value);
            }
        } catch (Exception e) {
            logger.error("CSV导入：数据解析，创建对象，类型转换异常", e);
        }
        return value;
    }



    /**
     * 正则校验
     * @param regex 正则表达式
     * @param str   校验字符串
     * @return boolean
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
