package com.tgmeng.common.bean;

import cn.hutool.http.HttpStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * description: 返回结果集封装
 *  *                      return ResultTeamplateBean.success("111",map);
 *  *                      return ResultTeamplateBean.error("失败了");
 * package: com.tgmeng.common.bean
 * className: ResultTemplateBean
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/7/1 10:27
*/
@Data
@Accessors(chain = true)
public class ResultTemplateBean<T> implements Serializable {
    private static final long serialVersionUID = -8143566284567789026L;
    /** 状态码(不是HTTP的状态，只是一种信息提示) */
    private Integer code;
    /** 信息提示 */
    private String message;
    /** 内容 */
    private T data;

    /**
     * @description 成功返回调用方法
     * @method success
     * @param data 1
     * @return com.ma.githubauto.common.bean.ResultTeamplateBean<T>
     *
     * @author xiaoma
     * @date 2023/2/13 13:01
     * @version V1.0
     */
    public static <T> ResultTemplateBean<T> success(T data) {
        return new ResultTemplateBean<T>().setCode(HttpStatus.HTTP_OK).setMessage("请求成功").setData(data);
    }

    /**
     * description: 成功返回调用方法
     * method: success
     *
     * @author tgmeng
     * @since 2025/7/1 11:36
    */
    public static <T> ResultTemplateBean<T> success(String message, T data) {
        return new ResultTemplateBean<T>().setCode(HttpStatus.HTTP_OK).setMessage(message).setData(data);
    }

    public static <T> ResultTemplateBean<T> success(Integer code, String message, T data) {
        return new ResultTemplateBean<T>().setCode(code).setMessage(message).setData(data);
    }
}
