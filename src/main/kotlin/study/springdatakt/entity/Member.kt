package study.springdatakt.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Member(username : String) {
    @Id
    @GeneratedValue
    var id : Long? = null

    var username : String = username
}