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

            for (SyndEntry entry : feed.getEntries()) {
                String title = "";           // 标题
                String url = "";             // 链接
                String description = "";     // 描述/摘要
                String pubDate = "";           // 发布时间
                String author = "";          // 作者
                String guid = "";            // 唯一标识符
                String imageUrl = "";        // 图片URL（封面图）
                String comments = "";        // 评论链接
                String contentRss = "";         // 完整内容（如果有）

                if (entry.getTitle() != null) {
                    title = entry.getTitle();
                }
                if (entry.getLink() != null) {
                    url = entry.getLink();
                }
                if (entry.getUri() != null) {
                    guid = entry.getUri();
                }
                if (entry.getPublishedDate() != null) {
                    pubDate = outputFormat.format(entry.getPublishedDate());
                }
                if (entry.getDescription() != null) {
                    description = entry.getDescription().getValue();
                }
                // 作者
                if (entry.getAuthor() != null) {
                    author = entry.getAuthor();
                }
                if (entry.getContents() != null && !entry.getContents().isEmpty()) {
                    contentRss = entry.getContents().getFirst().getValue();
                }
                if (entry.getEnclosures() != null && !entry.getEnclosures().isEmpty()) {
                    imageUrl = entry.getEnclosures().getFirst().getUrl();
                }
                if (entry.getComments() != null) {
                    comments = entry.getComments();
                }
                if (StrUtil.isNotBlank(title) && StrUtil.isNotBlank(url)) {
                    topSearchCommonVOS.add(new TopSearchCommonVO.DataInfo(title, "", url, imageUrl, author, description, "", pubDate, "", null, null, null));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topSearchCommonVOS;
    }
}
