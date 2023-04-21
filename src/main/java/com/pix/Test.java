package com.pix;

import com.mySpring.*;
import com.pix.service.UserService;
import com.pix.service.UserServiceImpl;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException {
        MyApplicationContext myApplicationContext = new MyApplicationContext(AppConfig.class);

        //识别单例Bean和原型Bean .... map<beanName,bean对象> 使用单例池存储单例Bean对象
        UserService userService = (UserService) myApplicationContext.getBean("userService");
//        System.out.println(userService);
        userService.test();
    }
}
