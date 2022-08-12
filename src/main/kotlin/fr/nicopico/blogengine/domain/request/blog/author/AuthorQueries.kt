package fr.nicopico.blogengine.domain.request.blog.author

import fr.nicopico.blogengine.domain.entities.Author
import fr.nicopico.blogengine.domain.repository.AuthorRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class AuthorQueries(
    private val authorRepository: AuthorRepository
) {
    fun findAll(): Iterable<Author> =
        authorRepository.findAll()

    @Throws(NoSuchElementException::class)
    fun getById(id: Long): Author =
        authorRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("No author found with ID $id")
}
