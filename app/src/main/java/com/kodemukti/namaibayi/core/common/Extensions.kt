package com.kodemukti.namaibayi.core.common

fun String.isValidEmail(): Boolean = this.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))

fun String.isValidPhone(): Boolean = this.matches(Regex("^\\+?[0-9]{10,15}$"))

fun String.isValidName(): Boolean = this.matches(Regex("^[A-Za-z\\s]{2,50}$"))

fun String.isValidIndonesianName(): Boolean = this.matches(Regex("^[A-Za-zA-ZÀ-ÿ\\s]{2,50}$"))
