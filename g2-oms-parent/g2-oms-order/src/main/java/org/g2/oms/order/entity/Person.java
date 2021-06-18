package org.g2.oms.order.entity;

/**
 * @author wenxi.wu@hand-chian.com 2020-12-10
 */
public class Person {

    private Long age;
    private String name;

    public Long getAge() {
        return age;
    }

    public Person setAge(Long age) {
        this.age = age;
        return this;
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
