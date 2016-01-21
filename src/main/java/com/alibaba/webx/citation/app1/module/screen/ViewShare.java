/**
 * ICT NASC
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.alibaba.webx.citation.app1.module.screen;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.citrus.turbine.Context;

/**
 * 
 * @author xueye.duanxy
 * @version $Id: ViewShare.java, v 0.1 2016-1-21 下午4:58:19  Exp $
 */
public class ViewShare {

    /**执行指令*/
    private static final String BASH_BEDROOM_DUANXUEYE_TAS_CON_NASC_RUN_SH = "bash /usr/local/tomcat/citation/run.sh";

    /**
     * 
     * 
     * @param request
     * @param context
     */
    public void execute(HttpServletRequest request, Context context) {
        try {

            String titleStr = request.getParameter("title");
            String titleId = request.getParameter("titleId");
            String citationCount = "1";
            String authorCount = "1";
            List<String> shareList = new ArrayList<String>();
            List<String> nameList=new ArrayList<String>();
            //返回脚步输出信息
            List<String> outputList = getShOutStr(titleId);
            String outputResult = "";
            
            //StringList 转换成输出数据 Data List
            for(String line : outputList){
            	outputResult = outputResult + line;
            	/**
                if(line.startsWith("Citations")){
                    citationCount=line.split("/t")[1];
                }if(line.startsWith("Author")){
                    authorCount=line.split("/t")[1];
                }if(line.startsWith("Name")){
                	for(String item : line.split("/t")){
                		nameList.add(item);
                	}
                }
                else{
                    for(String item : line.split("/t")){
                    	shareList.add(item);
                    }
                }
                **/
            }
            context.put("outputResult", outputResult);
            context.put("title", titleStr);
            context.put("citationCount", citationCount);
            context.put("authorCount", authorCount);
            context.put("nameList", nameList);
            context.put("shareList", shareList);
        } catch (Exception e) {
            context.put("message", e.getMessage());
        }
    }

    /**
     * 获取运行代码数据
     * 
     * @param titleId
     * @return
     * @throws Exception 
     */
    private List<String> getShOutStr(String titleId) throws Exception {
        List<String> outputList = new ArrayList<String>();
        try {
            Process process = null;
            process = Runtime.getRuntime().exec(BASH_BEDROOM_DUANXUEYE_TAS_CON_NASC_RUN_SH + " " + titleId);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                process.getInputStream()));

            String line = "";
            while ((line = input.readLine()) != null) {
                outputList.add(line);
            }

            input.close();

        } catch (Exception e) {
            throw e;
        }
        return outputList;
    }

}
