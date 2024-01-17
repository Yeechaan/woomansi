package com.lee.remember

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertEquals

class AndroidGreetingTest {

    @Test
    fun testExample() {
        assertTrue("Check Android is mentioned", Greeting().greet().contains("Android"))
    }

    @Test
    fun testCoroutineScope() = runBlocking {
        println(this.coroutineContext.job)
        launch {
            delay(2000L)
            println("### world2 ${this.coroutineContext.job}")
        }
        launch {
            delay(1000L)
            println("### world1 ${this.coroutineContext.job}")
        }
        println("### hello ${this.coroutineContext.job}")
    }

    @Test
    fun testEmptyFriendIndex() {
        val imageSize = 5
        val friendHasImageList = listOf(false, true, true, false, false, true, false)

        val currentIndex = 6
        assertEquals(1, currentIndex % imageSize)
    }

}