package com.vashuag.grocery.data.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.HnswIndex
import io.objectbox.annotation.Id

@Entity
data class GroceryItem(
    @Id var id: Long = 0,
    var title: String = "",
    @HnswIndex(dimensions = 1280) var embeddings: FloatArray? = null,
    var imagePath: String = "",
    var expiryDateMs: Long = System.currentTimeMillis()
)