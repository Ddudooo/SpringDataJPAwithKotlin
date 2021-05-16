package study.springdatakt.repo

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import study.springdatakt.entity.Member

@SpringBootTest
@Transactional
internal class MemberRepoTest {
    @Autowired
    lateinit var memberRepo: MemberRepo

    @Test
    fun testMember(){
        val member = Member("홍길동")
        val savedMember = memberRepo.save(member)

        val findMember = memberRepo.findById(savedMember.id!!).get()

        assertThat(findMember.id).isEqualTo(savedMember.id!!)
        assertThat(findMember.username).isEqualTo(savedMember.username)
        assertThat(findMember).isEqualTo(member)
    }
}