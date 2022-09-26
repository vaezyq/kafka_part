package com.example.kafka_test.utils;

import java.io.*;
import java.util.List;

public class JsonUtils {

    /**
     * 读入Json文件并存储为字符串
     * @param pactFile
     * @return
     * @throws IOException
     */
    public static String readJsonData(String pactFile) throws IOException{
        StringBuffer stringBuffer = new StringBuffer();
        File jsonFile = new File(pactFile);
        if(!jsonFile.exists()){
            System.err.println("Can't Find "+pactFile);
        }
        try{
            FileInputStream fis = new FileInputStream(pactFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fis,"UTF-8");
            BufferedReader in = new BufferedReader(inputStreamReader);

            String str;
            while((str = in.readLine())!=null){
                stringBuffer.append(str);
            }
            in.close();
        }catch (IOException e){
            e.getStackTrace();
        }
        return stringBuffer.toString();
    }
}
