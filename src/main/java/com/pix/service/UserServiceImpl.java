package com.pix.service;


import com.mySpring.*;

@Component("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    OrderService orderService;

    private String beanName;

    @Override
    public void test(){
        System.out.println(orderService);
    }

//    @Override
//    public void setBeanName(String Name) {
//        this.beanName = Name;
//        System.out.println(beanName);
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.out.println("cccc");
//    }
}
