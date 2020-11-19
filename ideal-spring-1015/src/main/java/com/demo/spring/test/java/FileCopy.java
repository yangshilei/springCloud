package com.demo.spring.test.java;


import java.io.*;

/**
 *
 */
public class FileCopy {

    public static void main(String[] args)  {

        String filePath = "C:\\work\\ip\\";
        String fileName = "123.jpg";
        String newName = "222.jpg";

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            File file = new File(filePath+fileName);
            inputStream = new FileInputStream(file);
            System.out.println(file.isDirectory());
            File file2 = new File(filePath+newName);
            outputStream = new FileOutputStream(file2);


            int i = 0;
            if(file.exists()){
                System.out.println("文件存在");
                byte[] bytes = new byte[1024];
                while (inputStream.read(bytes) != -1){
                    outputStream.write(bytes);
                    inputStream.read(bytes);
                    i++;
                }
            }
            System.out.println("循环次数="+i);
        }catch (Exception e){

        }finally {
            try {
                if(null != inputStream){
                    inputStream.close();
                }
                if(null != outputStream){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
