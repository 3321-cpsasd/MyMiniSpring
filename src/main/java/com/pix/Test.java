package com.pix;

import com.mySpring.MyApplicationCopntext;

public class Test {
    public static void main(String[] args) {
        MyApplicationCopntext myApplicationCopntext = new MyApplicationCopntext(AppConfig.class);

        Object userService = myApplicationCopntext.getBean("userService");
    }
}
