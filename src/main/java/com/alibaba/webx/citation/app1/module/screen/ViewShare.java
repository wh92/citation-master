/**
 * ICT NASC
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.alibaba.webx.citation.app1.module.screen;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.citrus.turbine.Context;

/**
 * 
 * @author xueye.duanxy
 * @version $Id: ViewShare.java, v 0.1 2016-1-21 4:58:19  Exp $
 */
public class ViewShare {

    /***/
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
            List<List<String>> shareList = new ArrayList();
            List<String> rawList=null;
            List<String> nameList=new ArrayList<String>();
            List<String> totalList=new ArrayList<String>();
            //
            List<String> outputList = getShOutStr(titleId);
            String outputResult = "";
            int authorC=0;
            //StringList Data List
            for(String line : outputList){           	
            	
                if(line.startsWith("Citations")){
                    citationCount=line.split("\t")[1];
                }else if(line.startsWith("Author")){
                    authorCount=line.split("\t")[1];
                    authorC=Integer.parseInt(authorCount);
                }else if(line.startsWith("Name")){
                	//line.replace("Name\t", "");
                	String[] nameLineList=line.split("\t");
                	for(int i=0;i<authorC;i++){
                		nameList.add(nameLineList[i+1]);
                	}
                }else if(line.startsWith("total")){
                	//line.replace("total\t","");
                	outputResult = outputResult + line;
                	String[] totalLineList=line.split("\t");
                	for(int j=0;j<authorC;j++){
                		totalList.add(totalLineList[j+1]);
                	}
                }else{
                	rawList=new ArrayList();
                    for(String item : line.split("\t")){
                    	rawList.add(item);
                    }
                    shareList.add(rawList);
                }
            }
            context.put("outputResult", outputResult);
            context.put("title", titleStr);
            context.put("citationCount", citationCount);
            context.put("authorCount", authorCount);
            context.put("nameList", nameList);
            context.put("shareList", shareList);
            context.put("totalList", totalList);
            dataMsg(context,outputList);
        } catch (Exception e) {
            context.put("message", e.getMessage());
        }
    }
    
    private void dataMsg(Context context, List<String> strList) throws NumberFormatException {
        int authorCnt = 0;
        List<String> nameList = new ArrayList<String>();
        List<JSONArray> yearDataList = new ArrayList<JSONArray>();
        List<JSONObject> nameJSList=new ArrayList<JSONObject>();
        for (String str : strList) {
            if (str.startsWith("Citations")) {
                continue;
            } else if (str.startsWith("Author")) {
                String[] countStr = str.split("\t");
                //authorCnt = Integer.parseInt(countStr);
                continue;
            } else if (str.startsWith("Name")) {
                JSONObject jso=null;
                String[] nameLineList=str.split("\t");
            	for(int i=1;i<nameLineList.length;i++){
            		jso=new JSONObject();
            		jso.put("name",nameLineList[i]);
            		nameJSList.add(jso);
            	}
            	continue;
            } else if(str.startsWith("total")){
            	continue;
            } else{
                //yearDataList.add(str);
                String[] yearData = str.split("\t");

                List<JSONObject> linedata = new ArrayList<JSONObject>();
                for (String data : yearData) {
                    JSONObject ob = new JSONObject();
                    ob.put("a", data);
                    linedata.add(ob);
                }
                JSONArray dataList = new JSONArray(linedata);
                yearDataList.add(dataList);
            }
        }
        JSONArray dataList = new JSONArray(yearDataList);
        context.put("dataList", dataList);
        context.put("nameJSList", nameJSList.toString());
        
    }


    /**
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
