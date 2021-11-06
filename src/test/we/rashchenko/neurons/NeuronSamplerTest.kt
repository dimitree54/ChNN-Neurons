package we.rashchenko.neurons

import org.junit.jupiter.api.Test
import org.openjdk.jol.info.GraphLayout
import we.rashchenko.base.ExternallyControlledActivity
import we.rashchenko.base.Feedback
import we.rashchenko.neurons.inputs.MirroringNeuron
import we.rashchenko.utils.randomIds
import java.util.*
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals
import kotlin.test.assertTrue


internal abstract class NeuronSamplerTest {
	private val timeLimitMillisForSampler = 20000L
	private val memoryLimitBytesForSampler = 100 * 1024 * 1024  // 10 Mb max for empty sampler after work
	private val numNeurons = 100000
	private val numSamplerTicks = 1000000

	private val timeLimitMillisForNeuron = 2000L
	private val memoryLimitBytesForNeuron = 1024 * 1024  // 1 Mb max per Neuron
	private val numNeuronTicks = 1000000
	private val numNeighboursForNeuron = 100

	abstract fun getInstance(): NeuronsSampler

	@Test
	fun testMemoryUsageAndRuntimeOfSampler() {
		val r = Random()
		val sampler = getInstance()
		val neurons = mutableListOf<Neuron>()
		val neuronToTest: Neuron
		measureTimeMillis {
			// imitating init
			repeat(numNeurons) {
				neurons.add(sampler.next(it))
			}
			// imitating work
			repeat(numSamplerTicks) {
				val id = r.nextInt(neurons.size)
				sampler.reportFeedback(id, Feedback(r.nextDouble() * 2 - 1))

				val i = r.nextInt(neurons.size)
				sampler.reportDeath(id)
				neurons.removeAt(i)
				neurons.add(sampler.next(id))
			}
			neuronToTest = sampler.next(-1)  // neuron from the middle of sequence
			neurons.forEachIndexed { i, _ -> sampler.reportDeath(i) }
		}.also { assertTrue(it < timeLimitMillisForSampler) }

		val sizeAfter = GraphLayout.parseInstance(sampler).totalSize()
		assertTrue(sizeAfter < memoryLimitBytesForSampler)

		testMemoryUsageAndRuntimeOfTheNeuron(neuronToTest)
		testExternalControlling(neuronToTest)
	}

	private fun testMemoryUsageAndRuntimeOfTheNeuron(neuron: Neuron) {
		val r = Random()
		// imitating work
		val fakeNeighboursIds =
			mutableListOf<Int>().also { neighbours -> repeat(numNeighboursForNeuron) { neighbours.add(randomIds.next()) } }
		measureTimeMillis {
			var timeStep = 0L
			repeat(numNeuronTicks) {
				if (r.nextDouble() < 0.1) {
					neuron.touch(fakeNeighboursIds[r.nextInt(fakeNeighboursIds.size)], timeStep)
				}
				if (r.nextDouble() < 0.1) {
					neuron.touch(fakeNeighboursIds[r.nextInt(fakeNeighboursIds.size)], timeStep)
				}
				if (r.nextDouble() < 0.1) {
					neuron.update(Feedback(r.nextDouble() * 2 - 1), timeStep)
				}
				if (r.nextDouble() < 0.1) {
					neuron.getFeedback(fakeNeighboursIds[r.nextInt(fakeNeighboursIds.size)])
				}
				if (r.nextDouble() < 0.1) {
					val i = r.nextInt(fakeNeighboursIds.size)
					neuron.forgetSource(fakeNeighboursIds[i])
					fakeNeighboursIds[i] = r.nextInt()
				}
				timeStep += r.nextInt(100)
				neuron.active
			}
			fakeNeighboursIds.forEach { neuron.forgetSource(it) }
		}.also { assertTrue(it < timeLimitMillisForNeuron) }

		val sizeAfter = GraphLayout.parseInstance(neuron).totalSize()
		assertTrue(sizeAfter < memoryLimitBytesForNeuron)
	}

	private fun testExternalControlling(neuron: Neuron) {
		val r = Random()
		// imitating work
		val fakeNeighboursIds =
			mutableListOf<Int>().also { neighbours -> repeat(numNeighboursForNeuron) { neighbours.add(randomIds.next()) } }

		val externalActivity = ExternallyControlledActivity()
		val mirroringNeuron = MirroringNeuron(externalActivity, neuron)
		var timeStep = 0L
		repeat(numNeuronTicks) {
			if (r.nextDouble() < 0.1) {
				val newValue = r.nextBoolean()
				externalActivity.active = newValue
				assertEquals(mirroringNeuron.active, newValue)
			}
			if (r.nextDouble() < 0.1) {
				mirroringNeuron.touch(fakeNeighboursIds[r.nextInt(fakeNeighboursIds.size)], timeStep)
			}
			if (r.nextDouble() < 0.1) {
				mirroringNeuron.touch(fakeNeighboursIds[r.nextInt(fakeNeighboursIds.size)], timeStep)
			}
			if (r.nextDouble() < 0.1) {
				mirroringNeuron.update(Feedback(r.nextDouble() * 2 - 1), timeStep)
			}
			if (r.nextDouble() < 0.1) {
				mirroringNeuron.getFeedback(fakeNeighboursIds[r.nextInt(fakeNeighboursIds.size)])
			}
			if (r.nextDouble() < 0.1) {
				val i = r.nextInt(fakeNeighboursIds.size)
				mirroringNeuron.forgetSource(fakeNeighboursIds[i])
				fakeNeighboursIds[i] = r.nextInt()
			}
			timeStep += r.nextInt(100)
		}
		// @todo make test for parallel neuron functions calling
	}
}
