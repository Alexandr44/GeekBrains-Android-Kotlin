package com.alex44.kotlincourse.model.dtos

class User(val name : String, val email : String, val phone : String, val photoUrl : String) {

    constructor(name : String) : this(name, "email", "phone", "photoUrl")

}