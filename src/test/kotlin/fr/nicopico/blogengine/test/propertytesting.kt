package fr.nicopico.blogengine.test

import io.kotest.common.runBlocking
import io.kotest.property.Gen
import io.kotest.property.PropertyContext
import io.kotest.property.checkAll

private const val KOTEST_ITERATION = 200

fun <A> checkAllBlocking(genA: Gen<A>, block: PropertyContext.(A) -> Unit): Unit = runBlocking {
    checkAll(iterations = KOTEST_ITERATION, genA, block)
}

fun <A, B> checkAllBlocking(genA: Gen<A>, genB: Gen<B>, block: PropertyContext.(A, B) -> Unit): Unit = runBlocking {
    checkAll(iterations = KOTEST_ITERATION, genA, genB, block)
}

fun <A, B, C> checkAllBlocking(
    genA: Gen<A>,
    genB: Gen<B>,
    genC: Gen<C>,
    block: PropertyContext.(A, B, C) -> Unit
): Unit = runBlocking {
    checkAll(iterations = KOTEST_ITERATION, genA, genB, genC, block)
}
