package fr.nicopico.blogengine.domain.repository

import fr.nicopico.blogengine.domain.entities.Author
import fr.nicopico.blogengine.domain.entities.Email
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface AuthorRepository : CrudRepository<Author, Long> {
    @Query("select a from Author a where a.email = :email")
    fun findByEmail(email: Email): Author?
}
