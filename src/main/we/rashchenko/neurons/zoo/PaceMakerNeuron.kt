package we.rashchenko.neurons.zoo

import we.rashchenko.base.Feedback
import we.rashchenko.neurons.Neuron
import we.rashchenko.neurons.NeuronsSampler
import java.util.*

open class PaceMakerNeuron(private val period: Int) : Neuron {
	private var random = Random()
	private var internalActive = false
	override val active: Boolean
		get() = internalActive

	override fun forgetSource(sourceId: Int) {}

	override fun getFeedback(sourceId: Int): Feedback = Feedback(random.nextDouble() * 2 - 1)

	override fun touch(sourceId: Int, timeStep: Long) {
		internalActive = (timeStep % period == 0L)
	}

	override fun update(feedback: Feedback, timeStep: Long) {
		internalActive = (timeStep % period == 0L)
	}
}


class PaceMakerNeuronSampler : NeuronsSampler {
	private val random = Random()
	override val name: String = "PaceMakerNeuron"
	override val author: String = "dimitree54"
	override fun next(id: Int): Neuron {
		val randomPeriod = random.nextInt(100) + 1
		return PaceMakerNeuron(randomPeriod)
	}

	override fun reportFeedback(id: Int, feedback: Feedback) {}
	override fun reportDeath(id: Int) {}
}
