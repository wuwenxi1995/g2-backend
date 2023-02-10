package org.g2.dynamic.jdbc;

import org.g2.dynamic.jdbc.context.DataRoutHolder;

/**
 * @author wuwenxi 2023-02-10
 */
public final class DataRoutHelper {

    public static <T> T hint(String dsKey, IConsumer<T> consumer) {
        try {
            DataRoutHolder.set(dsKey);
            return consumer.consume();
        } finally {
            DataRoutHolder.clear();
        }
    }

    @FunctionalInterface
    public interface IConsumer<T> {
        /**
         * consume
         *
         * @return result
         */
        T consume();
    }
}
