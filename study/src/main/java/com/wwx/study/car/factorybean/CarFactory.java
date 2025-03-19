package com.wwx.study.car.factorybean;

import com.wwx.study.car.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author wuwenxi 2022-02-09
 */
public class CarFactory implements FactoryBean<Car> {

    private static final Logger log = LoggerFactory.getLogger(CarFactory.class);

    @Override
    public Car getObject() throws Exception {
        log.info("===== 调用CarFactory ======= ");
        return new Car();
    }

    @Override
    public Class<?> getObjectType() {
        return Car.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public static void main(String[] args) {
        System.out.println(FactoryBean.class.isAssignableFrom(CarFactory.class));
        System.out.println(FactoryBean.class.isInstance(new CarFactory()));
    }
}
