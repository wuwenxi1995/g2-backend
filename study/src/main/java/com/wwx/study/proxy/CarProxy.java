package com.wwx.study.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wuwenxi 2022-02-09
 */
public class CarProxy implements InvocationHandler {

    private Car car;

    public CarProxy() {
        this.car = new DefaultCar();
    }

    public void setDefaultCar(DefaultCar defaultCar) {
        this.car = defaultCar;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(car, args);
    }

    private static class DefaultCar implements Car {

        @Override
        public Car buildCar() {
            System.out.println(this + "=== 汽车构建完成 ===");
            return this;
        }

        @Override
        public void run() {
            System.out.println(this + "=== 汽车最高时速：210km/h ===");
        }
    }

    public static void main(String[] args) {
        Car car = newProxyInstance(Car.class, CarProxy.class);
        car.buildCar().run();
    }

    @SuppressWarnings("unchecked")
    private static <T> T newProxyInstance(Class<T> interfaceClass, Class<? extends InvocationHandler> proxyClass) {
        try {
            return (T) Proxy.newProxyInstance(CarProxy.class.getClassLoader(), new Class[]{interfaceClass}, proxyClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
