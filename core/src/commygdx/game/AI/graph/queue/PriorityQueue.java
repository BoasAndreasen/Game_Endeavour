package commygdx.game.AI.graph.queue;

import java.util.ArrayList;

public class PriorityQueue {
    private final ArrayList<PriorityItem> queue;

    public PriorityQueue() {
        queue = new ArrayList<PriorityItem>();
    }

    public void push(PriorityItem insert) {
        for (int i = 0; i < queue.size(); i++) {
            if (insert.priority < queue.get(i).priority) {
                queue.add(i, insert);
                return;
            }
        }
        queue.add(insert);
    }

    public PriorityItem pop() {
        return queue.remove(0);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
