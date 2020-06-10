package com.example.android2finalproject.model

data class Violation(var description: String = "",
                     var least_severe_condition: Int = 1,
                     var num_of_conditions: Int = 5,
                     var conditions_points: MutableList<Int> = mutableListOf())