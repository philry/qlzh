package com.sy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CountLine {

    protected File root;						// FileFinder的查找范围
    protected int lineCounter;
    // 行数计数器
    public void Dir(String rootname)			// 输入目录，从该目录开始查找
    {
        if (rootname.charAt(rootname.length()-1)!= File.separatorChar)
        {
            rootname = rootname+File.separator;	// 如果结尾不是分隔符，则添上分隔符
        }
        root = new File(rootname);				// 初始化root对象
    }

    public void countLine()						// 统计根目录下所有.java文件的总行数
    {
        lineCounter = 0;						// 行数计数器归零
        recurrent(root);						// 列出根目录下逐级子目录，如果是.java则统计行数
    }
    public int getLines()						// 获取行数统计结果
    {
        return lineCounter;
    }

    protected void recurrent(File root)			// 输入根目录，递归输出根目录下各级子目录及其文件
    {
        if (root != null)						// 如果目录非空
        {
            String sRoot = root.getAbsolutePath();	// root文件的绝对路径
            String[] tmp = sRoot.split("\\\\");		// 用正则表达式分开各级路径
            // getAbsolutePath方法中文件路径字符串都是用\\表示\的,\\的转义字符就是\\\\
            String filename = tmp[tmp.length-1];	// 取最后一级的文件名
            if (root.isDirectory())					// 如果根目录是文件夹
            {
                File[] fileArray = root.listFiles();// 用数组fileArray保存根目录下所有文件/文件夹
                for (File fa : fileArray)			// 对于所有子文件/子目录
                {
                    recurrent(fa);					// 递归调用recurrent方法
                }
            }
            else									// 如果根目录是文件
            {
                if (filename.contains(".java")||filename.contains(".xml")||filename.contains(".css")||filename.contains(".html")||filename.contains(".js"))
                {
                    try {
                        BufferedReader fin = new BufferedReader(new FileReader(root));
                        while (fin.readLine() != null)		// 如果不是行尾
                        {
                            lineCounter++;
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                System.out.println(filename);		// 输出文件名
            }
        }
    }
}
