package fr.nicopico.blogengine.domain.request.blog.author.create

import fr.nicopico.blogengine.domain.entities.Author
import fr.nicopico.blogengine.domain.repository.AuthorRepository
import fr.nicopico.blogengine.domain.request.RequestHandler
import fr.nicopico.blogengine.infra.logInfo
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
@Transactional
class CreateAuthorRequestHandler(
    private val authorRepository: AuthorRepository
) : RequestHandler<CreateAuthorRequest, Author> {
    override fun handle(request: CreateAuthorRequest): Author {
        val author = Author(
            name = request.name,
            email = request.email
        )

        return authorRepository.save(author).also {
            logInfo { "Create Author $it" }
        }
    }
}
