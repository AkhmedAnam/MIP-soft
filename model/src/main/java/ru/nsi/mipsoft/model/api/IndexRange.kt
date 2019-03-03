package ru.nsi.mipsoft.model.api

data class IndexRange(var startIndex : Int = 0, var endIndex : Int = 0, var currentIndex : Int = 0) {
    fun nextIndexInTheRange() : Boolean = (currentIndex + 1) <= endIndex
    fun previousIndexInTheRange() : Boolean = (currentIndex - 1) >= startIndex
    fun increement() = ++currentIndex
    fun decreement() = --currentIndex
}