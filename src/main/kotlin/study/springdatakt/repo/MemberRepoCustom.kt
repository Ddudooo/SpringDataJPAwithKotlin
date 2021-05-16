package study.springdatakt.repo

import study.springdatakt.entity.Member

interface MemberRepoCustom {
    fun findMemberCustom():List<Member>
}