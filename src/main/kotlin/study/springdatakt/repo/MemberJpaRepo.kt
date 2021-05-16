package study.springdatakt.repo

import org.springframework.stereotype.Repository
import study.springdatakt.entity.Member
import java.util.*
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

    fun findById(id:Long): Optional<Member> {
        val find = em.find(Member::class.java, id)
        return Optional.ofNullable(find)
    }

    fun count(): Long {
        //wrapping 객체로 컨버팅을 위해 javaObjectType으로...
        return em.createQuery("select count(m) from Member m", Long::class.javaObjectType)
            .singleResult
    }

    fun findAll() : List<Member> {
        return em.createQuery("select m From Member m", Member::class.java).resultList
    }

    fun delete(member :Member) = em.remove(member)
}