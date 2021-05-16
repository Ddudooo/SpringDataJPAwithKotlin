package study.springdatakt.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

@MappedSuperclass
open class JpaBaseEntity {

    @Column(updatable = false)
    var createdDate: LocalDateTime? = LocalDateTime.now()

    var updatedDate: LocalDateTime? = LocalDateTime.now()

    @PrePersist
    fun prePersist() {
        val now = LocalDateTime.now()
        createdDate = now
        updatedDate = now
    }

    @PreUpdate
    fun preUpdate() {
        updatedDate = LocalDateTime.now()
    }
}