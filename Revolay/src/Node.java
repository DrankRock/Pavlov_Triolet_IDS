/**
 * @author Matvei Pavlov
 */
public class Node {
    private int virtualValue;
    private int physicalValue;
    public Node(int virtualValue, int physicalValue){
        this.virtualValue = virtualValue;
        this.physicalValue = physicalValue;
    }

    public int getVirtualValue() {
        return virtualValue;
    }

    public void setVirtualValue(int virtualValue) {
        this.virtualValue = virtualValue;
    }

    public int getPhysicalValue() {
        return physicalValue;
    }

    public void setPhysicalValue(int physicalValue) {
        this.physicalValue = physicalValue;
    }
}
