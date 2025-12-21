package com.tgmeng.common.util;

import com.google.common.hash.Hashing;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
@Slf4j
public class SimHashUtil {

    private static final int HASH_BITS = 64;
    private static final int HAMMING_THRESHOLD = 4;
    private static final com.google.common.hash.HashFunction MURMUR = Hashing.murmur3_128();

    // 停用词集合
    private static final Set<String> STOP_WORDS = Set.of(
            "的", "了", "在", "是", "我", "有", "和", "就", "不", "人",
            "都", "一", "一个", "上", "也", "很", "到", "说", "要", "去",
            "你", "会", "着", "没有", "看", "好", "自己", "这"
    );

    /**
     * 第一步: 标题规范化
     * - 转小写
     * - 去除特殊字符和标点
     * - 统一空格
     * - 去除多余空白
     */
    private static String normalizeTitle(String title) {
        if (title == null || title.isEmpty()) {
            return "";
        }
        // 转小写
        String normalized = title.toLowerCase();
        // 去除HTML标签
        normalized = normalized.replaceAll("<[^>]*>", "");
        // 3. 去掉常见中文/英文标点符号
        normalized = normalized.replaceAll("[【】\\[\\]():：]", " ");
        // 保留中文、英文、数字,去除其他特殊字符
        normalized = normalized.replaceAll("[^\\u4e00-\\u9fa5a-z0-9\\s]", " ");
        // 统一多个空格为一个
        normalized = normalized.replaceAll("\\s+", " ").trim();
        return normalized;
    }

    /**
     * 第二步: HanLP 分词并过滤
     */
    private static List<HanLPUtil.Token> segmentAndFilter(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return HanLPUtil.tokenize(text);
    }

    /**
     * 第三步: 计算 64-bit SimHash
     */
    public static long calculateSimHash(String title) {
        // 1. 规范化
        String normalized = normalizeTitle(title);
        // 2. 分词
        List<HanLPUtil.Token> tokens = segmentAndFilter(normalized);
        if (tokens.isEmpty()) {
            return 0L;
        }
        // 3. 初始化64位向量
        int[] vector = new int[HASH_BITS];
        // 4. 对每个词计算hash并累加到向量
        for (HanLPUtil.Token token : tokens) {
            long hash = MURMUR.hashString(token.word(), StandardCharsets.UTF_8).asLong();
            int weight = token.weight();
            // 按位处理
            for (int i = 0; i < HASH_BITS; i++) {
                if (((hash >> i) & 1) == 1) {
                    vector[i] += weight;
                } else {
                    vector[i] -= weight;
                }
            }
        }
        // 5. 降维: 将向量转换为64位指纹
        long fingerprint = 0L;
        for (int i = 0; i < HASH_BITS; i++) {
            if (vector[i] > 0) {
                fingerprint |= (1L << i);
            }
        }
        return fingerprint;
    }

    /**
     * 第四步: 计算汉明距离
     */
    public static int hammingDistance(long simHashValue1, long simHashValue2) {
        // XOR 找出不同的位
        long xor = simHashValue1 ^ simHashValue2;
        // 计算1的个数 (Java内置方法)
        return Long.bitCount(xor);
    }

    /**
     * 判断两个标题是否相似 (汉明距离 ≤ 4)
     */
    public static boolean isSimilar(long simHashValue1, long simHashValue2) {
        return hammingDistance(simHashValue1, simHashValue2) <= HAMMING_THRESHOLD;
    }

    /**
     * 判断两个标题是否相似 (自定义阈值)
     */
    public static boolean isSimilar(long simHashValue1, long simHashValue2, int threshold) {
        return hammingDistance(simHashValue1, simHashValue2) <= threshold;
    }


    /**
     * 测试示例
     */
    public static void main(String[] args) {
        System.out.println("=== 中文标题 SimHash 去重测试 ===\n");

        // 测试1: 基础相似度检测
        //String a1 = "【重要通知】关于2024年春节放假安排的通知";
        //String a2 = "重要通知：2024年春节放假安排";
        //String a3 = "2024年春节放假安排通知";
        //String a4 = "春节放假安排公布，2024年春节放假通知";
        //
        //String b1 = "2024年春节放假安排通知";
        //String b2 = "2024年春节调休安排公布";
        //String b3 = "2024年春节高速免费时间公布";
        //
        //String c1 = "苹果发布iPhone16，售价7999元起";
        //String c2 = "国务院发布最新房地产调控政策";
        //String c3 = "比特币价格突破80000美元再创新高";

        String d1 = "雷军回应小米汽车事故：安全永远是第一位";
        String d2 = "小米汽车事故雷军发声：安全问题不容忽视";
        String d3 = "雷军谈小米汽车事故，回应安全质疑";

        String e1 = "炸了！雷军首次回应小米汽车事故";
        String e2 = "雷军回应小米汽车事故";
        String e3 = "小米汽车出事了？雷军最新回应";



        List<String> titles = List.of(
                //a1,a2,a3,a4,
                //b1,b2,b3,
                //c1,c2,c3,
                d1,d2,d3,
                e1,e2,e3
        );

        for (int i = 0; i < titles.size(); i++) {
            for (int j = i + 1; j < titles.size(); j++) {
                int dist = hammingDistance(
                        calculateSimHash(titles.get(i)),
                        calculateSimHash(titles.get(j))
                );
                log.info("汉明距离：{}，标题1：{}  标题2：{}",dist,titles.get(i),titles.get(j));
            }
        }


    }
}
