package study.springdatakt.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import study.springdatakt.dto.MemberDto
import study.springdatakt.entity.Member
import study.springdatakt.repo.MemberRepo
import javax.annotation.PostConstruct

@RestController
class MemberController(
    private val memberRepo: MemberRepo
){
    @PostConstruct
    fun init() {
        for(i in 1..100){
            memberRepo.save(Member("user${i}", i))
        }
    }

    @GetMapping("/members/{id}")
    fun findMember(
        @PathVariable("id") id : Long
    ): String = memberRepo.findById(id).get().username

    @GetMapping("/members2/{id}")
    fun findMember2(
        @PathVariable("id") member : Member
    ): String = member.username

    @GetMapping("/members")
    fun list(@PageableDefault(size=5) pageable: Pageable) : Page<MemberDto> {
        return memberRepo.findAll(pageable).map { MemberDto(it.id!!, it.username, "null") }
    }
}