package fr.nicopico.blogengine.domain.repository

import fr.nicopico.blogengine.domain.entities.Post
import org.springframework.data.repository.CrudRepository

interface PostRepository : CrudRepository<Post, Long>
