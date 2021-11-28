package we.rashchenko.neurons

import we.rashchenko.neurons.zoo.HebbianAngryNeuronSampler
import we.rashchenko.neurons.zoo.HebbianHappyNeuronSampler
import we.rashchenko.neurons.zoo.HebbianNeuronSampler
import we.rashchenko.neurons.zoo.StochasticNeuronSampler

val CONTEST_NEURON_SAMPLERS = listOf(
    HebbianNeuronSampler(),
    HebbianHappyNeuronSampler(),
    HebbianAngryNeuronSampler(),
    StochasticNeuronSampler(),
)

@Suppress("unused")
val CONTEST_NEURONS_MANAGER = NeuronsManager().also { manager ->
    CONTEST_NEURON_SAMPLERS.forEach {
        manager.add(it)
    }
}
