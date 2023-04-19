package com.mySpring;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class MyApplicationContext {
    private Class configClass;
    private ConcurrentHashMap<String,Object> singletonObjects = new ConcurrentHashMap<>();//单例池
    private ConcurrentHashMap<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(); //bean的定义
    /*
    * */
    public MyApplicationContext(Class configClass) throws ClassNotFoundException {
        this.configClass = configClass;
        //获取扫描路径

        //解析配置类
        //ComponentScan注解 --> 扫描路径 --> 扫描 --> BeanDefinition --> BeanDefinitionMap
        scan(configClass);

        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if(beanDefinition.getScope().equals("singleton")){
                Object o = creatBean(beanName,beanDefinition);
                singletonObjects.put(beanName,o);
            }
        }

    }

    public Object creatBean(String beanName,BeanDefinition beanDefinition){
        Class clazz = beanDefinition.getClazz();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();

            //DI

            for (Field declaredField : clazz.getDeclaredFields()) {
                if(declaredField.isAnnotationPresent(Autowired.class)){

                    Object bean = getBean(declaredField.getName());
                    declaredField.setAccessible(true);
                    declaredField.set(instance,bean);
                }
            }

            //回调
            if(instance instanceof BeanNameAware){
                ((BeanNameAware) instance).setBeanName(beanName);
            }
            //
            if(instance instanceof InitializingBean){
                ((InitializingBean) instance).afterPropertiesSet();
            }
            return instance;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void scan(Class configClass) {
        ComponentScan declaredAnnotation = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);

        String path = declaredAnnotation.value();

        //扫描
        //获取类加载器
        //BootStrap ---> jre/lib
        //Ext  -----> jre/ext/lib
        //App ------> classpath

        ClassLoader classLoader = MyApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource("com/pix/service");//得到的是一个目录
        File file = new File(resource.getFile());

        if(file.isDirectory()){
            File[] files = file.listFiles();
            for (File file1 : files) {
                String fileName = file1.getAbsolutePath();
                if(fileName.endsWith(".class")) {
                    String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                    className = className.replace("\\", ".");
                    // System.out.println(className);
                    try{
                        Class<?> clazz = classLoader.loadClass(className);

                        if(clazz.isAnnotationPresent(Component.class)){
                            //表示这个类是一个Bean
                            //需要解析当前Bean类型: singleton or prototype -->BeanDefinition
                            Component componentAnnotation = clazz.getAnnotation(Component.class);
                            String beanName = componentAnnotation.value();

                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setClazz(clazz);
                            if(clazz.isAnnotationPresent(Scope.class)){
                                Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
                                beanDefinition.setScope(scopeAnnotation.value());
                            }else{
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionMap.put(beanName,beanDefinition);

                        }
                    }catch (ClassNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Object getBean(String beanName){
        if(beanDefinitionMap.containsKey(beanName)){
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if(beanDefinition.getScope().equals("singleton")){
                Object o = singletonObjects.get(beanName);
                return o;
            }else{
                //创建Bean对象
                Object o = creatBean(beanName,beanDefinition);
                return o;
            }

        }else{
            //在ioc容器中不存在对应的bean
            throw new NullPointerException();
        }

    }
}
