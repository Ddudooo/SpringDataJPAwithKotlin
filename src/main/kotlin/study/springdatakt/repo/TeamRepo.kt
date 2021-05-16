package study.springdatakt.repo

import org.springframework.data.jpa.repository.JpaRepository
import study.springdatakt.entity.Team

interface TeamRepo:JpaRepository<Team, Long> {
}