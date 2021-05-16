package study.springdatakt.repo

import org.springframework.data.jpa.repository.JpaRepository
import study.springdatakt.entity.Member

interface MemberRepo: JpaRepository<Member, Long> {

}