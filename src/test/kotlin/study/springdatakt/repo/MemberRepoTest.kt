package study.springdatakt.repo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.transaction.annotation.Transactional
import study.springdatakt.entity.Member
import study.springdatakt.entity.Team
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@SpringBootTest
@Transactional
internal class MemberRepoTest {
    @Autowired
    lateinit var memberRepo: MemberRepo

    @Autowired
    lateinit var teamRepo: TeamRepo

    @PersistenceContext
    lateinit var em : EntityManager

    @Test
    fun testMember(){
        val member = Member("홍길동")
        val savedMember = memberRepo.save(member)

        val findMember = memberRepo.findById(savedMember.id!!).get()

        assertThat(findMember.id).isEqualTo(savedMember.id!!)
        assertThat(findMember.username).isEqualTo(savedMember.username)
        assertThat(findMember).isEqualTo(member)
    }

    @Test
    fun basicCRUDTest() {
        val member1 = Member("member1")
        val member2 = Member("member2")
        memberRepo.save(member1)
        memberRepo.save(member2)

        //단건 조회 검증
        val findMember1 = memberRepo.findById(member1.id!!).get()
        val findMember2 = memberRepo.findById(member2.id!!).get()

        assertThat(findMember1).isEqualTo(findMember1)
        assertThat(findMember2).isEqualTo(findMember2)

        //리스트 조회 검증
        val findAll = memberRepo.findAll()
        assertThat(findAll.size).isEqualTo(2)

        //카운트 검증
        val count = memberRepo.count()
        assertThat(count).isEqualTo(2)

        //삭제 검증
        memberRepo.delete(member1)
        memberRepo.delete(member2)

        val deleteCount = memberRepo.count()
        assertThat(deleteCount).isEqualTo(0)
    }

    @Test
    fun findByUsernameAndAgeGreaterThanTest(){
        val member1 = Member("AAA",10)
        val member2 = Member("AAA", 20)
        memberRepo.save(member1)
        memberRepo.save(member2)

        val result =
            memberRepo.findByUsernameAndAgeGreaterThan("AAA", 15)

        for(member in result){
            assertThat(member.username).isEqualTo("AAA")
            assertThat(member.age).isGreaterThan(15)
        }
    }

    @Test
    fun testNamedQuery() {
        val member1 = Member("AAA", 10)
        val member2 = Member("BBB",20)
        memberRepo.save(member1)
        memberRepo.save(member2)

        val result = memberRepo.findByUsername("AAA")

        for(member in result){
            assertThat(member.username).isEqualTo("AAA")
        }
    }

    @Test
    fun testQuery() {
        val member1 = Member("AAA", 10)
        val member2 = Member("BBB",20)
        memberRepo.save(member1)
        memberRepo.save(member2)

        val result = memberRepo.findUser("AAA",10)

        for(member in result){
            assertThat(member.username).isEqualTo("AAA")
            assertThat(member.age).isEqualTo(10)
        }
    }

    @Test
    fun testQueryUsername() {
        val member1 = Member("AAA", 10)
        val member2 = Member("BBB",20)
        memberRepo.save(member1)
        memberRepo.save(member2)

        val result = memberRepo.findUsernameList()

        for(username in result){
            assertThat(username).isNotNull
        }
    }

    @Test
    fun testQueryDto() {
        val teamA = Team("teamA")
        teamRepo.save(teamA)
        val member1 = Member("AAA", 10,teamA)
        val member2 = Member("BBB",20,teamA)
        memberRepo.save(member1)
        memberRepo.save(member2)

        val result = memberRepo.findMemberDto()

        for(dto in result){
            assertThat(dto.id).isNotNull
            assertThat(dto.username).isIn("AAA","BBB")
            assertThat(dto.teamName).isEqualTo("teamA")
        }
    }

    @Test
    fun testQueryList(){
        val member1 = Member("AAA", 10)
        val member2 = Member("BBB",20)
        memberRepo.save(member1)
        memberRepo.save(member2)

        val result = memberRepo.findByNames(listOf("AAA","BBB"))

        for(member in result){
            assertThat(member.username).isIn("AAA","BBB")
        }
    }

    @Test
    fun testQueryReturnType() {
        val member1 = Member("AAA", 10)
        val member2 = Member("BBB",20)
        memberRepo.save(member1)
        memberRepo.save(member2)

        //val findListByUsername = memberRepo.findListByUsername("AAA")
        //val findMemberByUsername = memberRepo.findMemberByUsername("AAA")
        val findOptionalMemberByUsername = memberRepo.findOptionalMemberByUsername("AAA")
    }

    @Test
    fun testPaging(){
        //given
        memberRepo.save(Member("member1", 10))
        memberRepo.save(Member("member2", 10))
        memberRepo.save(Member("member3", 10))
        memberRepo.save(Member("member4", 10))
        memberRepo.save(Member("member5", 10))

        val age = 10
        val pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"))

        //when
        val page = memberRepo.findByAge(age, pageable)
        //편하게 DTO 변환
        //page.map{ MemberDto(it.id!!, it.username, "") }

        //then
        assertThat(page.content.size).isEqualTo(3)
        assertThat(page.totalElements).isEqualTo(5)
    }

    @Test
    fun testBulkUpdate() {
        //given
        memberRepo.save(Member("member1", 10))
        memberRepo.save(Member("member2", 19))
        memberRepo.save(Member("member3", 20))
        memberRepo.save(Member("member4", 21))
        memberRepo.save(Member("member5", 40))

        //when
        val resultCount = memberRepo.bulkAgePlus(20)
        //em.clear()

        val findMember = memberRepo.findByUsername("member5")
        for(member in findMember){
            println("member ${member.username} ${member.age}")
        }

        //then
        assertThat(resultCount).isEqualTo(3)
    }

    @Test
    fun findMemberLazyTest() {
        //given
        //member1 -> teamA
        //member2 -> teamB
        val teamA = Team("teamA")
        val teamB = Team("teamB")
        teamRepo.save(teamA)
        teamRepo.save(teamB)

        val member1 = Member("member1", 10, teamA)
        val member2 = Member("member2", 10, teamB)
        memberRepo.save(member1)
        memberRepo.save(member2)

        em.flush()
        em.clear()

        //when
        val members = memberRepo.findGraphBy()

        //then
        for(member in members){
            println("member = ${member.username} ${member.age}")
            println("member.teamClass = ${member.team?.javaClass?:"null"}")
            println("member.team = ${member.team?.name?:"null"}")
        }
    }

    @Test
    fun testQueryHint() {
        //given
        val saved = memberRepo.save(Member("member1", 10))
        em.flush()
        em.clear()

        //when
        val findMember = memberRepo.findReadOnlyByUsername("member1")
        findMember.username = "member2"

        em.flush()
    }

    @Test
    fun testLock() {
        //given
        val saved = memberRepo.save(Member("member1", 10))
        em.flush()
        em.clear()

        //when
        val findMember = memberRepo.findLockByUsername("member1")
    }

    @Test
    fun testCallCustom(){
        val findMemberCustom = memberRepo.findMemberCustom()
    }
}