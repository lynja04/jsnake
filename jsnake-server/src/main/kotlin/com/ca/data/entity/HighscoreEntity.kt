package com.ca.data.entity

import javax.persistence.*

@Entity
@Table(name = "highscore")
data class HighscoreEntity(@Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Int = 0,
                           @Column(length = 45) var username: String = "",
                           var score: Int = 0,
                           @Enumerated(EnumType.STRING) var difficulty: Difficulty = Difficulty.medium)