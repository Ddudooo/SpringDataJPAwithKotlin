package study.springdatakt.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import study.springdatakt.entity.Member

interface MemberRepo: JpaRepository<Member, Long> {
    fun findByUsernameAndAgeGreaterThan(username :String, age: Int) :List<Member>

    //관례상 엔티티.NamedQuery명 을 찾아 호출, 없을시 메소드 이름으로 쿼리호출
    //@Query(name = "Member.findByUsername")
    fun findByUsername(@Param("username") username: String) : List<Member>
}