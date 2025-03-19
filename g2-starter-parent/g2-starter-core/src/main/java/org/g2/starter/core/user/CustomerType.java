package org.g2.starter.core.user;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-04
 */
public class CustomerType extends UserType {

    public static final String CUSTOMER_TYPE = "C";

    @Override
    public String userType() {
        return CUSTOMER_TYPE;
    }
}
