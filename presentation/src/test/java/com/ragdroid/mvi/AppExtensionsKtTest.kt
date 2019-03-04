package com.ragdroid.mvi

import org.junit.Assert.*
import org.junit.Test

class AppExtensionsKtTest {

    @Test
    fun testIf() {
        val condition = true
        val output = condition then { 1 + 1 }
        assertEquals(2, output)
    }

    @Test
    fun testIfElze() {
        val condition = true
        val output = condition then { 1 + 1 } elze { 2 + 2 }
        assertEquals(output, 2)
    }

    @Test
    fun testIfElzeInt() {
        val condition = false
        val output = condition then { 1 + 1 } elze { 2 + 2 }
        assertEquals(output, 4)
    }

    @Test
    fun testIfElzeString() {
        val condition = true
        val output = condition then { "Condition is true" } elze { "Condition is false" }
        assertEquals(output, "Condition is true")
    }

    @Test
    fun testIfElzeStringFalse() {
        val condition = false
        val output = condition then { "Condition is true" } elze { "Condition is false" }
        assertEquals(output, "Condition is false")
    }

    @Test
    fun testIfElzeObject() {
        val condition = true
        val output = condition then { TestIfElze(10) } elze { TestIfElze(100) }
        assertEquals(output.integer, 10)
    }

    @Test
    fun testIfElzeObjectFalse() {
        val condition = false
        val output = condition then { TestIfElze(10) } elze { TestIfElze(100) }
        assertEquals(output.integer, 100)
    }

    class TestIfElze(val integer: Int)
}