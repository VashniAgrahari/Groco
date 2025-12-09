package com.vashuag.grocery

import android.content.Context
import com.vashuag.grocery.data.entity.MyObjectBox
import io.objectbox.BoxStore

class ObjectBox private constructor(val boxStore: BoxStore) {

    companion object {
        @Volatile
        private var instance: ObjectBox? = null

        fun init(context: Context): ObjectBox {
            return instance ?: synchronized(this) {
                instance ?: ObjectBox(
                    MyObjectBox.builder().androidContext(context.applicationContext).build()
                ).also { instance = it }
            }
        }

        fun get(): ObjectBox = instance ?: throw IllegalStateException("ObjectBox not initialized")
    }
}
