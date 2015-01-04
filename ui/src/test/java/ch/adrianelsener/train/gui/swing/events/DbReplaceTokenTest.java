package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.odb.api.Odb;
import ch.adrianelsener.odb.api.OdbFunction;
import ch.adrianelsener.odb.api.OdbPredicate;
import ch.adrianelsener.train.gui.swing.model.TrackPart;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class DbReplaceTokenTest {

    @Test
    public void apply() throws Exception {
        final OdbFunction<TrackPart> replacement = mock(OdbFunction.class);
        final OdbPredicate<TrackPart> predicate = mock(OdbPredicate.class);
        final Odb<TrackPart> odb = mock(Odb.class);
        final DbReplaceToken testee = DbReplaceToken.create(predicate, replacement);
        // act
        testee.apply(odb);
        // assert
        verify(odb).replace(predicate, replacement);
        verifyNoMoreInteractions(odb, predicate, replacement);
    }
}