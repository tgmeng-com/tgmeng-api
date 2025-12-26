package com.tgmeng.common.util;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgmeng.common.bean.LicenseBean;
import com.tgmeng.common.bean.SubscriptionBean;
import com.tgmeng.common.enums.business.LicenseFeatureEnum;
import com.tgmeng.common.enums.business.LicenseStatusEnum;
import com.tgmeng.common.enums.exception.ServerExceptionEnum;
import com.tgmeng.common.exception.ServerException;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.tgmeng.common.util.StringUtil.generateRandomFileName;

@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Value("${my-config.license.dir}")
    private String licenseDir;

    @Value("${my-config.subscription.dir}")
    private String subscriptionDir;

    @Value("${my-config.log.license.dir}")
    private String logLicenseDir;

    @Value("${my-config.log.license.max-log-size}")
    private long maxLogSize;

    @Value("${my-config.log.license.keep-log-size}")
    private long keepLogSize;

    private final Map<String, Lock> FILE_LOCKS = new ConcurrentHashMap<>();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 加载授权码
    public LicenseBean loadLicense(String licenseCode) {
        try {
            return MAPPER.readValue(new File(getLicenseFilePathByCode(licenseCode)), LicenseBean.class);
        } catch (Exception e) {
            throw new ServerException(ServerExceptionEnum.LICENSE_CHECK_EXCEPTION, "授权码无效", null);
        }
    }

    // 检查权限
    public void checkFeatures(LicenseBean licenseBean, LicenseFeatureEnum feature) {
        if (!licenseBean.getFeatures().contains(feature)) {
            throw new ServerException(ServerExceptionEnum.LICENSE_CHECK_EXCEPTION, "无此功能授权", null);
        }
    }

    // 检查过期
    public void checkExpire(LicenseBean licenseBean) {
        String currentTime = TimeUtil.getCurrentTimeFormat(TimeUtil.defaultPattern);
        if (TimeUtil.isAfter(currentTime, licenseBean.getExpireTime())) {
            throw new ServerException(ServerExceptionEnum.LICENSE_CHECK_EXCEPTION, "已过期", null);
        }
    }

    // 检查状态
    public void checkStatus(LicenseBean licenseBean) {
        LicenseStatusEnum status = licenseBean.getStatus();
        if (status == LicenseStatusEnum.DISABLED) {
            throw new ServerException(ServerExceptionEnum.LICENSE_CHECK_EXCEPTION, "已禁用", null);
        }
        if (status == LicenseStatusEnum.EXPIRED) {
            throw new ServerException(ServerExceptionEnum.LICENSE_CHECK_EXCEPTION, "已过期", null);
        }
    }

    // 检查绑定机器数
    public void checkBoundMachineCount(LicenseBean licenseBean, String machineId) {
        try {
            if (StrUtil.isBlank(machineId)) {
                throw new ServerException(ServerExceptionEnum.LICENSE_CHECK_EXCEPTION, "机器码无效", null);
            }
            if (!licenseBean.getMachineIds().contains(machineId)) {
                if (licenseBean.getMachineIds().size() >= licenseBean.getMaxMachines()) {
                    throw new ServerException(ServerExceptionEnum.LICENSE_CHECK_EXCEPTION, "绑定机器已达上限", null);
                } else {
                    // 绑定机器
                    licenseBean.getMachineIds().add(machineId);
                    FileUtil.writeToFile(new File(getLicenseFilePathByCode(licenseBean.getLicenseCode())), licenseBean);
                }
            }
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    // 根据授权码获取授权文件路径
    public String getLicenseFilePathByCode(String licenseCode) {
        return licenseDir + licenseCode + StringUtil.LicenseCodeFileExtension;
    }

    // TODO 生成LicenseCode文件，这个只有管理员用，就他妈是我用，这傻逼代码，谁也不用，就他妈给老子用，艹，写麻了把人都
    public List<String> initLicenseFile(Integer count, String expireTime, List<LicenseFeatureEnum> features) {
        log.info("开始创建初始化LicenseCode文件，数量为：" + count);
        List<String> newFileList = new ArrayList<>();
        Set<String> allFileNamesInPath = new LinkedHashSet<>(FileUtil.getAllFileNamesInPath(licenseDir));
        try {
            String licenseCodeInitTemplate = FileUtil.readFileToStringFromClasspath("template/LicenseCodeInitTemplate.json");
            String subscriptionInitTemplate = FileUtil.readFileToStringFromClasspath("template/SubscriptionInitTemplate.json");
            int createdSuccess = 0;
            while (createdSuccess < count) {
                String fileName = generateRandomFileName();
                // 创建密钥文件
                createLicenseFile(fileName, allFileNamesInPath, licenseCodeInitTemplate, expireTime, features);
                // 创建订阅历史推送记录文件
                createSubscriptionFile(fileName, allFileNamesInPath, subscriptionInitTemplate);
                // 创建密钥使用记录日志文件
                createLogLicenseFile(fileName, allFileNamesInPath);
                newFileList.add(fileName);
                createdSuccess++;
                log.info("第{}个文件创建成功：{}", createdSuccess, fileName);
            }
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
        log.info("所有文件创建成功，共{}个", newFileList.size());
        return newFileList;
    }

    private void createLicenseFile(String fileName, Set<String> allFileNamesInPath, String licenseCodeInitTemplate, String expireTime, List<LicenseFeatureEnum> features) throws Exception {
        fileName = fileName + StringUtil.LicenseCodeFileExtension;
        if (!allFileNamesInPath.contains(fileName)) {
            LicenseBean licenseBean = MAPPER.readValue(licenseCodeInitTemplate, LicenseBean.class);
            licenseBean.setLicenseCode(fileName.split("\\.")[0]);
            licenseBean.setExpireTime(expireTime);
            licenseBean.setFeatures(features);
            // 创建文件并写入内容
            FileUtil.createFileAndWriteInitContent(licenseDir, fileName, MAPPER.writeValueAsString(licenseBean));
        }
    }

    private void createSubscriptionFile(String fileName, Set<String> allFileNamesInPath, String subscriptionInitTemplate) throws Exception {
        fileName = fileName + StringUtil.SubscriptionFileExtension;
        if (!allFileNamesInPath.contains(fileName)) {
            SubscriptionBean subscriptionBean = MAPPER.readValue(subscriptionInitTemplate, SubscriptionBean.class);
            subscriptionBean.setLicenseCode(fileName.split("\\.")[0]);
            // 创建文件并写入内容
            FileUtil.createFileAndWriteInitContent(subscriptionDir, fileName, MAPPER.writeValueAsString(subscriptionBean));
        }
    }

    private void createLogLicenseFile(String fileName, Set<String> allFileNamesInPath) throws Exception {
        fileName = fileName + StringUtil.logFileExtension;
        if (!allFileNamesInPath.contains(fileName)) {
            // 创建文件并写入内容
            FileUtil.createFileAndWriteInitContent(logLicenseDir, fileName, null);
        }
    }

    // 记录使用日志
    public void log(String licenseCode, String machineId, LicenseFeatureEnum feature) {

        try {
            FileUtil.checkDirExitAndMake(logLicenseDir);
            File file = new File(logLicenseDir + licenseCode + StringUtil.logFileExtension);

            // 超过预定大小 → 裁剪，比如设置最大10M，超过后，删除开头的1M，保留后面的9M，然后接着写
            Lock lock = FILE_LOCKS.computeIfAbsent(file.getAbsolutePath(), k -> new ReentrantLock());
            lock.lock();
            try {
                if (file.exists() && file.length() >= maxLogSize) {
                    truncateHeadIfNeeded(file);
                }

                // 4. 写入日志（JSON 格式）
                String line = String.format(
                        "{\"time\":\"%s\",\"code\":\"%s\",\"machineId\":\"%s\",\"feature\":\"%s\"}%n",
                        LocalDateTime.now().format(DATE_TIME_FORMATTER),
                        licenseCode,
                        machineId,
                        feature.getDescription()
                );
                Files.writeString(
                        file.toPath(),
                        line,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND
                );
                log.debug("✅ License 使用日志已记录 - Code: {}, Feature: {}", licenseCode, feature.getDescription());
            } finally {
                lock.unlock();
            }
        } catch (IOException e) {
            // ✅ 记录异常日志，但不中断业务流程
            log.error("❌ License 日志写入失败 - Code: {}, MachineId: {}, Feature: {}",
                    licenseCode, machineId, feature.getDescription(), e);
        } catch (Exception e) {
            // ✅ 捕获其他异常
            log.error("❌ License 日志记录异常", e);
        }
    }

    private void truncateHeadIfNeeded(File file) throws IOException {
        if (!file.exists()) {
            return;
        }

        long size = file.length();
        if (size < maxLogSize) {
            return;
        }

        log.info("📝 日志文件超过限制 ({} bytes)，开始裁剪: {}", size, file.getName());

        // 计算保留的起始位置
        long start = Math.max(0, size - keepLogSize);
        byte[] buffer;

        // 1. 读取需要保留的部分
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(start);
            int length = (int) (size - start);
            buffer = new byte[length];
            raf.readFully(buffer);
        }

        // 2. 找到第一个完整行的起始位置
        int offset = findFirstLineBreak(buffer);

        // 3. 原子性地重写文件
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(0);  // 清空文件
            raf.write(buffer, offset, buffer.length - offset);
        }

        log.info("日志文件裁剪完成 - 原始: {} bytes, 裁剪后: {} bytes",
                size, buffer.length - offset);
    }

    private int findFirstLineBreak(byte[] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] == '\n') {
                return i + 1; // 从下一行开始
            }
        }
        log.warn("日志缓冲区中未找到换行符，保留全部内容");
        return 0; // 没找到换行符，保留全部
    }

    @PreDestroy
    public void cleanup() {
        log.info("🧹 清理 License 日志锁资源，共 {} 个", FILE_LOCKS.size());
        FILE_LOCKS.clear();
    }
}
