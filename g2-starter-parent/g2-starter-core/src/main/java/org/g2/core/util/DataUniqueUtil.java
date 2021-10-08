package org.g2.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据处理工具类
 *
 * @author wuwenxi 2021-09-28
 */
public class DataUniqueUtil {

    public <K, T> Map<K, T> filter(List<T> operationDataList, DataUniqueHandler<K, T> handler) {
        Map<K, T> result = new HashMap<>(16);
        if (operationDataList != null && operationDataList.size() > 1) {
            Map<K, List<T>> groupMap = operationDataList.stream().collect(Collectors.groupingBy(handler::mapKey));
            groupMap.forEach((key, value) -> {
                if (value.size() <= 1) {
                    result.put(key, value.get(0));
                    return;
                }
                // 选择需要的数据
                T choose = handler.choose(value);
                result.put(key, choose);
                // 对不需要对数据做处理
                value.remove(choose);
                handler.abandonData(value);
            });
        }
        return result;
    }


    public interface DataUniqueHandler<K, T> {
        /**
         * 返回需要分组的健
         *
         * @param value 值
         * @return 健
         */
        K mapKey(T value);

        /**
         * 在分组数据中选择一个需要对数据
         *
         * @param operationData 分组数据
         * @return 选择
         */
        T choose(List<T> operationData);

        /**
         * 对无用的数据做处理
         *
         * @param abandonVar 无用数据
         */
        void abandonData(List<T> abandonVar);
    }

}
