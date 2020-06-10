package com.example.android2finalproject.model

data class Restaurant(var name:	String = "",
                      var city:	String = "",
                      var street1: String = "",
                      var house_number1: Int = 0,
                      var street2: String? = null,
                      var house_number2: Int? = null,
                      var grade: String = "pending inspection",
                      var next_inspection_day: Int = 1,
                      var next_inspection_month: Int = 1,
                      var next_inspection_year: Int = 1970,
                      var assigned_inspector_username: String = "general_pool",
                      var inspections_keys: MutableList<String> = mutableListOf())