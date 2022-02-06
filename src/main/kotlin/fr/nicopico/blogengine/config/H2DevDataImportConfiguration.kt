package fr.nicopico.blogengine.config

import fr.nicopico.blogengine.domain.entities.Author
import fr.nicopico.blogengine.domain.entities.Email
import fr.nicopico.blogengine.domain.repository.AuthorRepository
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.annotation.PostConstruct

@Configuration
@Profile("h2dev")
class H2DevDataImportConfiguration(
    private val authorRepository: AuthorRepository,
) {
    @PostConstruct
    fun importData() {
        val author = Author(name = "Nicolas PICON", email = Email("nicopico.dev@gmail.com"))
        authorRepository.save(author)
    }
}
