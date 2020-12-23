package com.demo.sftp.sftp;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * 文件工具类
 * @author xiaobei
 * update meilon
 */
@Slf4j
public class FileUtil {

    /**
     * 默认文件分隔符
     * windows中分隔符有所不同, 但也能识别因此作为默认分隔符使用
     */
    final static public String DEF_LINE_SEPARATOR= "/";
    /**
     * windows 默认文件分隔符, 在linux系统中不能使用
     */
    final static public String WIN_LINE_SEPARATOR = "\\";
    /**
     * 获取当前系统默认换行符
     */
    final static public String LINE_SEPARATOR = System.getProperty("line.separator", "/n");
    /**
     * 当前系统默认文件分隔符
     */
    final static public String FILE_SEPARATOR = System.getProperty("file.separator", DEF_LINE_SEPARATOR);


    /**
     * 判断多级路径是否存在，不存在就创建
     * 如果存在则再判断是文件还是目录
     * @param path 目标路径
     * @return true 目录已存在, 或创建成功, false: 目标为文件, 或创建目录失败;
     */
    public static boolean isExistDir(String path) {
        boolean res = false;
        File file = null;
        try {
            file = new File(path);
        }catch (Exception e){
            log.error("路径格式错误!");
            return false;
        }
        int exist = isExist(file);
        if (exist < 0){
            return createDir(path);
        }else if (exist == 0){
            res = true;
        }
        return res;
    }
    /**
     * 判断目标路径类型
     * @param path 目标路径
     * @return  -1 : 目标不存在, 0: 目标为目录, 1: 目标为文件
     */
    public static int isExist(String path){
        File file = null;
        try {
            file = new File(path);
        }catch (Exception e){
            log.error("路径格式错误!");
            return -1;
        }
        return isExist(file);

    }
    /**
     * 判断目标对象类型
     * @param file 目标对象
     * @return  -1 : 目标不存在, 0: 目标为目录, 1: 目标为文件
     */
    public static int isExist(File file){
        int res = -1;
        if (file.exists()){
            if (file.isFile()){
                res = 1;
            }
            if (file.isDirectory()){
                res = 0;
            }
        }
        return res;
    }
    /**
     * 创建目录, 如果目标已经存在则直接返回true
     * 创建失败返回false
     * @param path 目标路径
     * @return true 成功, false 失败
     */
    public static boolean createDir(String path){

        File file = null;
        try {
            file = new File(path);

        }catch (Exception e){
            log.error("创建路目录错误!");
            return false;
        }
        return createDir(file);
    }
    /**
     * 创建目录, 如果目标已经存在则直接返回true
     * 创建失败返回false
     * @param file 目标路径
     * @return true 成功, false 失败
     */
    private static boolean createDir(File file){
        boolean res = false;
        try {
            int isF = isExist(file);
            if (isF < 0){
                // mkdirs可以创建多级目录
                res = file.mkdirs();
            }
            if (isF == 0){
                res = true;
            }
        }catch (Exception e){
            log.error("创建路目录错误!");
        }
        return res;
    }
    /**
     * 用于拼接路径和文件名
     *
     * @param filePaths
     * @return 完整路径
     */
    public static String unite(String... filePaths){

        return unite(true, filePaths);

//        if (null == filePath || filePath.isEmpty()){
//            return fileName;
//        }
//        String wei = filePath.substring(filePath.length() - 1);
//        if (DEF_LINE_SEPARATOR.equals(wei) || WIN_LINE_SEPARATOR.equals(wei)){
//            return filePath + fileName;
//        }
//        return filePath + LINE_SEPARATOR + fileName;
    }
    /**
     * 拼接文件路径或目录路径
     * isFilePath 为 true时指定为拼接文件路径, 此时路径最后不会拼接文件分隔符
     * isFilePath 为 false时指定为拼接目录路径, 此时路径最后会拼接文件分隔符
     * 注: 目录名不能为null, 否则会忽略此目录
     * @param isFilePath
     * @param dirs
     * @return 完整路径
     */
    public static String unite(boolean isFilePath, String... dirs){
        if (null == dirs || dirs.length == 0){
            return null;
        }
        StringBuilder pathStr = new StringBuilder();
        for (int i = 0; i < dirs.length; i++) {
            String dir = dirs[i];
            if (!StringUtils.isBlank(dir)){
                pathStr.append(dir);
                String wei = dir.substring(dir.length() - 1);
                // 判断目录名最后一位是否已经有文件分隔符, 没有则拼接分隔符
                if (!DEF_LINE_SEPARATOR.equals(wei) && !WIN_LINE_SEPARATOR.equals(wei)){
                    if (i == dirs.length - 1 && isFilePath){
                        continue;
                    }
                    pathStr.append(DEF_LINE_SEPARATOR);
                }
            }
        }
        return pathStr.toString();
    }
    /**
     * 解压 gz文件
     * 解压到 文件所在目录
     * @param gzFilePath 要解压的文件所在目录
     * @param fileName 文件名
     * @return 解压成功返回解压后的文件名, 失败返回null
     */
    public static String gzip(String gzFilePath, String fileName){
        return gzip(gzFilePath, fileName, gzFilePath);
    }
    /**
     * 解压gz文件
     * @param gzFilePath 要解压的文件所在目录
     * @param gzFileName 文件名
     * @param outPath 解压输出的目录
     * @return 解压成功返回文件名, 失败返回null
     */
    private static String gzip(String gzFilePath, String gzFileName, String outPath) {

        if (StringUtils.isEmpty(gzFileName)){
            log.error("解压Gz文件错误, 不存在的文件");
            return null;
        }
        int index = gzFileName.lastIndexOf('.');
        String postfix = gzFileName.substring(index + 1);

        if ("gz".equalsIgnoreCase(postfix)){

            InputStream in = null;
            GZIPInputStream gzin = null;
            FileOutputStream fout = null;
            String filePath = unite(gzFilePath, gzFileName);

            try {

                in = new FileInputStream(filePath);
                String fileName = gzFileName.substring(0, index);
                if (gzip(in, outPath, fileName)){
                    return fileName;
                }
            } catch (FileNotFoundException e) {
                log.error("文件: {} 解压错误, 文件不存在", filePath);
            } catch (Exception e){
                log.error("文件: {} 解压错误: {}", filePath, e.getMessage());
            }
        }else {
            log.error("文件: {} 解压出错, 不是可解压的格式", gzFileName);
        }
        return null;
    }
    /**
     * 解压缩gz文件
     * @param inputStream 文件流
     * @param outPath 解压输出的目录
     * @param outFileName 要输出的文件名
     * @return 解压是否成功
     */
    private static boolean gzip(InputStream inputStream, String outPath, String outFileName) {

        boolean res = false;
        String outputFile = null;
        GZIPInputStream gzin = null;
        FileOutputStream fout = null;

        try {
            // 建立gzip解压工作流
            gzin = new GZIPInputStream(inputStream);

            outputFile = unite(outPath, outFileName);
            if (isExistDir(outPath)){

                fout = new FileOutputStream(outputFile);
                int num;
                byte[] buf = new byte[1024 * 1024];
                while ((num = gzin.read(buf, 0, buf.length)) != -1) {
                    fout.write(buf, 0, num);
                }
                res = true;
            }
        } catch (Exception e) {
            log.error("解压数据错误: {}", e.getMessage());
        }finally {
            try {
                if (null != inputStream){
                    inputStream.close();
                }
                if (null != gzin){
                    gzin.close();
                }
                if (null != fout){
                    fout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return res;
    }

    /**
     * inputStreeam转string格式
     *
     * @param input InputStream
     * @return string
     */
    public static String inputStreamToString(InputStream input) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bf = new BufferedReader(new InputStreamReader(input));
        try {

            String line = null;

            while ((line = bf.readLine()) != null) {
                line = line.trim().replace(" ","");
                stringBuilder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }



}
