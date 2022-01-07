package we.rashchenko.neurons.zoo

import we.rashchenko.base.Feedback
import we.rashchenko.neurons.Neuron
import we.rashchenko.neurons.NeuronsSampler
import java.util.*

open class HebbianNeuron : Neuron {
	private val random = Random()
	private val weights = mutableMapOf<Int, Double>()

	private var activatedOnTimeStep = Long.MIN_VALUE

	private fun initializeNewWeight(): Double {
		return 0.1
	}

	private var internalActive: Boolean = false
	override val active: Boolean
		get() = internalActive

	override fun touch(sourceId: Int, timeStep: Long) {
		weights.getOrPut(sourceId, ::initializeNewWeight).let {
			if (random.nextDouble() < it) {
				internalActive = true
				weights[sourceId] = it + 0.01
			} else {
				weights[sourceId] = it - (0.01 / weights.size)
			}
		}
	}

	override fun forgetSource(sourceId: Int) {
		weights.remove(sourceId)
	}

	private var receivedFeedback = Feedback.NEUTRAL
	override fun getFeedback(sourceId: Int): Feedback = receivedFeedback

	override fun update(feedback: Feedback, timeStep: Long) {
		receivedFeedback = feedback
		if (timeStep != activatedOnTimeStep) {
			internalActive = false
		}
	}
}


class HebbianNeuronSampler : NeuronsSampler {
	override val name: String = "HebbianNeuron"
	override val author: String = "dimitree54"
	override fun next(id: Int): Neuron {
		return HebbianNeuron()
	}

	override fun reportFeedback(id: Int, feedback: Feedback) {}
	override fun reportDeath(id: Int) {}
}
