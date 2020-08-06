package com.mutakin.githubfinder.model

data class GithubResponse(
	val totalCount: Int? = null,
	val incompleteResults: Boolean? = null,
	val items: ArrayList<User>
)
