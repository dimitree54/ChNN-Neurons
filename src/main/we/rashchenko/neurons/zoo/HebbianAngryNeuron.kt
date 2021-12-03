package we.rashchenko.neurons.zoo

import we.rashchenko.base.Feedback
import we.rashchenko.neurons.Neuron
import we.rashchenko.neurons.NeuronsSampler

open class HebbianAngryNeuron : HebbianNeuron() {
	override fun getFeedback(sourceId: Int): Feedback = Feedback.VERY_NEGATIVE
}


class HebbianAngryNeuronSampler : NeuronsSampler {
	override val name: String = "HebbianAngryNeuron"
	override val author: String = "dimitree54"
	override fun next(id: Int): Neuron {
		return HebbianAngryNeuron()
	}

	override fun reportFeedback(id: Int, feedback: Feedback) {}
	override fun reportDeath(id: Int) {}
}
