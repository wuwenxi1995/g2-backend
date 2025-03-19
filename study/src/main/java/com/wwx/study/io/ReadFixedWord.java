package com.wwx.study.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mingxing.li02@hand-china.com
 * @Date 2022/8/1 14:30
 **/
public class ReadFixedWord {

    public static void main(String[] args) throws Exception {
        int[] data = new int[]{27, 28, 29, 30, 31};
        List<String> urlList = new ArrayList<>();
//        urlList.add("/hpfm/v1/0/employees/employee/all");
//        urlList.add("/o2ot/v1/csg-order-pay/wx-paid");
//        urlList.add("/hwfp/v1/1/hr/employee/query");
//        urlList.add("/o2mkt/v1/promotion-bill/test/bill-export");
//        urlList.add("/o2mkt/v1/promotion-bill/list");
//        urlList.add("113.14.224.142");
        urlList.add("117.61.247.9");
        urlList.add("117.61.243.42");
        urlList.add("220.173.31.142");
        urlList.add("113.14.224.142");
        for (int datum : data) {
            // 写入
            File file = null;
            FileWriter fw = null;
            file = new File("/Users/wwx/Desktop/logWrite2022.7." + datum+".txt");
            fw = new FileWriter(file);
            // 读取
            File logFile = new File("/Users/wwx/Desktop/data2022.7." + datum);
            FileInputStream fis = new FileInputStream(logFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            Long totalLines = 0L;
            Long readLines = 0L;
            String line = null;
            while ((line = br.readLine()) != null) {
                totalLines = totalLines + 1;
                for (String url : urlList) {
                    if (line.contains(url)) {
                        fw.write(line + "\n");
                        readLines = readLines + 1;
                        break;
                    }
                }
            }
            br.close();
            fw.flush();
            fw.close();
            System.out.println("" + "总条数：" + totalLines + " 读取条数：" + readLines);
        }
    }
}
