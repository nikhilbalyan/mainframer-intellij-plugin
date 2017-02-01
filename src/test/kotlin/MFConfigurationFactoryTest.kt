import com.elpassion.intelijidea.configuration.MFConfigurationFactory
import com.elpassion.intelijidea.configuration.MFRunConfigurationType
import com.elpassion.intelijidea.configuration.MFRunnerConfiguration
import com.intellij.openapi.command.impl.DummyProject
import org.junit.Assert.assertEquals
import org.junit.Test

class MFConfigurationFactoryTest {

    val confFactory = MFConfigurationFactory(MFRunConfigurationType())

    @Test
    fun shouldHaveProperName() {
        assertEquals("Factory for mainframer configuration", confFactory.name)
    }

    @Test
    fun shouldCreateProperRunConfiguration() {
        assertEquals(MFRunnerConfiguration::class.java, confFactory.createTemplateConfiguration(DummyProject.getInstance()).javaClass)
    }
}