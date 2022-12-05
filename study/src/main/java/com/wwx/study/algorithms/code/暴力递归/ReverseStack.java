package com.wwx.study.algorithms.code.暴力递归;

import java.util.LinkedList;

/**
 * 栈逆序
 * <p>
 * 使用递归
 * </p>
 *
 * @author wuwenxi 2022-07-25
 */
public class ReverseStack {

    public void reverse(LinkedList<Integer> stack) {
        if (stack.isEmpty()
                || stack.size() == 1) {
            return;
        }
        int last = fun(stack);
        reverse(stack);
        stack.push(last);
    }

    private int fun(LinkedList<Integer> stack) {
        Integer last = stack.pop();
        if (stack.isEmpty()) {
            return last;
        }
        int result = fun(stack);
        stack.push(last);
        return result;
    }
}
