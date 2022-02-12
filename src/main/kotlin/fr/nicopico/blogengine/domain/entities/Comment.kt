package fr.nicopico.blogengine.domain.entities

import javax.persistence.*

@Entity
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,
    @Column(name = "text")
    val text: String,
    @ManyToOne
    @JoinColumn(name = "post_id")
    val post: Post,
    @Embedded
    val contentMetadata: ContentMetadata,
)
