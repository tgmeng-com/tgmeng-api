package com.tgmeng.common.util;

import cn.hutool.core.io.resource.ResourceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.Striped;
import com.tgmeng.common.exception.ServerException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class FileUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Striped<Lock> FILE_LOCKS = Striped.lock(1024);

    /** 读取类路径下的json文件（推荐） */
    public static String readFileToStringFromClasspath(String path) {
        return ResourceUtil.readUtf8Str(path);   // hutool 方式，最简
    }

    /** 读取任意路径的json文件 */
    public static String readFileToStringFromAbsolutePath(String absolutePath) {
        try {
            return Files.readString(Paths.get(absolutePath));
        } catch (Exception e) {
            throw new ServerException("读取json文件失败，路径:" + absolutePath + " 异常信息:" + e);
        }
    }

    // 检查文件夹是否存在并创建
    public static void checkDirExitAndMake(String subscriptionDir) {
        File dir = new File(subscriptionDir);
        if (!dir.exists()) dir.mkdirs(); // 启动时自动创建
    }

    // 初始化订阅文件并写入内容
    public static void createFileAndWriteInitContent(String filePath, String fileName, String content) {
        checkDirExitAndMake(filePath);
        String fullPath = filePath + fileName;
        try (FileWriter writer = new FileWriter(fullPath)) {
            if (content != null) {
                writer.write(content);
            }
        } catch (Exception e) {
            throw new ServerException("创建文件失败，文件名:" + fullPath + " 异常信息:" + e);
        }
    }

    // 获取指定路径下所有文件名
    public static List<String> getAllFileNamesInPath(String directoryPath) {
        List<String> fileNames = new ArrayList<>();
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            return fileNames;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }
        return fileNames;
    }

    // 获取指定路径下所有文件
    public static File[] getAllFilesInPath(String directoryPath) {
        File dir = new File(directoryPath);
        File[] subscriptionFiles = dir.listFiles();
        if (subscriptionFiles != null && subscriptionFiles.length > 0) {
            return subscriptionFiles;
        } else {
            throw new ServerException("路径下没有文件:" + directoryPath);
        }
    }

    // 递归删除文件夹及其中的所有文件
    public static boolean deleteFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFolder(file);  // 递归删除文件
                }
            }
        }
        return folder.delete();  // 删除空的文件夹
    }

    public static void writeToFile(File file, Object data) throws IOException {
        String filePath = file.getAbsolutePath();
        Lock lock = FILE_LOCKS.get(filePath); // 通过 hash 映射到某一把锁

        lock.lock();
        try {
            MAPPER.writeValue(file, data);
        } finally {
            lock.unlock();
        }
    }
}
