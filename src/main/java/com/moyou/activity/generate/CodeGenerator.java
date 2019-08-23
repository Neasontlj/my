package com.moyou.activity.generate;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @author  Neason
 */
public class CodeGenerator {

    public static void main(String[] args) {

        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(true)
                .setAuthor("Neason")
                .setOutputDir("./src/main/java")
                .setFileOverride(true)
                .setIdType(IdType.INPUT)
                .setServiceName("%sService")
                .setBaseResultMap(true)
                .setBaseColumnList(true);

        //2. 数据源配置
        DataSourceConfig  dsConfig  = new DataSourceConfig();
        dsConfig.setDbType(DbType.MYSQL)
                .setDriverName("com.mysql.jdbc.Driver")
                .setUrl("jdbc:mysql://39.106.26.128:3306/sport?useUnicode=true&characterEncoding=utf8&useSSL=false")
                .setUsername("cto")
                .setPassword("cto123456");
        //3. 策略配置globalConfiguration中
        StrategyConfig stConfig = new StrategyConfig();
        stConfig.setCapitalMode(true)
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude("moy_travel_user");
                //.setInclude("t_admin_feedback_dat").setInclude("t_admin_work_dat").setInclude("t_log_device_dat")
                //.setLogicDeleteFieldName("is_delete");

        //4. 包名策略配置
        PackageConfig pkConfig = new PackageConfig();
        pkConfig.setParent("com.moyou.activity")
                .setMapper("mapper")
                .setService("service")
                .setController("controller")
                .setEntity("model")
                .setXml("mapper");

        //5. 整合配置
        AutoGenerator  ag = new AutoGenerator();
        ag.setGlobalConfig(config)
                .setDataSource(dsConfig)
                .setStrategy(stConfig)
                .setPackageInfo(pkConfig);
        //6. 执行
        ag.execute();


    }

}
