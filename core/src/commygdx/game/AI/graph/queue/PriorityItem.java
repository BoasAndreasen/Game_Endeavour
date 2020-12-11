package commygdx.game.AI.graph.queue;

import commygdx.game.AI.graph.PathNode;

public class PriorityItem {
    public PathNode node;
    public PathNode firstStep;
    public float priority;
    public float pathCost;

    public PriorityItem(PathNode node, PathNode firstStep, float priority, float pathCost) {
        this.node = node;
        this.firstStep = firstStep;
        this.priority = priority;
        this.pathCost = pathCost;
    }
}
