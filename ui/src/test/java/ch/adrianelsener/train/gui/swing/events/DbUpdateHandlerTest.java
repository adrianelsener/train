package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.odb.api.DbUpdater;
import ch.adrianelsener.odb.api.Odb;
import ch.adrianelsener.train.gui.swing.model.TrackPart;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class DbUpdateHandlerTest {

    @Test
    public void replace() throws Exception {
        final Odb<TrackPart> db = mock(Odb.class);
        final DbUpdater<TrackPart> updater = mock(DbUpdater.class);
        final DbUpdateHandler testee = DbUpdateHandler.create(db);
        // act
        testee.replace(updater);
        // assert
        verify(updater).apply(db);
        verifyNoMoreInteractions(updater, db);
    }
}