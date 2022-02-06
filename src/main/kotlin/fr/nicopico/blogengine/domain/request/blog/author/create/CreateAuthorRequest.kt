package fr.nicopico.blogengine.domain.request.blog.author.create

import fr.nicopico.blogengine.domain.entities.Email

data class CreateAuthorRequest(
    val name: String?,
    val email: Email,
)
