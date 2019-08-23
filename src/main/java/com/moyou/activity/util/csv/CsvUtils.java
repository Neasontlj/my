package com.moyou.activity.util.csv;

import com.csvreader.CsvReader;
import com.moyou.activity.util.csv.base.CsvUtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CSV工具类
 * @author wyf 2018/11/5
 */
public final class CsvUtils {

    /**
     * 日志对象
     **/
    private static final Logger logger = LoggerFactory.getLogger(CsvUtils.class);

    /**
     * 字符编码
     */
    private static final String ENCODE = "GBK";


    /**
     * 下载CSV模板
     * @param response 响应对象
     * @param clazz  实体对象集合
     * @param fileName 文件名称
     */
    public static <T> void exportCsvTemplate(HttpServletResponse response, Class<T> clazz, String fileName) {
        try {
            // 筛选出标有注解的字段
            Field[] declaredFields = clazz.getDeclaredFields();
            List<Field> annoFields = new ArrayList<Field>();
            for (Field field : declaredFields) {
                CsvField anno = field.getAnnotation(CsvField.class);
                if (anno != null) {
                    annoFields.add(field);
                }
            }
            CsvUtilsService.setHeader(response, fileName);
            // 获取注解的值，即内容标题
            StringBuilder title = new StringBuilder();
            for (int i = 0; i < annoFields.size(); i++) {
                title.append(annoFields.get(i).getAnnotation(CsvField.class).name()).append(",");
            }
            title.deleteCharAt(title.length()-1);
            PrintWriter out = response.getWriter();
            out.println(title);
            out.close();
        } catch (Exception e) {
            logger.error("导出CSV模板失败", e);
        }
    }



    /**
     * 导出CSV文件
     * @param response 响应对象
     * @param beans 实体对象集合
     * @param fileName 文件名称
     */
    public static <T> void exportCSV(HttpServletResponse response, List<T> beans, String fileName) {
        try {
            CsvUtilsService.setHeader(response, fileName);
            // 写入内容
            CsvUtilsService.getStringArrayFromBean(beans, response.getWriter());
        } catch (Exception e) {
            logger.info("CSV导出失败", e);
        }
    }



    /**
     * 导入CSV文件
     * @param inputStream 文件流
     * @param bean     类类型
     * @return List<T>
     */
    public static <T> List<T> importCSV(InputStream inputStream, Class<T> bean) {
        List<String[]> dataList = new ArrayList<String[]>();
        CsvReader reader = null;
        try {
            // 创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
            reader = new CsvReader(inputStream, ',', Charset.forName(ENCODE));
            if (reader != null) {
                // 跳过表头，如果需要表头的话，这句可以忽略
//                reader.readHeaders();
                // 逐行读入除表头的数据
                while (reader.readRecord()) {
                    dataList.add(reader.getValues());
                }
                if (!dataList.isEmpty()) {
                    // 数组转对象
                    return CsvUtilsService.getBeanFromStringArray(dataList, bean);
                }
            }
        } catch (Exception e) {
            logger.error("CSV导入：读取失败", e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return Collections.emptyList();
    }



    /**
     * 导入CSV文件(数据校验)
     * @param inputStream 文件流
     * @param bean     类类型
     * @return CsvImportResult
     */
    public static <T> CsvImportResult importCSVCheck(InputStream inputStream, Class<T> bean) {
        List<String[]> dataList = new ArrayList<>();
        CsvReader reader = null;
        try {
            // 创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
            reader = new CsvReader(inputStream, ',', Charset.forName(ENCODE));
            if (reader != null) {
                // 跳过表头，如果需要表头的话，这句可以忽略
//                reader.readHeaders();
                // 逐行读入除表头的数据
                while (reader.readRecord()) {
                    dataList.add(reader.getValues());
                }
                if (!dataList.isEmpty()) {
                    // 数组转对象
                    return CsvUtilsService.getBeanFromCheck(dataList, bean);
                }
            }
        } catch (Exception e) {
            logger.error("CSV导入：读取失败", e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return null;
    }

}