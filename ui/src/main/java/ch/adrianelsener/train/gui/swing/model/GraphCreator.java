package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.SwitchId;
import com.google.common.base.Predicates;
import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphCreator {
    private final static Logger logger = LoggerFactory.getLogger(GraphCreator.class);

    public ImmutableCollection<GraphTrackPart> graphify(ImmutableCollection<TrackPart> trackParts) {
        final Map<SwitchId, GraphTrackPart> graphParts = Maps.newHashMap();
        for (TrackPart trackPartA : trackParts) {
            logger.debug("Use as main part {}", trackPartA);
            if (!trackPartA.isPipe()) {
                final Collection<GraphTrackPart> graphParents = getOrCreateGraphPart(graphParts, trackPartA);
                for (GraphTrackPart graphParent : graphParents) {
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
                                        final Collection<GraphTrackPart> others = getOrCreateGraphPart(graphParts, part);
                                        for (GraphTrackPart other : others) {
                                            graphParent.appendChild(other);
                                        }
                                    } else {
                                        logger.debug("is a pipe -> use as additional out connector points");
                                        final ImmutableCollection<Point> pipeOutConnector = part.getOutConnectors();
                                        final Collection<Point> points = Collections2.filter(pipeOutConnector, Predicates.not(Predicates.equalTo(outConnector)));
                                        final Collection<TrackPart> filteredParts = Collections2.filter(trackParts, Predicates.not(Predicates.equalTo(part)));
                                        final Collection<Point> pipedOutConnectors = followPipe(filteredParts, points);
                                        additionalOut.addAll(pipedOutConnectors);
//                                    additionalOut.addAll(part.getOutConnectors());
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
                                        final Collection<GraphTrackPart> others = getOrCreateGraphPart(graphParts, part);
                                        for (GraphTrackPart other : others) {
                                            graphParent.appendChild(other);
                                        }
                                    }
                                }
                            });
                        });
                    }
                    graphParts.put(graphParent.getId(), graphParent);
                }
            }
        }
        return ImmutableSet.copyOf(graphParts.values());
    }

    private Collection<Point> followPipe(Collection<TrackPart> trackParts, Collection<Point> points) {
        final ImmutableList.Builder<Point> listBuilder = ImmutableList.builder();
        for (TrackPart part : trackParts) {
            for (Point point : points) {
                if (part.getInConnectors().contains(point)) {
                    if (part.isPipe()) {
                        logger.debug("'{}' is a pipe", part);
                        final ImmutableCollection<Point> pipeOutConnector = part.getOutConnectors();
                        final Collection<Point> otherPoints = Collections2.filter(pipeOutConnector, Predicates.not(Predicates.equalTo(point)));
                        logger.debug("deeper check with '{}'", otherPoints);
                        final Collection<TrackPart> filteredParts = Collections2.filter(trackParts, Predicates.not(Predicates.equalTo(part)));
                        listBuilder.addAll(followPipe(filteredParts, otherPoints));
                    } else {
                        logger.debug("No pipe as result so result is '{}'", point);
                        return Lists.newArrayList(point);
                    }
                }
            }
        }
        return listBuilder.build();
    }

    private Collection<GraphTrackPart> getOrCreateGraphPart(Map<SwitchId, GraphTrackPart> graphParts, TrackPart trackPartA) {
        final List<GraphTrackPart> result = Lists.newArrayList();
        trackPartA.getId().forEach(part -> {
            final SwitchId id = part;
            if (!graphParts.containsKey(id)) {
                graphParts.put(id, new GraphSwitch(id));
            }
            result.add(graphParts.get(id));
        });
        return result;
    }
}
