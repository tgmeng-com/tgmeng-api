package com.tgmeng.controller.topsearch;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.translation.manager.TranslationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/topsearch/translation")
public class TopSearchTranslationController {

    private final TranslationManager translationManager;

    // TODO 测试翻译用的，由于目前翻译额度不够，所以先不用，以后用的时候再弄，这里是一个使用demo
    @RequestMapping("/language/test")
    public ResultTemplateBean getLanguageTest() {
        String japaneseText = "こんにちは、世界";
        String translate = translationManager.translate(japaneseText, "zh");

        List<String> japaneseTitles = Arrays.asList(
                "今日のトップニュース",
                "テクノロジーの最前線",
                "エンターテインメントニュース",
                "スポーツ速報",
                "経済市場分析"
        );
        // 批量翻译：日文 → 中文
        List<String> chineseResults = translationManager.batchTranslate(japaneseTitles, "zh");
        return null;
    }



}
