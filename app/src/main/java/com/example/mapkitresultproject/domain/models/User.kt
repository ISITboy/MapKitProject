package com.example.mapkitresultproject.domain.models

abstract class  User {

    abstract val email: String
    abstract val id: String
    abstract val name: String
    abstract val organization: String

    class Base(
        override val email: String,
        override val id: String,
        override val name: String="",
        override val organization:String=""
    ) : User(){
        constructor() : this("", "")
    }

    object Empty : User() {
        override val email = "Empty"
        override val id = "Empty"
        override val name="Empty"
        override val organization="Empty"
    }


}