package com.tgmeng.common.enums.business;

import com.tgmeng.common.enums.enumcommon.INameValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description:
 * package: com.tgmeng.common.Enum
 * className: ForestRequestHeaderOriginEnum
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/29 18:17
*/
@Getter
@AllArgsConstructor
public enum SearchTypeHuggingFaceEnum implements INameValueEnum<String,String> {
    SPACE_TRENDING_HUGGING_FACE("spacestrending", "spaces?sort=trending", "HuggingFace space热门", true,1),
    SPACE_LIKES_HUGGING_FACE("spaceslikes", "spaces?sort=likes", "HuggingFace space喜欢", true,2),
    MODELS_TRENDING_HUGGING_FACE("modelstrending", "models?sort=trending", "HuggingFace models热门", true,2),
    MODELS_LIKES_HUGGING_FACE("modelslikes", "models?sort=likes", "HuggingFace models喜欢", true,2),
    DATASETS_TRENDING_HUGGING_FACE("datasetstrending", "datasets?sort=trending", "HuggingFace datasets热门", true,2),
    DATASETS_LIKES_HUGGING_FACE("datasetslikes", "datasets?sort=likes", "HuggingFace datasets喜欢", true,2);

    private final String key;
    private final String value;
    private final String description;
    private final Boolean enabled;
    private final Integer sort;
}
