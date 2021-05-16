package study.springdatakt.repo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import study.springdatakt.entity.Member
import study.springdatakt.entity.Team

@SpringBootTest
@Transactional
internal class MemberRepoTest {
    @Autowired
    lateinit var memberRepo: MemberRepo

    @Autowired
    lateinit var teamRepo: TeamRepo

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
}