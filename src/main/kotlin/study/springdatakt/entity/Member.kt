package study.springdatakt.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.NamedAttributeNode
import javax.persistence.NamedEntityGraph
import javax.persistence.NamedQuery

@Entity
@NamedQuery(
    name = "Member.findByUsername",
    query = "select m from Member m where m.username = :username"
)
@NamedEntityGraph(name= "Member.all", attributeNodes = [NamedAttributeNode("team")])
class Member(username : String, age: Int=0) : BaseEntity() {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    var id : Long? = null
    var age : Int = age
    var username : String = username

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    var team : Team? = null

    constructor(username: String, age: Int, team: Team): this(username, age){
        this.changeTeam(team)
    }

    fun changeTeam(team : Team) {
        this.team?.members?.remove(this)

        this.team = team
        team.members.add(this)
    }
}