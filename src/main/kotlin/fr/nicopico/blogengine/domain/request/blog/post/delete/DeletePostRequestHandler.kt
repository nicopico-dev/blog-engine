package fr.nicopico.blogengine.domain.request.blog.post.delete

import fr.nicopico.blogengine.domain.repository.PostRepository
import fr.nicopico.blogengine.domain.request.CommandHandler
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
@Transactional
class DeletePostRequestHandler(
    private val postRepository: PostRepository
) : CommandHandler<DeletePostRequest> {
    override fun handle(request: DeletePostRequest) {
        postRepository.deleteById(request.postId)
    }
}
