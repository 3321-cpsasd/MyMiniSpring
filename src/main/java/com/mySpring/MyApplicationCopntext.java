package com.mySpring;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;

public class MyApplicationCopntext {
    private Class configClass;

    public MyApplicationCopntext(Class configClass) throws ClassNotFoundException {
        this.configClass = configClass;
        //获取扫描路径
        ComponentScan declaredAnnotation = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);

        String path = declaredAnnotation.value();

        //扫描
        //获取类加载器
        //BootStrap ---> jre/lib
        //Ext  -----> jre/ext/lib
        //App ------> classpath

        ClassLoader classLoader = MyApplicationCopntext.class.getClassLoader();
        URL resource = classLoader.getResource("com/pix/service");//得到的是一个目录
        File file = new File(resource.getFile());

        if(file.isDirectory()){
            File[] files = file.listFiles();
            for (File file1 : files) {
                String fileName = file1.getAbsolutePath();
                if(fileName.endsWith(".class")) {
                    String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                    className = className.replace("\\", ".");
                    System.out.println(className);

                    Class<?> clazz = classLoader.loadClass(className);

                    if(clazz.isAnnotationPresent(Component.class)){
                        //..
                    }
                }
            }
        }


    }

    public Object getBean(String beanName){
        return null;
    }
}
