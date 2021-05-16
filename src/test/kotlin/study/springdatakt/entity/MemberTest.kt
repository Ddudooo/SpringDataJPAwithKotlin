package study.springdatakt.entity

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import study.springdatakt.repo.MemberRepo
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@SpringBootTest
@Transactional
internal class MemberTest{
    @PersistenceContext
    lateinit var em : EntityManager

    @Autowired
    lateinit var memberRepo: MemberRepo

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

    @Test
    fun jpaEventBaseEntityTest() {
        //given
        val member = Member("member1")
        memberRepo.save(member) //@PrePersist

        Thread.sleep(100L)
        member.username = "member2"

        em.flush()
        em.clear()

        //when
        val findMember = memberRepo.findById(member.id!!).get()

        //then
        println("findMember.createdDate = ${findMember.createdDate?:"null"}")
        println("findMember.updatedDate = ${findMember.updatedDate?:"null"}")
        println("findMember.createdBy = ${findMember.createdBy?:"null"}")
        println("findMember.modifiedBy = ${findMember.lastModifiedBy?:"null"}")
    }
}