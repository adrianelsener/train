package ch.adrianelsener.train.gui.swing.model;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collection;
import java.util.Optional;

public class GraphSwitch implements GraphTrackPart {
    private Optional<GraphTrackPart> parent = Optional.empty();
    private final Collection<GraphTrackPart> childs = Lists.newArrayList();

    @Override
    public void appendChild(GraphTrackPart other) {
        other.setParent(this);
        childs.add(other);
    }

    @Override
    public Optional<GraphTrackPart> getParent() {
        return parent;
    }

    @Override
    public ImmutableCollection<GraphTrackPart> getChilds() {
        return ImmutableList.copyOf(childs);
    }

    @Override
    public void setParent(GraphTrackPart parent) {
        this.parent = Optional.of(parent);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
