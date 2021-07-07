package org.g2.core.chain;

import org.springframework.core.Ordered;

/**
 * @author wuwenxi 2021-07-07
 */
public interface Chain extends Ordered {

    /**
     * 调用链执行优先级
     *
     * @return 调用链执行优先级
     */
    @Override
    default int getOrder() {
        return 0;
    }
}
