package we.rashchenko.neurons

import org.junit.jupiter.api.Test
import we.rashchenko.environments.SimpleEnvironment
import we.rashchenko.networks.ControlledNeuralNetwork
import we.rashchenko.networks.StochasticNeuralNetwork
import we.rashchenko.networks.builders.Evolution
import we.rashchenko.networks.builders.NeuralNetworkIn2DBuilder
import we.rashchenko.networks.controllers.ActivityController
import we.rashchenko.networks.controllers.ComplexController
import we.rashchenko.networks.controllers.TimeController

internal class ContestRunTest {

    @Test
    fun testRun() {
        val environment = SimpleEnvironment(100)
        val nnWithInput = StochasticNeuralNetwork()
        val controlledNN = ControlledNeuralNetwork(
            nnWithInput,
            ComplexController(
                listOf(TimeController(), ActivityController())
            ),
            0.5, 2, 0.2
        )
        val neuronsManager = getContestManager()
        val builder = NeuralNetworkIn2DBuilder(
            controlledNN,
            neuronsManager
        ).apply {
            addEnvironment(environment)
            repeat(1000) { addNeuron() }
        }
        val evolution = Evolution(builder,
            100, 10, 1.0
        )
        repeat(10000){
            controlledNN.tick()
            environment.tick()
            evolution.tick()
        }
    }
}