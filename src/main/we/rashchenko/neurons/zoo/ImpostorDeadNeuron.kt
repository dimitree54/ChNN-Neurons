package we.rashchenko.neurons.zoo

import we.rashchenko.base.Feedback
import we.rashchenko.neurons.Neuron
import we.rashchenko.neurons.NeuronsSampler
import java.util.*

open class ImpostorDeadNeuron : Neuron {
	private var random = Random()
	override val active: Boolean
		get() = false

	override fun forgetSource(sourceId: Int) {}

	override fun getFeedback(sourceId: Int): Feedback = Feedback(random.nextDouble() * 2 - 1)

	override fun touch(sourceId: Int, timeStep: Long) {}

	override fun update(feedback: Feedback, timeStep: Long) {}
}


class ImpostorDeadNeuronSampler : NeuronsSampler {
	override val name: String = "ImpostorDeadNeuron"
	override val author: String = "dimitree54"
	override fun next(id: Int): Neuron {
		return ImpostorDeadNeuron()
	}

	override fun reportFeedback(id: Int, feedback: Feedback) {}
	override fun reportDeath(id: Int) {}
}
