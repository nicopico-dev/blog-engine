package fr.nicopico.blogengine.api.blog.author.dto

import fr.nicopico.blogengine.domain.request.blog.author.create.CreateAuthorRequest
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class CreateAuthorRequestDTO(
    val name: String?,

    @Email
    @NotEmpty(message = "{validation.email.notEmpty}")
    val email: String,
)

fun CreateAuthorRequestDTO.toRequest() =
    CreateAuthorRequest(
        name = name,
        email = fr.nicopico.blogengine.domain.entities.Email(email)
    )
