package study.springdatakt.entity

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@SpringBootTest
@Transactional
internal class MemberTest{
    @PersistenceContext
    lateinit var em : EntityManager

    @Test
    @Rollback(false)
    fun testEntity() {
        val teamA = Team("teamA")
        val teamB = Team("teamB")
        em.persist(teamA)
        em.persist(teamB)

        val member1 = Member("member1", 10, teamA)
        val member2 = Member("member2", 20, teamA)
        val member3 = Member("member3", 30, teamB)
        val member4 = Member("member4", 40, teamB)

        em.persist(member1)
        em.persist(member2)
        em.persist(member3)
        em.persist(member4)

        //초기화
        em.flush()
        em.clear()

        //확인
        val members = em.createQuery("select m from Member m join fetch m.team t", Member::class.java)
            .resultList

        for(member in members){
            println("member = ${member.username} ${member.age}")
            println("-> member.team = ${member.team?.name?:"null value"}")
        }
    }
}