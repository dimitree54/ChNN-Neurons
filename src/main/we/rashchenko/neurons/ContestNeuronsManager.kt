package we.rashchenko.neurons

import we.rashchenko.neurons.zoo.*

val CONTEST_NEURON_SAMPLERS = listOf(
    ::HebbianNeuronSampler,
    ::HebbianHappyNeuronSampler,
    ::HebbianAngryNeuronSampler,
    ::StochasticNeuronSampler,
    ::ImpostorDeadNeuronSampler,
    ::ImpostorHyperactiveNeuronSampler,
    ::ImpostorRandomNeuronSampler,
    ::PaceMakerNeuronSampler
)

@Suppress("unused")
fun getContestManager() = NeuronsManager().also { manager ->
    CONTEST_NEURON_SAMPLERS.forEach {
        manager.add(it())
    }
}
