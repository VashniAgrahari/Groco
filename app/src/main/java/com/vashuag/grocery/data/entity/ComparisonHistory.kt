package com.vashuag.grocery.data.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class ComparisonHistory(
    @Id var id: Long = 0,
    var fingerprint: String = "",
    var queryText: String = "",
    var locationJson: String = "",
    var responseJson: String = "",
    var createdAtMs: Long = System.currentTimeMillis()
)
