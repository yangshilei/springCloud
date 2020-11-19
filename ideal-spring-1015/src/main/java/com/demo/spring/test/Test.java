package com.demo.spring.test;


import org.springframework.util.StringUtils;

import java.io.*;

/**
 *
 */
public class Test {
    public static void main(String[] args)  {



        String filePath = "C:\\work\\ip\\";
        String fileName = "123.jpg";


        try {
            File file = new File(filePath);
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for(int i = 0; i<files.length;i++){
                    System.out.println(files[i].getName());
                }
            }
        }catch (Exception e){

        }finally {

        }

    }

}
