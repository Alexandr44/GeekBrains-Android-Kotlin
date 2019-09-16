package com.alex44.kotlincourse.model

class MainModel(private var counter: Int) {

    var message : String

    init {
        message = "Nothing"
    }

    constructor(counter: Int, message : String) : this(counter) {
        this.message = message
    }

    override fun toString(): String {
        return "$counter:$message"
    }

    fun update(counter: Int, message: String) {
        this.counter = counter
        this.message = message
    }
}