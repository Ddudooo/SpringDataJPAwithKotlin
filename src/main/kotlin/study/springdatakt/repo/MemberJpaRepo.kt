package study.springdatakt.repo

import org.springframework.stereotype.Repository
import study.springdatakt.entity.Member
import javax.persistence.EntityManager

@Repository
class MemberJpaRepo(
    private val em : EntityManager
) {

    fun save(member: Member) : Member {
        em.persist(member)
        return member
    }

    fun find(id : Long):  Member = em.find(Member::class.java, id)
}