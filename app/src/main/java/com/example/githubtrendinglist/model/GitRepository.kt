package com.example.githubtrendinglist.model

data class GitRepository(
    val totalCount: Long,
    val incompleteResults: Boolean,
    val items: List<GitRepositoryItem>
)

data class GitRepositoryItem(
    val id: Long,
    val nodeID: String,
    val name: String,
    val fullName: String,
    val private: Boolean,
    val owner: Owner,
    val htmlURL: String,
    val description: String? = null,
    val fork: Boolean,
    val url: String,
    val forksURL: String,
    val keysURL: String,
    val collaboratorsURL: String,
    val teamsURL: String,
    val hooksURL: String,
    val issueEventsURL: String,
    val eventsURL: String,
    val assigneesURL: String,
    val branchesURL: String,
    val tagsURL: String,
    val blobsURL: String,
    val gitTagsURL: String,
    val gitRefsURL: String,
    val treesURL: String,
    val statusesURL: String,
    val languagesURL: String,
    val stargazersURL: String,
    val contributorsURL: String,
    val subscribersURL: String,
    val subscriptionURL: String,
    val commitsURL: String,
    val gitCommitsURL: String,
    val commentsURL: String,
    val issueCommentURL: String,
    val contentsURL: String,
    val compareURL: String,
    val mergesURL: String,
    val archiveURL: String,
    val downloadsURL: String,
    val issuesURL: String,
    val pullsURL: String,
    val milestonesURL: String,
    val notificationsURL: String,
    val labelsURL: String,
    val releasesURL: String,
    val deploymentsURL: String,
    val createdAt: String,
    val updatedAt: String,
    val pushedAt: String,
    val gitURL: String,
    val sshURL: String,
    val cloneURL: String,
    val svnURL: String,
    val homepage: String? = null,
    val size: Long,
    val stargazersCount: Long,
    val watchersCount: Long,
    val language: String? = null,
    val hasIssues: Boolean,
    val hasProjects: Boolean,
    val hasDownloads: Boolean,
    val hasWiki: Boolean,
    val hasPages: Boolean,
    val forksCount: Long,
    val mirrorURL: Any? = null,
    val archived: Boolean,
    val disabled: Boolean,
    val openIssuesCount: Long,
    val license: License? = null,
    val allowForking: Boolean,
    val isTemplate: Boolean,
    val topics: List<String>,
    val visibility: String,
    val forks: Long,
    val openIssues: Long,
    val watchers: Long,
    val defaultBranch: String,
    val permissions: Permissions,
    val score: Double
)

enum class DefaultBranch {
    Develop,
    GhPages,
    Master
}

data class License(
    val key: String,
    val name: String,
    val spdxID: String,
    val url: String? = null,
    val nodeID: String
)

data class Owner(
    val login: String,
    val id: Long,
    val nodeID: String,
    val avatarURL: String,
    val gravatarID: String,
    val url: String,
    val htmlURL: String,
    val followersURL: String,
    val followingURL: String,
    val gistsURL: String,
    val starredURL: String,
    val subscriptionsURL: String,
    val organizationsURL: String,
    val reposURL: String,
    val eventsURL: String,
    val receivedEventsURL: String,
    val type: String,
    val siteAdmin: Boolean
)

enum class Type {
    Organization,
    User
}

data class Permissions(
    val admin: Boolean,
    val maintain: Boolean,
    val push: Boolean,
    val triage: Boolean,
    val pull: Boolean
)

enum class Visibility {
    Public, Private
}

sealed class RepositoryResponse() {
    data class SUCCESS(val data: GitRepository) : RepositoryResponse()
    data class FAILURE(val failure: String) : RepositoryResponse()
    data class ERROR(val error: String) : RepositoryResponse()
}