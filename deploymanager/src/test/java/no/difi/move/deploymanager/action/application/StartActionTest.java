package no.difi.move.deploymanager.action.application;

import no.difi.move.deploymanager.config.DeployManagerProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StartActionTest {

    private StartAction target;

    @Mock
    private DeployManagerProperties propertiesMock;

    @Before
    public void setUp() {
        target = new StartAction(propertiesMock);
    }

    @Test(expected = NullPointerException.class)
    public void apply_toNull_shouldThrow() {
        target.apply(null);
    }
}
