package fr.nicopico.blogengine.domain.entities

import javax.persistence.*

@Entity
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val text: String,
    @ManyToOne
    val post: Post,
    @Embedded
    val contentMetadata: ContentMetadata,
)
