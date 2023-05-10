package com.example.donatemate.model

class MemoriamData(val honourId: String,
                    val honourName: String,
                    val honourRela: String,
                    val honourBio: String) {
    constructor() : this("", "", "", "")
}