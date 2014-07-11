package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.SwitchId;
import com.beust.jcommander.internal.Maps;
import com.beust.jcommander.internal.Sets;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Map;
import java.util.Set;

public class GraphCreator {
    private final static Logger logger = LoggerFactory.getLogger(GraphCreator.class);

    public ImmutableCollection<GraphTrackPart> graphify(ImmutableCollection<TrackPart> trackParts) {
        final Map<SwitchId, GraphTrackPart> graphParts = Maps.newHashMap();
        for (TrackPart trackPartA : trackParts) {
            logger.debug("Use as main part {}", trackPartA);
            if (!trackPartA.isPipe()) {
                final GraphTrackPart graphParent = getOrCreateGraphPart(graphParts, trackPartA);
                final ImmutableCollection<Point> outConnectors = trackPartA.getOutConnectors();
                logger.debug("got this out connecotrs {}", outConnectors);
                for (Point outConnector : outConnectors) {
                    final Set<Point> additionalOut = Sets.newHashSet();
                    trackParts.forEach(part -> {
                        if (part != trackPartA) {
                            logger.debug("check '{}' if inConnecotrs are in list", part);
                            if (part.getInConnectors().contains(outConnector)) {
                                logger.debug("inConnecotrs of {} are matching to outConnector of {}", part, trackPartA);
                                if (!part.isPipe()) {
                                    logger.debug("not a pipe -> add");
                                    final GraphTrackPart other = getOrCreateGraphPart(graphParts, part);
                                    graphParent.appendChild(other);
                                } else {
                                    logger.debug("is a pipe -> use as additional out connector points");
                                    additionalOut.addAll(part.getOutConnectors());
                                }
                            }
                        }
                    });
                    additionalOut.removeAll(trackPartA.getOutConnectors());
                    logger.debug("this are the additional points to check {}", additionalOut);
                    additionalOut.forEach(outPoint -> {
                        trackParts.forEach(part -> {
                            logger.debug("Check if '{}' matches to additionalOut", part);
                            if (part != trackPartA && !part.isPipe()) {
                                if (part.getInConnectors().contains(outPoint)) {
                                    logger.debug("'{}' matched to additionalOut", part);
                                    final GraphTrackPart other = getOrCreateGraphPart(graphParts, part);
                                    graphParent.appendChild(other);
                                }
                            }
                        });
                    });
                }
                graphParts.put(graphParent.getId(), graphParent);
            }
        }
        return ImmutableSet.copyOf(graphParts.values());
    }

    private GraphTrackPart getOrCreateGraphPart(Map<SwitchId, GraphTrackPart> graphParts, TrackPart trackPartA) {
        final SwitchId id = trackPartA.getId();
        if (!graphParts.containsKey(id)) {
            graphParts.put(id, new GraphSwitch(id));
        }
        return graphParts.get(id);
    }
}
