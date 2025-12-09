package com.vashuag.grocery

import android.content.Context
import com.vashuag.grocery.data.entity.GroceryItem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ObjectBoxModule {

    @Provides
    @Singleton
    fun provideBoxStore(@ApplicationContext context: Context): BoxStore {
        return ObjectBox.init(context).boxStore
    }

    @Provides
    @Singleton
    fun provideGroceryBox(store: BoxStore): Box<GroceryItem> {
        return store.boxFor(GroceryItem::class.java)
    }

}
