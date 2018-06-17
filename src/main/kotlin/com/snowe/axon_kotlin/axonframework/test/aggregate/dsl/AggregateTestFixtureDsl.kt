package com.snowe.axon_kotlin.axonframework.test.aggregate.dsl

import com.snowe.axon_kotlin.axonframework.test.aggregate.whenever
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventsourcing.AggregateFactory
import org.axonframework.eventsourcing.EventSourcingRepository
import org.axonframework.messaging.MessageHandler
import org.axonframework.messaging.MetaData
import org.axonframework.test.aggregate.FixtureConfiguration
import org.hamcrest.Matcher


operator fun <T> FixtureConfiguration<T>.invoke(init: AggregateTestFixtureBuilder<T>.() -> Unit): FixtureConfiguration<T> {
    val fixture = AggregateTestFixtureBuilder<T>(this)
    fixture.init()
    fixture.build()
    return fixture.aggregateTestFixture
}

/**
 *
 * ```
 * fixture {
 *   given {
 *     events(CreatedEvent)
 *     commands(StubCommand)
 *   }
 *   whenever(StubCommand)
 *   expect {
 *     events = listOf(StubEvent)
 *   }
 * ```
 */
class AggregateTestFixtureBuilder<T>(val aggregateTestFixture: FixtureConfiguration<T>) {
    private var wheneverCommand: Any? = null
    private var wheneverMetaData: Map<String, Any> = MetaData.emptyInstance()
    private val expectsBuilder: ExpectsBuilder = ExpectsBuilder()
    private val givenBuilder: GivenBuilder = GivenBuilder()
    private val registerBuilder: RegisterBuilder<T> = RegisterBuilder()

    fun register(block: RegisterBuilder<T>.() -> Unit) = registerBuilder.apply(block)

    fun given(block: GivenBuilder.() -> Unit) = givenBuilder.apply(block)

    fun whenever(block: () -> Any) {
        val out = block()
        when (out) {
            is Pair<*, *> -> {
                wheneverCommand = out.first
                wheneverMetaData = out.second as Map<String, Any>
            }
            else -> {
                wheneverCommand = out
            }
        }
    }

    fun expect(block: ExpectsBuilder.() -> Unit) = expectsBuilder.apply(block)

    fun build() {
        requireNotNull(wheneverCommand)
        requireNotNull(expectsBuilder)
        val expects = expectsBuilder
        var executorBuilder = aggregateTestFixture
        executorBuilder = registerBuilder.repository?.let { aggregateTestFixture.registerRepository(it) } ?: executorBuilder
        executorBuilder = registerBuilder.aggregateFactory?.let { aggregateTestFixture.registerAggregateFactory(it) } ?: executorBuilder
        executorBuilder = registerBuilder.annotatedCommandHandler?.let { aggregateTestFixture.registerAnnotatedCommandHandler(it) }
                ?: executorBuilder
        registerBuilder.commandHandlers.forEach {
            executorBuilder.registerCommandHandler(it.key, it.value)
        }
        val testExecutor = executorBuilder
                .given(givenBuilder.eventsBuilder.list)
                .andGivenCommands(givenBuilder.commandsBuilder.list)
        var resultValidator = testExecutor.whenever(wheneverCommand!!, wheneverMetaData)
        resultValidator = expects.events?.let { resultValidator.expectEvents(*it.toTypedArray()) } ?: resultValidator
        resultValidator = expects.eventsMatching?.let { resultValidator.expectEventsMatching(it) } ?: resultValidator
        resultValidator = expects.returnValue?.let { resultValidator.expectReturnValue(it) } ?: resultValidator
        resultValidator = expects.returnValueMatching?.let { resultValidator.expectReturnValueMatching(it) } ?: resultValidator
        resultValidator = expects.exception?.let { resultValidator.expectException(it) } ?: resultValidator

    }

    data class RegisterBuilder<T>(
            var repository: EventSourcingRepository<T>? = null,
            var aggregateFactory: AggregateFactory<T>? = null,
            var annotatedCommandHandler: Any? = null,
            var commandHandlers: MutableMap<Class<*>, MessageHandler<CommandMessage<*>>> = mutableMapOf()
    ) {

        inline fun <reified C> commandHandler(command: MessageHandler<CommandMessage<*>>) {
            addCommandHandler(C::class.java, command)
        }

        fun <C> addCommandHandler(payloadType: Class<C>, command: MessageHandler<CommandMessage<*>>) {
            commandHandlers[payloadType] = command
        }
    }

    data class ExpectsBuilder(
            var returnValue: Any? = null,
            var returnValueMatching: Matcher<*>? = null,
            var noEvents: Boolean? = null,
            var events: List<Any>? = null,
            var eventsMatching: Matcher<out MutableList<in EventMessage<*>>>? = null,
            var exception: Matcher<*>? = null
    )

    class GivenBuilder {
        val eventsBuilder: GivenEventsBuilder = GivenEventsBuilder()
        val commandsBuilder: GivenCommandsBuilder = GivenCommandsBuilder()

        class GivenEventsBuilder {
            val list = mutableListOf<Any>()
            operator fun Any.unaryPlus() {
                list.add(this)
            }
        }

        class GivenCommandsBuilder {
            val list = mutableListOf<Any>()
            operator fun Any.unaryPlus() {
                list.add(this)
            }
        }

        fun events(builder: GivenEventsBuilder.() -> Unit) {
            eventsBuilder.apply(builder).list
        }

        fun commands(builder: GivenCommandsBuilder.() -> Unit) {
            commandsBuilder.apply(builder).list
        }
    }

}

