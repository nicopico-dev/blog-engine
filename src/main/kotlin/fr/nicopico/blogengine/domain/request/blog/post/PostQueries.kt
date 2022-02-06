package fr.nicopico.blogengine.domain.request.blog.post

import fr.nicopico.blogengine.domain.entities.Post
import fr.nicopico.blogengine.domain.repository.PostRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class PostQueries(
    private val postRepository: PostRepository,
) {
    fun findAll(): Iterable<Post> =
        postRepository.findAll()

    @Throws(NoSuchElementException::class)
    fun getById(id: Long): Post =
        postRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("No Post found with ID $id")

    @Throws(NoSuchElementException::class)
    fun checkPostExists(id: Long) {
        getById(id)
    }
}
