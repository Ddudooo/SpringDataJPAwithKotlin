package study.springdatakt.repo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import study.springdatakt.entity.Member

@SpringBootTest
@Transactional
internal class MemberJpaRepoTest {

    @Autowired
    lateinit var memberJpaRepo : MemberJpaRepo

    @Test
    fun testMember() {
        val member : Member = Member("홍길동")
        val savedMember = memberJpaRepo.save(member)

        val findMember = memberJpaRepo.find(savedMember.id!!)

        assertThat(findMember.id!!).isEqualTo(savedMember.id!!)
        assertThat(findMember.username).isEqualTo(savedMember.username)
        assertThat(findMember).isEqualTo(member)
    }
}