package fr.nicopico.blogengine.domain.request.blog.post.delete

import fr.nicopico.blogengine.domain.repository.PostRepository
import fr.nicopico.blogengine.domain.request.RequestHandler
import fr.nicopico.blogengine.infra.logInfo
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
@Transactional
class DeletePostRequestHandler(
    private val postRepository: PostRepository
) : RequestHandler<DeletePostRequest, Unit> {
    override fun handle(request: DeletePostRequest) {
        postRepository.deleteById(request.postId)
    }
}
