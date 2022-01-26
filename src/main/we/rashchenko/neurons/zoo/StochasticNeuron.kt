package we.rashchenko.neurons.zoo

import we.rashchenko.base.Feedback
import we.rashchenko.neurons.Neuron
import we.rashchenko.neurons.NeuronsSampler
import we.rashchenko.utils.clip
import java.util.*

open class StochasticNeuron(private val initWeight: Double, private val lr: Double) : Neuron {
	private val random = Random()
	private val weights = mutableMapOf<Int, Double>()
	private val feedbacks = mutableMapOf<Int, Feedback>()

	private var internalActive: Boolean = false
	override val active: Boolean
		get() = internalActive

	private var activatedOnTimeStep = Long.MIN_VALUE
	private var activatedOnTouchFrom: Int? = null
	override fun touch(sourceId: Int, timeStep: Long) {
		if (random.nextDouble() < weights.getOrPut(sourceId) { initWeight }) {
			internalActive = true
			activatedOnTimeStep = timeStep
			activatedOnTouchFrom = sourceId
		}
	}

	override fun forgetSource(sourceId: Int) {
		weights.remove(sourceId)
		feedbacks.remove(sourceId)
	}

	override fun getFeedback(sourceId: Int): Feedback = feedbacks.getOrDefault(sourceId, Feedback.NEUTRAL)

	override fun update(feedback: Feedback, timeStep: Long) {
		activatedOnTouchFrom?.also {
			feedbacks[it] = feedback
			weights[it] = weights[it]?.plus(lr * feedback.value)?.clip(0.01, 0.99) ?: initWeight
		}?:run{
			// That neuron is not active but receives feedback.
			// Probably it is from external controller or environment, not from other neurons.
			// Negative feedback means that it is not ok to be passive,
			//  so neuron slightly increases all weights to increase chances of being active
			// Do nothing for positive feedback.
			if (feedback.value < 0){
				weights.keys.forEach {
					feedbacks[it] = feedback
					weights[it] = weights[it]?.minus(lr * feedback.value)?.clip(0.01, 0.99) ?: initWeight
				}
			}
		}
		if (timeStep != activatedOnTimeStep) {
			internalActive = false
			activatedOnTouchFrom = null
		}
	}
}


class StochasticNeuronSampler : NeuronsSampler {
	private val random = Random()
	override val name: String = "StochasticNeuron"
	override val author: String = "dimitree54"
	override fun next(id: Int): Neuron {
		val randomInitWeight = random.nextDouble() / 5
		val randomLr = random.nextDouble() / 5 - 0.1
		return StochasticNeuron(randomInitWeight, randomLr)
	}

	override fun reportFeedback(id: Int, feedback: Feedback) {}
	override fun reportDeath(id: Int) {}
}
