package com.sprout.dlyy.student.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import java.io.*;
import java.util.Map;

public class FreemarkerUtils {
    private static String templatePath="/resources";

    public static Template getTemplate(String templateName){
        Template template = null;
        try {
            Configuration configuration=new Configuration();

            configuration.setDefaultEncoding("utf-8");
            // 指定模板文件从何处加载的数据源，这里设置成一个文件目录。
            configuration.setDirectoryForTemplateLoading(new File("d:\\"));

            configuration.setObjectWrapper(new DefaultObjectWrapper());

            template=configuration.getTemplate(templateName);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return template;
    }

    public static File createDoc(Map<?, ?> dataMap, String templateName, String destinationPath){
        File file=new File(destinationPath);
        try {
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer w = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
            Template template=getTemplate(templateName);
            template.process(dataMap, w);
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return file;
    }
}
