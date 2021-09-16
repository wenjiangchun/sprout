package com.sprout.common.util;

import com.sprout.core.spring.SpringContextUtils;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Java类处理工具类
 * Created by Sofar on 2016/8/30.
 */
public class SproutClassUtils extends ClassUtils {

    private static PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private static MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
    /**
     * 获取指定路径下某个类或接口所有的子类信息
     * @param parentClass 指定类或接口
     * @param scanPackage 需要扫描的顶级包名称
     * @return 所有子类类名称集合列表
     */
    public static List<String> getSubclassNames(Class<?> parentClass, String scanPackage) throws Exception{
        String resourcePattern = "**/*.class";
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                org.springframework.util.ClassUtils.convertClassNameToResourcePath(new StandardEnvironment().resolveRequiredPlaceholders(scanPackage)) + "/" + resourcePattern;
        Set<String> classNames = new HashSet<>();
        Resource[]  resources = SpringContextUtils.getResources(packageSearchPath);

        for (Resource r : resources) {
            MetadataReader reader = metadataReaderFactory.getMetadataReader(r);

            String className = reader.getClassMetadata().getClassName();
            try {
                Class classz = Class.forName(className);
                if (parentClass.isAssignableFrom(classz) && parentClass != classz) {
                    classNames.add(className);
                }
            } catch (Throwable e) {
                continue;
            }
            }
            return new ArrayList<>(classNames);
    }
}
