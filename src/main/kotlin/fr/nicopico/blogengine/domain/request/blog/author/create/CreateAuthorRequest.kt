package fr.nicopico.blogengine.domain.request.blog.author.create

import fr.nicopico.blogengine.domain.entities.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Email as EmailValidation

data class CreateAuthorRequest(
    val name: String?,

    @NotEmpty
    @EmailValidation
    val email: Email,
)
