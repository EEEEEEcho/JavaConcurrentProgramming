package com.echo.juc.chapter3;

import java.util.ArrayList;

class ThreadSafe2{
    public void method1(int loop){
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0;i < loop;i ++){
            method2(list);
            method3(list);
        }
    }

    public void method2(ArrayList<String> list){
        list.add("1");
    }

    public void method3(ArrayList<String> list){
        list.remove(0);
    }
}
class ThreadSafe2Sub extends ThreadSafe2{
    @Override
    public void method3(ArrayList<String> list) {
        new Thread(() -> {
            list.remove(0);
        }).start();
    }
}
public class ExtendsUnsafe {
    public static void main(String[] args) {
        ThreadSafe2Sub t = new ThreadSafe2Sub();
        t.method1(200);
    }
}

public class MyServlet extends HttpServlet {
    // 是否安全？
    private UserService userService = new UserServiceImpl();
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        userService.update(...);
    }
}
public class UserServiceImpl implements UserService {
    // 记录调用次数
    private int count = 0;
    public void update() {
// ...
        count++;
    }
}