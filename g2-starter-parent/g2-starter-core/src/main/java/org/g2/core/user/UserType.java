package org.g2.core.user;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-04
 */
public abstract class UserType {

    private static final String PLATFORM_TYPE = "P";
    private static final Map<String, UserType> USER_TYPE = new ConcurrentHashMap<>(16);

    public UserType() {
        USER_TYPE.put(userType(), this);
    }

    public static UserType ofDefault() {
        return USER_TYPE.get(PLATFORM_TYPE);
    }

    public static UserType ofDefault(String userType) {
        //
        if (StringUtils.isBlank(userType)) {
            // 从token获取用户信息
        }
        if (StringUtils.isBlank(userType)) {
            return ofDefault();
        }
        if (!USER_TYPE.containsKey(userType)) {
            UserType newUserType = new UserType() {
                @Override
                public String userType() {
                    return userType;
                }
            };
            USER_TYPE.put(userType, newUserType);
        }
        return USER_TYPE.get(userType);
    }

    /**
     * 获取用户类型
     *
     * @return 用户类型
     */
    public abstract String userType();

}
