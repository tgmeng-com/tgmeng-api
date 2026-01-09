package com.tgmeng.common.util;

import cn.hutool.core.util.StrUtil;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.tgmeng.common.config.RequestInfoManager;
import com.tgmeng.model.vo.topsearch.TopSearchCommonVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonRssUtil {

    public static List<TopSearchCommonVO.DataInfo> getCommonResult(String content, RequestInfoManager.PlatformConfig platform) {
        List<TopSearchCommonVO.DataInfo> topSearchCommonVOS = new ArrayList<>();
        try {
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new StringReader(content));

            for (int i = 0; i < feed.getEntries().size(); i++) {
                SyndEntry syndEntry = feed.getEntries().get(i);
                String title = "";           // 标题
                String url = "";             // 链接
                String description = "";     // 描述/摘要
                String pubDate = "";           // 发布时间
                String author = "";          // 作者
                String guid = "";            // 唯一标识符
                String imageUrl = "";        // 图片URL（封面图）
                String comments = "";        // 评论链接
                String contentRss = "";         // 完整内容（如果有）

                if (syndEntry.getTitle() != null) {
                    title = syndEntry.getTitle();
                }
                if (syndEntry.getLink() != null) {
                    url = syndEntry.getLink();
                }
                if (syndEntry.getUri() != null) {
                    guid = syndEntry.getUri();
                }
                if (syndEntry.getPublishedDate() != null) {
                    pubDate = outputFormat.format(syndEntry.getPublishedDate());
                }
                if (syndEntry.getDescription() != null) {
                    description = syndEntry.getDescription().getValue();
                }
                // 作者
                if (syndEntry.getAuthor() != null) {
                    author = syndEntry.getAuthor();
                }
                if (syndEntry.getContents() != null && !syndEntry.getContents().isEmpty()) {
                    contentRss = syndEntry.getContents().getFirst().getValue();
                }
                if (syndEntry.getEnclosures() != null && !syndEntry.getEnclosures().isEmpty()) {
                    imageUrl = syndEntry.getEnclosures().getFirst().getUrl();
                }
                if (syndEntry.getComments() != null) {
                    comments = syndEntry.getComments();
                }
                if (StrUtil.isBlank(url)){
                    url = "https://tgmeng.com";
                }
                if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                    topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, imageUrl, author, description, "", pubDate, "", null, null, null, i + 1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        topSearchCommonVOS.sort(Comparator.comparingInt(TopSearchCommonVO.DataInfo::getSort));
        return topSearchCommonVOS;
    }
}
