package com.snowe.axon_kotlin.axonframework.test.saga

import com.nhaarman.mockito_kotlin.mock
//import com.snowe.axon_kotlin.events.FooCreatedEvent
//import com.snowe.axon_kotlin.events.FooDeletedEvent
//import com.snowe.axon_kotlin.events.FooIncrementedEvent
import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.eventhandling.saga.EndSaga
import org.axonframework.eventhandling.saga.SagaEventHandler
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test


/**
 * @author Tyler Thrailkill
 */
/**
class SagaHelpersTest {

    @Test
    fun `Fixture created with inline function`() {
        val fixture = SagaTestFixture<StubAnnotatedSaga>()
        assertNotNull(fixture)
    }

    @Test
    fun `Register of command gateway`() {
        val fixture = SagaTestFixture<StubAnnotatedSaga>()
        val gateway = fixture.registerCommandGateway<CommandGateway>()
        assertNotNull(gateway)
    }

    @Test
    fun `Register of command gateway with object`() {
        val commandBus = mock<CommandBus>()
        val fixture = SagaTestFixture<StubAnnotatedSaga>()
        val gateway = fixture.registerCommandGateway<CommandGateway>(DefaultCommandGateway(commandBus))
        assertNotNull(gateway)
    }

    @Test
    fun `Register ignored field`() {
        val fixture = SagaTestFixture<StubAnnotatedSaga>()
        val fixtureConfiguration = fixture.registerIgnoredField<StubAnnotatedSaga>("invocationCount")
        assertNotNull(fixtureConfiguration)
    }
}

class StubAnnotatedSaga {

    private val serialVersionUID = -3224806999195676097L
    private var invocationCount = 0

    @SagaEventHandler(associationProperty = "id")
    fun handle(event: FooCreatedEvent) {
        invocationCount++
    }

    @SagaEventHandler(associationProperty = "id")
    fun handle(event: FooIncrementedEvent) {
        invocationCount++
    }

//    @SagaEventHandler(associationProperty = "id", associationResolver = MetaDataAssociationResolver::class)
//    fun handle(event: EventWithoutProperties) {
//        invocationCount++
//    }

    @EndSaga
    @SagaEventHandler(associationProperty = "id")
    fun handle(event: FooDeletedEvent) {
        invocationCount++
    }
}

*/
