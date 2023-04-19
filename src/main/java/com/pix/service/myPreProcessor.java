package com.pix.service;

import com.mySpring.BeanPostProcessor;

public class myPreProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessorBeforeInitialization(Object bean, String beanName) {
        return null;
    }

    @Override
    public Object postProcessorAfterInitialization(Object bean, String beanName) {
        return null;
    }
}
