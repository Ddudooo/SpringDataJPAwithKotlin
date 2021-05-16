package study.springdatakt.repo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import study.springdatakt.entity.Member

@SpringBootTest
@Transactional
internal class MemberJpaRepoTest {

    @Autowired
    lateinit var memberJpaRepo: MemberJpaRepo

    @Test
    fun testMember() {
        val member: Member = Member("홍길동")
        val savedMember = memberJpaRepo.save(member)

        val findMember = memberJpaRepo.find(savedMember.id!!)

        assertThat(findMember.id!!).isEqualTo(savedMember.id!!)
        assertThat(findMember.username).isEqualTo(savedMember.username)
        assertThat(findMember).isEqualTo(member)
    }

    @Test
    fun basicCRUDTest() {
        val member1 = Member("member1")
        val member2 = Member("member2")
        memberJpaRepo.save(member1)
        memberJpaRepo.save(member2)

        //단건 조회 검증
        val findMember1 = memberJpaRepo.findById(member1.id!!).get()
        val findMember2 = memberJpaRepo.findById(member2.id!!).get()

        assertThat(findMember1).isEqualTo(findMember1)
        assertThat(findMember2).isEqualTo(findMember2)

        //리스트 조회 검증
        val findAll = memberJpaRepo.findAll()
        assertThat(findAll.size).isEqualTo(2)

        //카운트 검증
        val count = memberJpaRepo.count()
        assertThat(count).isEqualTo(2)

        //삭제 검증
        memberJpaRepo.delete(member1)
        memberJpaRepo.delete(member2)

        val deleteCount = memberJpaRepo.count()
        assertThat(deleteCount).isEqualTo(0)
    }
}
