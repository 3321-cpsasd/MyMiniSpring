package com.pix.service;

import com.mySpring.BeanPostProcessor;
import com.mySpring.Component;

@Component("myPreProcessor")
public class MyPreProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessorBeforeInitialization(Object bean, String beanName) {
        System.out.println(1);
        return bean;
    }

    @Override
    public Object postProcessorAfterInitialization(Object bean, String beanName) {
        System.out.println(2);
        return bean;
    }
}
