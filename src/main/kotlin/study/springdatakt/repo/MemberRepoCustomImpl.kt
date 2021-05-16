package study.springdatakt.repo

import study.springdatakt.entity.Member
import javax.persistence.EntityManager

class MemberRepoCustomImpl(
    private val em : EntityManager
):MemberRepoCustom {
    override fun findMemberCustom(): List<Member> {
        return em.createQuery("select m from Member m", Member::class.java)
            .resultList
    }
}