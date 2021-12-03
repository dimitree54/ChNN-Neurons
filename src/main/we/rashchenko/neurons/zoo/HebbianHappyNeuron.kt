package we.rashchenko.neurons.zoo

import we.rashchenko.base.Feedback
import we.rashchenko.neurons.Neuron
import we.rashchenko.neurons.NeuronsSampler

open class HebbianHappyNeuron : HebbianNeuron() {
	override fun getFeedback(sourceId: Int): Feedback = Feedback.VERY_POSITIVE
}


class HebbianHappyNeuronSampler : NeuronsSampler {
	override val name: String = "HebbianHappyNeuron"
	override val author: String = "dimitree54"
	override fun next(id: Int): Neuron {
		return HebbianHappyNeuron()
	}

	override fun reportFeedback(id: Int, feedback: Feedback) {}
	override fun reportDeath(id: Int) {}
}
