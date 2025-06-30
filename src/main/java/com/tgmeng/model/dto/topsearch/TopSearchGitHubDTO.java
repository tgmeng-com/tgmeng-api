package com.tgmeng.model.dto.topsearch;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * description: GitHub热榜DTO
 * package: com.tgmeng.model.dto.topsearch
 * className: TopSearchGitHubDTO
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/30 2:37
 */
@Data
@Accessors(chain = true)
public class TopSearchGitHubDTO {
    @JSONField(name = "total_count")
    private Long totalCount;
    @JSONField(name = "incomplete_results")
    private String incompleteResults;

    private List<ItemDTO> items;

    @Data
    public static class ItemDTO {

        private Long id;

        @JSONField(name = "node_id")
        private String nodeId;

        private String name;

        @JSONField(name = "full_name")
        private String fullName;

        @JSONField(name = "private")
        private Boolean private_;

        private Owner owner;

        @JSONField(name = "html_url")
        private String htmlUrl;

        private String description;

        private Boolean fork;

        private String url;

        @JSONField(name = "forks_url")
        private String forksUrl;

        @JSONField(name = "keys_url")
        private String keysUrl;

        @JSONField(name = "collaborators_url")
        private String collaboratorsUrl;

        @JSONField(name = "teams_url")
        private String teamsUrl;

        @JSONField(name = "hooks_url")
        private String hooksUrl;

        @JSONField(name = "issue_events_url")
        private String issueEventsUrl;

        @JSONField(name = "events_url")
        private String eventsUrl;

        @JSONField(name = "assignees_url")
        private String assigneesUrl;

        @JSONField(name = "branches_url")
        private String branchesUrl;

        @JSONField(name = "tags_url")
        private String tagsUrl;

        @JSONField(name = "blobs_url")
        private String blobsUrl;

        @JSONField(name = "git_tags_url")
        private String gitTagsUrl;

        @JSONField(name = "git_refs_url")
        private String gitRefsUrl;

        @JSONField(name = "trees_url")
        private String treesUrl;

        @JSONField(name = "statuses_url")
        private String statusesUrl;

        @JSONField(name = "languages_url")
        private String languagesUrl;

        @JSONField(name = "stargazers_url")
        private String stargazersUrl;

        @JSONField(name = "contributors_url")
        private String contributorsUrl;

        @JSONField(name = "subscribers_url")
        private String subscribersUrl;

        @JSONField(name = "subscription_url")
        private String subscriptionUrl;

        @JSONField(name = "commits_url")
        private String commitsUrl;

        @JSONField(name = "git_commits_url")
        private String gitCommitsUrl;

        @JSONField(name = "comments_url")
        private String commentsUrl;

        @JSONField(name = "compare_url")
        private String compareUrl;

        @JSONField(name = "merges_url")
        private String mergesUrl;

        @JSONField(name = "archive_url")
        private String archiveUrl;

        @JSONField(name = "downloads_url")
        private String downloadsUrl;

        @JSONField(name = "issues_url")
        private String issuesUrl;

        @JSONField(name = "pulls_url")
        private String pullsUrl;

        @JSONField(name = "milestones_url")
        private String milestonesUrl;

        @JSONField(name = "notifications_url")
        private String notificationsUrl;

        @JSONField(name = "labels_url")
        private String labelsUrl;

        @JSONField(name = "releases_url")
        private String releasesUrl;

        @JSONField(name = "deployments_url")
        private String deploymentsUrl;

        @JSONField(name = "created_at")
        private String createdAt;

        @JSONField(name = "updated_at")
        private String updatedAt;

        @JSONField(name = "pushed_at")
        private String pushedAt;

        @JSONField(name = "git_url")
        private String gitUrl;

        @JSONField(name = "ssh_url")
        private String sshUrl;

        @JSONField(name = "clone_url")
        private String cloneUrl;

        @JSONField(name = "svn_url")
        private String svnUrl;

        private String homepage;

        private Long size;

        @JSONField(name = "stargazers_count")
        private Long stargazersCount;

        @JSONField(name = "watchers_count")
        private Long watchersCount;

        private String language;

        @JSONField(name = "has_issues")
        private Boolean hasIssues;

        @JSONField(name = "has_projects")
        private Boolean hasProjects;

        @JSONField(name = "has_downloads")
        private Boolean hasDownloads;

        @JSONField(name = "has_wiki")
        private Boolean hasWiki;

        @JSONField(name = "has_pages")
        private Boolean hasPages;

        @JSONField(name = "has_discussions")
        private Boolean hasDiscussions;

        @JSONField(name = "forks_count")
        private Long forksCount;

        private String mirrorUrl;

        private Boolean archived;

        private Boolean disabled;

        @JSONField(name = "open_issues_count")
        private Long openIssuesCount;

        private License license;

        @JSONField(name = "allow_forking")
        private String allowForking;

        @JSONField(name = "is_template")
        private String isTemplate;

        @JSONField(name = "web_commit_signoff_required")
        private String webCommitSignoffRequired;

        private List<String> topics;

        private String visibility;

        private Long forks;

        @JSONField(name = "open_issues")
        private Long openIssues;

        private Long watchers;

        @JSONField(name = "default_branch")
        private String defaultBranch;

        private Double score;

        @Data
        public static class License {
            @JSONField(name = "key")
            private String key;

            private String name;

            @JSONField(name = "spdx_id")
            private String spdxId;

            private String url;

            @JSONField(name = "node_id")
            private String nodeId;
        }

        @Data
        public static class Owner {
            private String login;

            private Long id;

            @JSONField(name = "node_id")
            private String nodeId;

            @JSONField(name = "avatar_url")
            private String avatarUrl;

            private String gravatarId;

            private String url;

            @JSONField(name = "html_url")
            private String htmlUrl;

            @JSONField(name = "followers_url")
            private String followersUrl;

            @JSONField(name = "following_url")
            private String followingUrl;

            @JSONField(name = "gists_url")
            private String gistsUrl;

            @JSONField(name = "starred_url")
            private String starredUrl;

            @JSONField(name = "subscriptions_url")
            private String subscriptionsUrl;

            @JSONField(name = "organizations_url")
            private String organizationsUrl;

            @JSONField(name = "repos_url")
            private String reposUrl;

            @JSONField(name = "events_url")
            private String eventsUrl;

            @JSONField(name = "received_events_url")
            private String receivedEventsUrl;

            private String type;

            @JSONField(name = "user_view_type")
            private String userViewType;

            private Boolean siteAdmin;
        }
    }
}
