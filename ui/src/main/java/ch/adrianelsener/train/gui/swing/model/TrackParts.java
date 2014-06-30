package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.ToggleCallback;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Iterator;
import java.util.List;

public class TrackParts implements Iterable<TrackPart> {
    private final static Logger logger = LoggerFactory.getLogger(TrackParts.class);
    private final List<TrackPart> parts = Lists.newArrayList();

    public void add(final TrackPart part) {
        parts.add(part);
    }

    public void paintAll(final Graphics2D g) {
        logger.debug("Paint all Parts");
        for (final TrackPart part : parts) {
            part.paint(g);
        }
    }

    public Point getNextConnectionPoint(final Point p) {
        logger.debug("Search next point near {}", p);
        Point result = p;
        for (final TrackPart part : parts) {
            if (part.isNear(p)) {
                logger.debug("Found something : {}", part);
                result = part.getNextConnectionpoint(p);
                logger.debug("Point nearby is: {}", result);
            }
        }
        return result;
    }

    @Override
    public Iterator<TrackPart> iterator() {
        return parts.iterator();
    }

    public void mirrorNextTo(final Point point) {
        logger.debug("Mirroring object next to {}", point);
        for (final TrackPart part : parts) {
            if (part.isNear(point)) {
                final TrackPart mirrored = part.createMirror();
                replace(part, mirrored);
                break;
            }
        }
    }

    public void move(final Point fromPoint, final Point toPoint) {
        logger.debug("Move a part from {} to {}", fromPoint, toPoint);
        for (final TrackPart part : parts) {
            if (part.isNear(fromPoint)) {
                final TrackPart moved = part.moveTo(toPoint);
                replace(part, moved);
                break;
            }
        }
    }

    public void delete(final Point point) {
        logger.debug("Delete object next to {}", point);
        for (final TrackPart part : parts) {
            if (part.isNear(point)) {
                logger.debug("found {}", part);
                parts.remove(part);
                break;
            }
        }
    }

    public void replace(final TrackPart part, final TrackPart other) {
        parts.remove(part);
        parts.add(other);
    }

    public boolean contains(final TrackPart part) {
        return parts.contains(part);
    }

    public void clear() {
        parts.clear();
    }

    public void addAll(final Iterable<TrackPart> poits) {
        for (final TrackPart trackPart : poits) {
            add(trackPart);
        }
    }

    public void toggleNextTo(final Point p, final ToggleCallback toggler) {
        logger.debug("Search next point near {}", p);
        for (final TrackPart part : parts) {
            if (part.isNear(p)) {
                logger.debug("Found something : {}", part);
                final TrackPart invertedPart = part.toggle(toggler);
                replace(part, invertedPart);
                break;
            }
        }
    }

    public Optional<TrackPart> getPartNextTo(final Point p) {
        Optional<TrackPart> result = Optional.absent();
        for (final TrackPart part : parts) {
            if (part.isNear(p)) {
                logger.debug("Found something : {}", part);
                result = Optional.of(part);
            }
        }
        return result;
    }
}
