package fr.nicopico.blogengine.api.blog.dto

import fr.nicopico.blogengine.domain.entities.Author
import fr.nicopico.blogengine.domain.entities.Email

data class AuthorDTO(
    val id: Long,
    val name: String?,
    val email: Email,
)

fun Author.toDTO() = AuthorDTO(
    id = id!!,
    name = name,
    email = email,
)
