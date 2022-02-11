package fr.nicopico.blogengine.domain.entities

import java.util.UUID
import javax.persistence.*

@Entity
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val title: String,
    val content: String,
    @OneToMany(
        mappedBy = "post",
        orphanRemoval = true,
    )
    val comments : List<Comment> = emptyList(),
    @Embedded
    val contentMetadata: ContentMetadata,
    val permalink: String = "test",
)
