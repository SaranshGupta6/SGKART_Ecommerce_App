package com.example.aexpress.model

import com.example.aexpress.utils.Constants
import Map.Entry
import kotlin.Throws

class Category(
    private var name: String?,
    private var icon: String?,
    private var color: String?,
    private var brief: String?,
    private var id: Int
) {
    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getIcon(): String? {
        return icon
    }

    fun setIcon(icon: String?) {
        this.icon = icon
    }

    fun getColor(): String? {
        return color
    }

    fun setColor(color: String?) {
        this.color = color
    }

    fun getBrief(): String? {
        return brief
    }

    fun setBrief(brief: String?) {
        this.brief = brief
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }
}
