package com.sonnt.fpmerchant.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.standardFormat() = this.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))