package com.tgmeng.common.util;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.tgmeng.common.enums.business.NatureRule;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HanLPUtil {
    /** 通用停用词（热点场景） */
    private static final Set<String> STOP_WORDS = Set.of(
            "的","了","在","是","我","有","和","就","不","人",
            "都","一","一个","上","也","很","到","说","要","去",
            "你","会","着","没有","看","好","自己","这"
    );

    /**
     * 词云专用
     * @param text
     * @return
     */
    public static List<String> tokenizeToWords(String text) {
        return tokenize(text).stream()
                .map(Token::word)
                .collect(Collectors.toList());
    }

    /**
     * 🔥 热点分词（核心方法）
     * 用于 simHash / 热点统计
     */
    public static List<Token> tokenize(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return HanLP.segment(text).stream()
                .filter(HanLPUtil::isMeaningful)
                .filter(term -> !STOP_WORDS.contains(term.word))
                .filter(term -> !term.word.isEmpty())
                .map(HanLPUtil::toToken)
                .collect(Collectors.toList());
    }

    /** 是否是有意义的词 */
    private static boolean isMeaningful(Term term) {
        if (term.nature == null) return false;
        // 只过滤长数字
        if (term.word.matches("[0-9]+") && term.word.length() > 3) return false;
        // 标点
        if (term.nature.toString().startsWith("w")) return false;
        return matchRule(term) != null;
    }

    /** 转成业务 Token */
    private static Token toToken(Term term) {
        return new Token(
                term.word,
                term.nature == null ? "" : term.nature.toString(),
                calculateWeight(term)
        );
    }

    /** 🔥 热点权重策略（关键） */
    private static int calculateWeight(Term term) {
        NatureRule rule = matchRule(term);
        return rule == null ? 1 : rule.getValue();
    }

    private static NatureRule matchRule(Term term) {
        if (term == null || term.nature == null) return null;
        String nature = term.nature.toString();
        for (NatureRule rule : NatureRule.values()) {
            if (nature.startsWith(rule.getKey())) {
                return rule;
            }
        }
        return null;
    }

    /** ================= Token 对象 ================= */

    public record Token(String word, String nature, int weight) {}
}
