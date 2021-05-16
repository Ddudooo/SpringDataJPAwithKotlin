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

    fun findByUsernameAndAgeGreaterThan(username:String, age :Int) : List<Member> {
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age", Member::class.java)
            .setParameter("username", username)
            .setParameter("age", age)
            .resultList
    }

    fun findByUsername(username: String) : MutableList<Member> {
        return em.createNamedQuery("Member.findByUsername", Member::class.java)
            .setParameter("username", username)
            .resultList
    }

    fun findByPage(age:Int, offset:Int, limit:Int):List<Member>{
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc", Member::class.java)
            .setParameter("age", age)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .resultList
    }

    fun totalCount(age: Int): Long {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long::class.javaObjectType)
            .setParameter("age",age)
            .singleResult
    }

    fun bulkAgePlus(age :Int) : Int {
        return em.createQuery("update Member m set m.age = m.age +1 where m.age >= :age")
            .setParameter("age", age)
            .executeUpdate()
    }
}