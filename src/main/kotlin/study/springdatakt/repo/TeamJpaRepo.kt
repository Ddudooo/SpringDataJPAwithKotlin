package study.springdatakt.repo

import org.springframework.stereotype.Repository
import study.springdatakt.entity.Team
import java.util.*
import javax.persistence.EntityManager

@Repository
class TeamJpaRepo(
    private val em : EntityManager
) {
    fun save(team:Team) : Team {
        em.persist(team)
        return team
    }

    fun delete(team : Team) = em.remove(team)

    fun findAll():List<Team> {
        return em.createQuery("select t from Team t ", Team::class.java)
            .resultList
    }

    fun findById(id: Long): Optional<Team> {
        val find = em.find(Team::class.java, id)
        return Optional.ofNullable(find)
    }

    fun count() : Long {
        return em.createQuery("select count(t) from Team t ", Long::class.javaObjectType)
            .singleResult
    }
}