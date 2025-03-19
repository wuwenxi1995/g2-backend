package com.wwx.study.algorithms.code.贪心算法;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 会议时间安排
 *
 * <p>
 * 会议室同时只能安排一个会议，给定会议室开发时间，给定所有会议开始结算时间，
 * 请合理安排会议，最多能有几个会议被安排
 * </p>
 *
 * @author wuwenxi 2022-07-19
 */
public class BaseProgram {

    private static class Program {
        private Integer start;
        private Integer end;

        public Integer getStart() {
            return start;
        }

        public Integer getEnd() {
            return end;
        }
    }

    public static int baseProgram(Program[] programs, int timePoint) {
        Arrays.sort(programs, Comparator.comparing(Program::getEnd));
        int result = 0;
        for (Program program : programs) {
            if (timePoint <= program.start) {
                result++;
                timePoint = program.end;
            }
        }
        return result;
    }
}
