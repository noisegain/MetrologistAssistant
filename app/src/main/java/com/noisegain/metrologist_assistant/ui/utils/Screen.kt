package com.noisegain.metrologist_assistant.ui.utils

enum class Screen(val route: String, val popUpTo: String? = null) {
    Passports("passports", "main"),
    ShowPassport("passport"),
    AddPassport("add_passport"),
    EditPassport("edit_passport"),
    Main("main"),
    Settings("settings"),
    Reports("reports"),
    FilterScreen("filter_screen"),
    ExportScreen("export_screen", "main"),
    SelectPassports("select_passports"),
    FilterForSelect("filter_for_select")
}
