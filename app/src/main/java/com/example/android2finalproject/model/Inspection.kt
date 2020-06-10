package com.example.android2finalproject.model

data class Inspection(var inspection_day: Int = 1,
                      var inspection_month: Int = 1,
                      var inspection_year: Int = 1970,
                      var points: Int = 0,
                      var restaurant_key: String = "",
                      var inspector_username: String = "general_pool",
                      var violations_keys: MutableList<String> = mutableListOf(),
                      var violations_conditions: MutableList<Int> = mutableListOf())