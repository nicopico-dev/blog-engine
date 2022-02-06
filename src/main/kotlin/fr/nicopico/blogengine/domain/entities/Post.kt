package fr.nicopico.blogengine.domain.entities

import javax.persistence.*

@Entity
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
)
