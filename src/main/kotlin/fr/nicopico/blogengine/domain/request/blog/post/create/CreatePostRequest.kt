package fr.nicopico.blogengine.domain.request.blog.post.create

import fr.nicopico.blogengine.domain.entities.Email

data class CreatePostRequest(
    val title: String,
    val content: String,
    val authorEmail: Email,
)
