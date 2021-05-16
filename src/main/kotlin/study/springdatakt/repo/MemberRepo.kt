package study.springdatakt.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.data.repository.query.Param
import study.springdatakt.dto.MemberDto
import study.springdatakt.entity.Member
import java.util.*
import javax.persistence.LockModeType
import javax.persistence.QueryHint

interface MemberRepo: JpaRepository<Member, Long>, MemberRepoCustom {
    fun findByUsernameAndAgeGreaterThan(username :String, age: Int) :List<Member>

    //관례상 엔티티.NamedQuery명 을 찾아 호출, 없을시 메소드 이름으로 쿼리호출
    //@Query(name = "Member.findByUsername")
    fun findByUsername(@Param("username") username: String) : List<Member>

    @Query("select m from Member m where m.username = :username and m.age = :age")
    fun findUser(@Param("username") username :String, @Param("age") age :Int) : List<Member>

    @Query("select m.username from Member m ")
    fun findUsernameList() : List<String>

    @Query("select new study.springdatakt.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    fun findMemberDto() :List<MemberDto>

    @Query("select m from Member m where m.username in :names")
    fun findByNames(@Param("names") names: Collection<String>):List<Member>

    fun findListByUsername(username:String): List<Member>
    fun findMemberByUsername(username: String):Member
    fun findOptionalMemberByUsername(username: String): Optional<Member>

    fun findByAge(age:Int, pageable: Pageable) : Page<Member>

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age +1 where m.age >= :age")
    fun bulkAgePlus(@Param("age") age :Int) : Int

    @Query("select m from Member m left join fetch m.team")
    fun findMemberFetchJoin():List<Member>

    @EntityGraph(attributePaths = ["team"])
    fun findGraphBy() : List<Member>

    @QueryHints(value = [QueryHint(name = "org.hibernate.readOnly", value = "true")])
    fun findReadOnlyByUsername(username : String) : Member

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findLockByUsername(username: String) : List<Member>
}